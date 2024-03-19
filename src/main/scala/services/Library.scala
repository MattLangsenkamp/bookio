package services
import domain.Domain.Library
import cats.effect.{IO, Resource}
import skunk.Command
import skunk.Codec
import java.util.UUID

import skunk.codec.all.*
import skunk.*
import skunk.implicits.*
import domain.Domain.Book
import java.{util => ju}
import services.LibrarySQL.*

trait LibraryAlg[F[_]] {
  def createLibrary(
      userId: UUID,
      title: String,
      description: String
  ): F[Library]
  def addBookToLibrary(libraryId: UUID, bookId: UUID): F[Unit]
  def getLibraryBooks(id: UUID): F[Seq[UUID]]
  def getLibraries(userId: UUID): F[Seq[Library]]
}

object LibraryAlg:
  def createIO(postgres: Resource[IO, Session[IO]]): LibraryAlg[IO] =
    new LibraryAlg[IO] {
      def createLibrary(
          userId: UUID,
          title: String,
          description: String
      ): IO[Library] =
        IO.randomUUID.flatMap { id =>
          val nLibrary =
            Library(id, userId, title, description, Vector.empty[Book])
          postgres.use { s =>
            s.prepare(createLibraryCommand)
              .flatMap { pc =>
                pc.execute(nLibrary)
              }
              .void
          } *>
            IO.pure(nLibrary)
        }

      def getLibraries(userId: UUID): IO[Seq[Library]] =
        postgres.use { s =>
          s.prepare(libraryBookBridgeQuery).flatMap { lbpc =>
            s.prepare(selectLibrariesQuery).flatMap { lpc =>
              lpc
                .stream(userId, 32)
                .flatMap { l =>
                  val books = lbpc.stream(l.id, 32).compile.toVector
                }
            }
          }
        }

      def getLibraryBooks(id: UUID): IO[Seq[UUID]] = ???
      def addBookToLibrary(libraryId: UUID, bookId: UUID): IO[Unit] = ???
    }

object LibrarySQL:

  val libraryCodec: Codec[Library] =
    (uuid ~ uuid ~ varchar ~ varchar).imap {
      case id ~ userId ~ title ~ description =>
        Library(id, userId, title, description, Vector.empty[Book])
    }(l => l.id ~ l.userId ~ l.title ~ l.description)

  val createLibraryCommand: Command[Library] =
    sql"""
    INSERT INTO LIBRARY 
    VALUES($libraryCodec);
    """.command

  val libraryBookBridgeQuery: Query[UUID, UUID] =
    sql"""
    SELECT book_id FROM library_book_bridge
    WHERE library_id = $uuid 
    """.query(uuid)

  val selectLibrariesQuery: Query[UUID, Library] =
    sql"""
    SELECT * from library
    WHERE id = $uuid
    """.query(libraryCodec)

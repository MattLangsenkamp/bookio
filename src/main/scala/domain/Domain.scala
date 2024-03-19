package domain

import java.util.UUID
import java.time.LocalDateTime
import cats.data.NonEmptyList
import io.circe.*
import io.circe.syntax.*

object Domain:
  case class Library(
      id: UUID,
      userId: UUID,
      title: String,
      description: String,
      entries: Seq[Book]
  ) derives Codec.AsObject
  case class Book(
      id: UUID,
      title: String,
      author: String,
      pages: NonEmptyList[Page]
  ) derives Codec.AsObject
  case class Page(bookId: UUID, pageNumber: Int, contents: String)
      derives Codec.AsObject
  case class User(id: UUID, name: String, joinedDate: LocalDateTime)

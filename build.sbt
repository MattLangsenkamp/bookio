val scala3Version = "3.4.0"
val fly4sVersion = "1.0.1"
val postgresVersion = "42.7.0"
val flywayPostgresVersion = "10.7.2"

lazy val root = project
  .in(file("."))
  .settings(
    name := "bookio",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies ++= Seq(
      "org.scalameta" %% "munit" % "0.7.29" % Test,
      "org.http4s" %% "http4s-core" % "0.23.26",
      "org.http4s" %% "http4s-client" % "0.23.26",
      "org.http4s" %% "http4s-server" % "0.23.26",
      "org.http4s" %% "http4s-circe" % "0.23.26",
      "org.tpolecat" %% "skunk-core" % "0.6.3",
      "com.github.geirolz" %% "fly4s" % fly4sVersion,
      "org.postgresql" % "postgresql" % postgresVersion,
      "org.flywaydb" % "flyway-database-postgresql" % flywayPostgresVersion % "runtime"
    )
  )

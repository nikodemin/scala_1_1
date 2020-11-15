package ru.otus.sc.db

case class DbConfig(dbUrl: String, dbUserName: String, dbPassword: String)

object DbConfig {
  val default: DbConfig = DbConfig(
    dbUrl = "jdbc:postgresql://localhost:5432/postgres",
    dbUserName = "postgres",
    dbPassword = "postgres"
  )
}

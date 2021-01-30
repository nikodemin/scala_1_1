package ru.otus.sc.db

import slick.basic.BasicBackend
import slick.jdbc.PostgresProfile.api._
import zio.{Has, UIO, ZLayer, ZManaged}

object DbProvider {
  type Db = Has[BasicBackend#DatabaseDef]

  val live: ZLayer[Has[DbConfig], Nothing, Db] = ZLayer.fromServiceManaged { config =>
    ZManaged.fromAutoCloseable(UIO(Database.forURL(
      config.dbUrl,
      config.dbUserName,
      config.dbPassword)))
  }
}

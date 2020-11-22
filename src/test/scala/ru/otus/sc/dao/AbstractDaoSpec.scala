package ru.otus.sc.dao

import com.typesafe.config.ConfigFactory
import ru.otus.sc.dao.util.Implicits._
import ru.otus.sc.db.DbProvider.Db
import ru.otus.sc.db.Migrations.Migrations
import ru.otus.sc.db.{DbConfig, DbProvider, Migrations}
import slick.basic.BasicBackend
import slick.jdbc.PostgresProfile.api._
import zio._
import zio.blocking.Blocking
import zio.test.DefaultRunnableSpec

abstract class AbstractDaoSpec extends DefaultRunnableSpec {
  private val configLayer = {
    val config = ConfigFactory.load.getConfig("postgres")
    ZLayer.succeed(DbConfig(config.getString("url"), config.getString("user"),
      config.getString("password")))
  }
  val dbLayer: RLayer[Any, Db] = configLayer >>> DbProvider.live
  val migrationsLayer: RLayer[Any, Migrations] = configLayer >>> Migrations.live

  def executeAction[T](action: DBIO[T]): RIO[Blocking, T] = dbLayer.build.use {
    db => {
      implicit val dbDef: BasicBackend#DatabaseDef = db.get
      action
    }
  }
}


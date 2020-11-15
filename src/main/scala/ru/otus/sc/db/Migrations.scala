package ru.otus.sc.db

import org.flywaydb.core.Flyway
import zio._
import zio.blocking.{Blocking, effectBlocking}
import zio.clock.Clock

object Migrations {
  type Migrations = Has[Service]

  type Env = Blocking with Clock

  trait Service {
    def applyMigrations(): URIO[Env, Unit]
  }

  val live: RLayer[Has[DbConfig], Migrations] = ZLayer.fromService { config =>
    new Service {
      def applyMigrations(): URIO[Env, Unit] =
        effectBlocking {
          Flyway
            .configure()
            .dataSource(config.dbUrl, config.dbUserName, config.dbPassword)
            .load()
            .migrate()
        }.orDie.unit
    }
  }
}

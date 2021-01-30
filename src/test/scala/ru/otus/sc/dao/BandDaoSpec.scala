package ru.otus.sc.dao

import ru.otus.sc.db.Migrations
import ru.otus.sc.model.entity.MusicEntities._
import slick.jdbc.H2Profile.api._
import zio._
import zio.test.Assertion._
import zio.test._
import zio.test.magnolia.DeriveGen

object BandDaoSpec extends AbstractDaoSpec {
  private val bandDaoLayer = (dbLayer >>> BandDao.live ++ migrationsLayer).mapError(TestFailure.fail)
  private val bandGen = DeriveGen[BandRow]

  override def spec = suite("BandDaoSpec") {
    testM("Should get by singer name") {
      (for {
        _ <- executeAction((bands.schema ++ albums.schema ++ tracks.schema).dropIfExists)
        migrationsService <- ZIO.service[Migrations.Service]
        _ <- migrationsService.applyMigrations()
      } yield ()) *>
        checkM(bandGen) { band =>
          for {
            _ <- executeAction(bands += band)
            bandDao <- ZIO.service[BandDao.Service]
            actualBandList = bandDao.getBySinger(band.singer).map(_.map(_._1))
            res <- assertM(actualBandList) {
              contains(band) && hasSize(equalTo(1))
            }
          } yield res
        }
    }

  }
    .provideSomeLayerShared[Environment](bandDaoLayer)

  //    .@@(after(executeAction(bands.delete)))
}

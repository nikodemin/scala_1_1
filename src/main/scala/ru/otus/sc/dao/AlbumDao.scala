package ru.otus.sc.dao

import java.util.UUID

import ru.otus.sc.dao.impl.AlbumDaoImpl
import ru.otus.sc.db.DbProvider
import ru.otus.sc.model.entity.MusicEntities.{AlbumRow, TrackRow}
import slick.basic.BasicBackend
import zio.{Has, RLayer, Task, ZLayer}

/**
 * Album DAO
 */
object AlbumDao {
  type AlbumDao = Has[Service]

  trait Service extends AbstractDao[AlbumRow, (AlbumRow, TrackRow)] {
    def getByBand(bandId: UUID): Task[List[(AlbumRow, TrackRow)]]
  }

  val live: RLayer[DbProvider.Db, AlbumDao] = ZLayer.fromFunction(db => {
    implicit val dbDef: BasicBackend#DatabaseDef = db.get
    new AlbumDaoImpl
  })
}

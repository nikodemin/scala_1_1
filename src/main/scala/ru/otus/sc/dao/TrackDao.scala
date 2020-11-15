package ru.otus.sc.dao

import java.util.UUID

import ru.otus.sc.dao.impl.TrackDaoImpl
import ru.otus.sc.db.DbProvider
import ru.otus.sc.model.entity.MusicEntities.TrackRow
import slick.basic.BasicBackend
import zio.{Has, RLayer, Task, ZLayer}

/**
 * Track DAO
 */
object TrackDao {
  type TrackDao = Has[Service]

  trait Service extends AbstractDao[TrackRow, TrackRow] {
    def getByAlbum(albumID: UUID): Task[List[TrackRow]]

    def getByAlbumName(albumName: String): Task[List[TrackRow]]
  }

  val live: RLayer[DbProvider.Db, TrackDao] = ZLayer.fromFunction(db => {
    implicit val dbRef: BasicBackend#DatabaseDef = db.get
    new TrackDaoImpl
  })
}

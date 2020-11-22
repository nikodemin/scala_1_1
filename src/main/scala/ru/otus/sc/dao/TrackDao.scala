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

  trait Service {
    def getByAlbum(albumID: UUID): Task[List[TrackRow]]

    def getByAlbumName(albumName: String): Task[List[TrackRow]]

    def getByName(name: String): Task[List[TrackRow]]

    def getByNameContaining(name: String): Task[List[TrackRow]]

    def getById(id: UUID, forUpdate: Boolean): Task[List[TrackRow]]

    def getAll: Task[List[TrackRow]]

    def add(element: TrackRow): Task[Boolean]

    def update(element: TrackRow): Task[Boolean]

    def deleteById(id: UUID): Task[Boolean]
  }

  val live: RLayer[DbProvider.Db, TrackDao] = ZLayer.fromFunction(db => {
    implicit val dbRef: BasicBackend#DatabaseDef = db.get
    new TrackDaoImpl
  })
}

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

  trait Service {
    def getByBand(bandId: UUID): Task[List[(AlbumRow, TrackRow)]]

    def getByName(name: String): Task[List[(AlbumRow, TrackRow)]]

    def getByNameContaining(name: String): Task[List[(AlbumRow, TrackRow)]]

    def getById(id: UUID, forUpdate: Boolean): Task[List[(AlbumRow, TrackRow)]]

    def getAll: Task[List[(AlbumRow, TrackRow)]]

    def add(element: AlbumRow): Task[Boolean]

    def update(element: AlbumRow): Task[Boolean]

    def deleteById(id: UUID): Task[Boolean]
  }

  val live: RLayer[DbProvider.Db, AlbumDao] = ZLayer.fromFunction(db => {
    implicit val dbDef: BasicBackend#DatabaseDef = db.get
    new AlbumDaoImpl
  })
}

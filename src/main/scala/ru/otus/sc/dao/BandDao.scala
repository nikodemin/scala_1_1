package ru.otus.sc.dao

import java.util.UUID

import ru.otus.sc.dao.impl.BandDaoImpl
import ru.otus.sc.db.DbProvider
import ru.otus.sc.model.entity.MusicEntities.{AlbumRow, BandRow}
import slick.basic.BasicBackend
import zio.{Has, RLayer, Task, ZLayer}

/**
 * Band DAO
 */
object BandDao {
  type BandDao = Has[Service]

  trait Service {
    def getBySinger(name: String): Task[List[(BandRow, AlbumRow)]]

    def getByName(name: String): Task[List[(BandRow, AlbumRow)]]

    def getByNameContaining(name: String): Task[List[(BandRow, AlbumRow)]]

    def getById(id: UUID, forUpdate: Boolean): Task[List[(BandRow, AlbumRow)]]

    def getAll: Task[List[(BandRow, AlbumRow)]]

    def add(element: BandRow): Task[Boolean]

    def update(element: BandRow): Task[Boolean]

    def deleteById(id: UUID): Task[Boolean]
  }

  val live: RLayer[DbProvider.Db, BandDao] = ZLayer.fromFunction(db => {
    implicit val dbDef: BasicBackend#DatabaseDef = db.get
    new BandDaoImpl
  })
}

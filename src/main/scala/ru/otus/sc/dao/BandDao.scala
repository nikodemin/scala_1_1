package ru.otus.sc.dao

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

  trait Service extends AbstractDao[BandRow, (BandRow, AlbumRow)] {
    def getBySinger(name: String): Task[List[(BandRow, AlbumRow)]]
  }

  val live: RLayer[DbProvider.Db, BandDao] = ZLayer.fromFunction(db => {
    implicit val dbDef: BasicBackend#DatabaseDef = db.get
    new BandDaoImpl
  })
}

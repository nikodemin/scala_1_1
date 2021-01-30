package ru.otus.sc.dao.impl


import java.util.UUID

import ru.otus.sc.dao.BandDao
import ru.otus.sc.dao.util.Implicits._
import ru.otus.sc.model.entity.MusicEntities._
import slick.basic.BasicBackend
import slick.jdbc.PostgresProfile.api._
import zio.Task

class BandDaoImpl(implicit db: BasicBackend#DatabaseDef) extends BandDao.Service {
  override def getBySinger(name: String): Task[List[(BandRow, AlbumRow)]] = {
    joinAlbum.filter(_._1.singer.toLowerCase === name.toLowerCase).result
  }

  override def getByName(name: String): Task[List[(BandRow, AlbumRow)]] = {
    joinAlbum.filter(_._1.name.toLowerCase === name.toLowerCase).result
  }

  override def getByNameContaining(name: String): Task[List[(BandRow, AlbumRow)]] = {
    joinAlbum.filter(_._1.name.toLowerCase like s"%${name.toLowerCase}%").result
  }

  override def getById(id: UUID, forUpdate: Boolean): Task[List[(BandRow, AlbumRow)]] = {
    val query = joinAlbum.filter(_._1.id === id)
    if (forUpdate) query.forUpdate.result else query.result
  }

  override def getAll: Task[List[(BandRow, AlbumRow)]] = joinAlbum.result

  override def add(band: BandRow): Task[Boolean] = bands += band

  override def update(band: BandRow): Task[Boolean] = {
    bands.filter(_.id === band.id.get)
      .map(b => (b.name, b.singer, b.established))
      .update((band.name, band.singer, band.established))
  }

  override def deleteById(id: UUID): Task[Boolean] = bands.filter(_.id === id).delete

  private def joinAlbum: Query[(Band, Album), (BandRow, AlbumRow), Seq] = bands join albums on (_.id === _.bandId)
}

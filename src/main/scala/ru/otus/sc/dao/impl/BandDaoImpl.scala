package ru.otus.sc.dao.impl

import java.util.UUID

import ru.otus.sc.dao.BandDao
import ru.otus.sc.model.entity.MusicEntities._
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{ExecutionContext, Future}


class BandDaoImpl(db: Database)(implicit ec: ExecutionContext) extends BandDao {
  override def getBySinger(name: String): Future[List[(BandRow, AlbumRow)]] = {
    val action = joinAlbum.filter(_._1.singer.toLowerCase === name.toLowerCase).result
    db.run(action).map(_.toList)
  }

  override def getByName(name: String): Future[List[(BandRow, AlbumRow)]] = {
    val action = joinAlbum.filter(_._1.name.toLowerCase === name.toLowerCase).result
    db.run(action).map(_.toList)
  }

  override def getByNameContaining(name: String): Future[List[(BandRow, AlbumRow)]] = {
    val action = joinAlbum.filter(_._1.name.toLowerCase like s"%${name.toLowerCase}%").result
    db.run(action).map(_.toList)
  }

  override def getById(id: UUID, forUpdate: Boolean): Future[List[(BandRow, AlbumRow)]] = {
    val query = joinAlbum.filter(_._1.id === id)
    val resQuery = if (forUpdate) query.forUpdate else query
    db.run(resQuery.result).map(_.toList)
  }

  override def getAll: Future[List[(BandRow, AlbumRow)]] = {
    val action = joinAlbum.result
    db.run(action).map(_.toList)
  }

  override def add(band: BandRow): Future[Boolean] = {
    val action = bands += band
    db.run(action).map(_ > 0)
  }

  override def update(band: BandRow): Future[Boolean] = {
    val action = bands.filter(_.id === band.id.get)
      .map(b => (b.name, b.singer, b.established))
      .update((band.name, band.singer, band.established))
    db.run(action).map(_ > 0)
  }

  override def deleteById(id: UUID): Future[Boolean] = {
    val action = bands.filter(_.id === id).delete
    db.run(action).map(_ > 0)
  }

  private def joinAlbum: Query[(Band, Album), (BandRow, AlbumRow), Seq] = bands join albums on (_.id === _.bandId)

}

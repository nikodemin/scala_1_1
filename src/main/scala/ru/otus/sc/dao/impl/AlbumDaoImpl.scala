package ru.otus.sc.dao.impl

import java.util.UUID

import ru.otus.sc.dao.AlbumDao
import ru.otus.sc.model.entity.MusicEntities._
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{ExecutionContext, Future}


class AlbumDaoImpl(db: Database)(implicit ec: ExecutionContext) extends AlbumDao {
  override def getByBand(bandId: UUID): Future[List[(AlbumRow, TrackRow)]] = {
    val action = joinTracks.filter(_._1.bandId === bandId).result
    db.run(action).map(_.toList)
  }

  override def getByName(name: String): Future[List[(AlbumRow, TrackRow)]] = {
    val action = joinTracks.filter(_._1.name === name).result
    db.run(action).map(_.toList)
  }

  override def getByNameContaining(name: String): Future[List[(AlbumRow, TrackRow)]] = {
    val action = joinTracks.filter(_._1.name.toLowerCase like s"%${name.toLowerCase}%").result
    db.run(action).map(_.toList)
  }

  override def getById(id: UUID, forUpdate: Boolean): Future[List[(AlbumRow, TrackRow)]] = {
    val query = joinTracks.filter(_._1.id === id)
    val resQuery = if (forUpdate) query.forUpdate else query
    db.run(resQuery.forUpdate.result).map(_.toList)
  }

  override def getAll: Future[List[(AlbumRow, TrackRow)]] = db.run(joinTracks.result).map(_.toList)

  override def add(album: AlbumRow): Future[Boolean] = {
    val action = bands.filter(_.id === album.bandId).exists.result.flatMap {
      case true => albums += album
      case false => DBIO.from(Future(0))
    }
    db.run(action).map(_ > 0)
  }

  override def update(album: AlbumRow): Future[Boolean] = {
    val action = albums.filter(_.id === album.id)
      .map(a => (a.name, a.year, a.bandId))
      .update((album.name, album.year, album.bandId))
    db.run(action).map(_ > 0)
  }

  override def deleteById(id: UUID): Future[Boolean] = db.run(albums.filter(_.id === id).delete).map(_ > 0)

  private def joinTracks = albums join tracks on (_.id === _.albumId)

}

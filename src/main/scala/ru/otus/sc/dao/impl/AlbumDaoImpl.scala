package ru.otus.sc.dao.impl

import java.util.UUID

import ru.otus.sc.dao.AlbumDao
import ru.otus.sc.dao.util.Implicits._
import ru.otus.sc.model.entity.MusicEntities._
import slick.basic.BasicBackend
import slick.jdbc.PostgresProfile.api._
import zio.{Task, ZIO}

class AlbumDaoImpl(implicit db: BasicBackend#DatabaseDef) extends AlbumDao.Service {
  override def getByBand(bandId: UUID): Task[List[(AlbumRow, TrackRow)]] = {
    joinTracks.filter(_._1.bandId === bandId).result
  }

  override def getByName(name: String): Task[List[(AlbumRow, TrackRow)]] = {
    joinTracks.filter(_._1.name.toLowerCase === name.toLowerCase).result
  }

  override def getByNameContaining(name: String): Task[List[(AlbumRow, TrackRow)]] = {
    joinTracks.filter(_._1.name.toLowerCase like s"%${name.toLowerCase}%").result
  }

  override def getById(id: UUID, forUpdate: Boolean): Task[List[(AlbumRow, TrackRow)]] = {
    val query = joinTracks.filter(_._1.id === id)
    if (forUpdate) query.forUpdate.result else query.result
  }

  override def getAll: Task[List[(AlbumRow, TrackRow)]] = joinTracks.result

  override def add(album: AlbumRow): Task[Boolean] = {
    val isBandExists: Task[Boolean] = bands.filter(_.id === album.bandId).exists.result
    isBandExists.flatMap {
      case true => albums += album
      case false => ZIO.succeed(false)
    }
  }

  override def update(album: AlbumRow): Task[Boolean] = {
    albums.filter(_.id === album.id)
      .map(a => (a.name, a.year, a.bandId))
      .update((album.name, album.year, album.bandId))
  }

  override def deleteById(id: UUID): Task[Boolean] = albums.filter(_.id === id).delete

  private def joinTracks = albums join tracks on (_.id === _.albumId)
}

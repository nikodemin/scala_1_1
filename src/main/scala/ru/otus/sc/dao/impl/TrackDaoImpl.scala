package ru.otus.sc.dao.impl

import java.util.UUID

import ru.otus.sc.dao.TrackDao
import ru.otus.sc.dao.util.Implicits._
import ru.otus.sc.model.entity.MusicEntities._
import slick.basic.BasicBackend
import slick.jdbc.PostgresProfile.api._
import zio.{Task, ZIO}

class TrackDaoImpl(implicit db: BasicBackend#DatabaseDef) extends TrackDao.Service {
  override def getByAlbum(albumId: UUID): Task[List[TrackRow]] = {
    tracks.filter(_.albumId === albumId).result
  }

  override def getByName(name: String): Task[List[TrackRow]] = {
    tracks.filter(_.name.toLowerCase === name.toLowerCase).result
  }

  override def getByNameContaining(name: String): Task[List[TrackRow]] = {
    tracks.filter(_.name.toLowerCase like s"%${name.toLowerCase}%").result
  }

  override def getById(id: UUID, forUpdate: Boolean): Task[List[TrackRow]] = {
    val query = tracks.filter(_.id === id)
    if (forUpdate) query.forUpdate.result else query.result
  }

  override def getAll: Task[List[TrackRow]] = tracks.result

  override def add(track: TrackRow): Task[Boolean] = {
    val isAlbumExists: Task[Boolean] = albums.filter(_.id === track.albumId).exists.result
    isAlbumExists.flatMap {
      case true => tracks += track
      case false => ZIO.succeed(false)
    }
  }

  override def update(track: TrackRow): Task[Boolean] = {
    tracks.filter(_.id === track.id)
      .map(t => (t.name, t.duration, t.albumId))
      .update((track.name, track.duration, track.albumId))
  }

  override def deleteById(id: UUID): Task[Boolean] = tracks.filter(_.id === id).delete

  override def getByAlbumName(albumName: String): Task[List[TrackRow]] = {
    albums.filter(_.name === albumName).flatMap(a => tracks.filter(_.albumId === a.id)).result
  }
}

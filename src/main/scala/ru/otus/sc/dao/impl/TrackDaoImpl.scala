package ru.otus.sc.dao.impl

import java.util.UUID

import ru.otus.sc.dao.TrackDao
import ru.otus.sc.model.entity.MusicEntities._
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{ExecutionContext, Future}

class TrackDaoImpl(db: Database)(implicit ec: ExecutionContext) extends TrackDao {
  override def getByAlbum(albumId: UUID): Future[List[TrackRow]] = {
    val action = tracks.filter(_.albumId === albumId).result
    db.run(action).map(_.toList)
  }

  override def getByName(name: String): Future[List[TrackRow]] = {
    val action = tracks.filter(_.name.toLowerCase === name.toLowerCase).result
    db.run(action).map(_.toList)
  }

  override def getByNameContaining(name: String): Future[List[TrackRow]] = {
    val action = tracks.filter(_.name.toLowerCase like s"%${name.toLowerCase}%").result
    db.run(action).map(_.toList)
  }

  override def getById(id: UUID, forUpdate: Boolean): Future[List[TrackRow]] = {
    val query = tracks.filter(_.id === id)
    val resQuery = if (forUpdate) query.forUpdate else query
    db.run(resQuery.result).map(_.toList)
  }

  override def getAll: Future[List[TrackRow]] = db.run(tracks.result).map(_.toList)

  override def add(track: TrackRow): Future[Boolean] = {
    val action = albums.filter(_.id === track.albumId).exists.result.flatMap {
      case true => tracks += track
      case false => DBIO.from(Future(0))
    }
    db.run(action).map(_ > 0)
  }

  override def update(track: TrackRow): Future[Boolean] = {
    val action = tracks.filter(_.id === track.id)
      .map(t => (t.name, t.duration, t.albumId))
      .update((track.name, track.duration, track.albumId))
    db.run(action).map(_ > 0)
  }

  override def deleteById(id: UUID): Future[Boolean] = db.run(tracks.filter(_.id === id).delete).map(_ > 0)

  override def getByAlbumName(albumName: String): Future[List[TrackRow]] = {
    val action = albums.filter(_.name === albumName).flatMap(a => tracks.filter(_.albumId === a.id)).result
    db.run(action).map(_.toList)
  }

}

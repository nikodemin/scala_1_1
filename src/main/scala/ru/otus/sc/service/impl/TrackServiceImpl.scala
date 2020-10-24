package ru.otus.sc.service.impl

import java.util.UUID

import ru.otus.sc.dao.TrackDao
import ru.otus.sc.model.dto._
import ru.otus.sc.service.TrackService

import scala.concurrent.{ExecutionContext, Future}

class TrackServiceImpl(trackDao: TrackDao)(implicit ec: ExecutionContext) extends TrackService {
  override def getAll: Future[List[TrackGetDto]] = trackDao.getAll.map(_.map(TrackGetDto.fromTrackRow))

  override def getByAlbum(albumID: UUID): Future[List[TrackGetDto]] = trackDao.getByAlbum(albumID).map(_.map(TrackGetDto.fromTrackRow))

  override def getByName(name: String): Future[List[TrackGetDto]] = trackDao.getByName(name).map(_.map(TrackGetDto.fromTrackRow))

  override def getById(id: UUID): Future[Option[TrackGetDto]] = trackDao.getById(id, forUpdate = false).map(_.map(TrackGetDto.fromTrackRow).headOption)

  override def add(trackAddDto: TrackAddDto): Future[Boolean] = trackDao.add(trackAddDto.toTrackRow)

  override def update(trackUpdateDto: TrackUpdateDto): Future[Boolean] = trackDao.getById(trackUpdateDto.id, forUpdate = true)
    .map(_.headOption).flatMap {
    case None => Future(false)
    case Some(track) => trackDao.update(track.copy(
      name = trackUpdateDto.name.getOrElse(track.name),
      duration = trackUpdateDto.duration.getOrElse(track.duration),
      albumId = trackUpdateDto.album.getOrElse(track.albumId)
    ))
  }

  override def delete(id: UUID): Future[Boolean] = trackDao.deleteById(id)

  override def getByAlbumName(name: String): Future[List[TrackGetDto]] = trackDao.getByAlbumName(name).map(_.map(TrackGetDto.fromTrackRow))
}

package ru.otus.sc.service.impl

import java.util.UUID

import ru.otus.sc.dao.TrackDao
import ru.otus.sc.model.dto._
import ru.otus.sc.service.TrackService
import zio.Task

class TrackServiceImpl(trackDao: TrackDao.Service) extends TrackService.Service {

  override def getAll: Task[List[TrackGetDto]] = trackDao.getAll.map(_.map(TrackGetDto.fromTrackRow))

  override def getByAlbum(albumID: UUID): Task[List[TrackGetDto]] = trackDao.getByAlbum(albumID).map(_.map(TrackGetDto.fromTrackRow))

  override def getByName(name: String): Task[List[TrackGetDto]] = trackDao.getByName(name).map(_.map(TrackGetDto.fromTrackRow))

  override def getById(id: UUID): Task[Option[TrackGetDto]] = trackDao.getById(id, forUpdate = false).map(_.map(TrackGetDto.fromTrackRow).headOption)

  override def add(trackAddDto: TrackAddDto): Task[Boolean] = trackDao.add(trackAddDto.toTrackRow)

  override def update(trackUpdateDto: TrackUpdateDto): Task[Boolean] = trackDao.getById(trackUpdateDto.id, forUpdate = true)
    .map(_.headOption).flatMap {
    case None => Task(false)
    case Some(track) => trackDao.update(track.copy(
      name = trackUpdateDto.name.getOrElse(track.name),
      duration = trackUpdateDto.duration.getOrElse(track.duration),
      albumId = trackUpdateDto.album.getOrElse(track.albumId)
    ))
  }

  override def delete(id: UUID): Task[Boolean] = trackDao.deleteById(id)

  override def getByAlbumName(name: String): Task[List[TrackGetDto]] = trackDao.getByAlbumName(name).map(_.map(TrackGetDto.fromTrackRow))
}

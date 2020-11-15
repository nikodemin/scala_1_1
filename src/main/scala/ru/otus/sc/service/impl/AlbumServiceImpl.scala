package ru.otus.sc.service.impl

import java.util.UUID

import ru.otus.sc.dao.AlbumDao
import ru.otus.sc.model.dto._
import ru.otus.sc.service.AlbumService
import zio.Task

class AlbumServiceImpl(albumDao: AlbumDao.Service) extends AlbumService.Service {

  override def getByBand(bandId: UUID): Task[List[AlbumGetDto]] = albumDao.getByBand(bandId).map(AlbumGetDto.fromTupleList)

  override def getAll: Task[List[AlbumGetDto]] = albumDao.getAll.map(AlbumGetDto.fromTupleList)

  override def getByName(name: String): Task[List[AlbumGetDto]] = albumDao.getByName(name).map(AlbumGetDto.fromTupleList)

  override def getById(id: UUID): Task[Option[AlbumGetDto]] = albumDao.getById(id, forUpdate = false).map(AlbumGetDto.fromTupleList(_).headOption)

  override def add(albumAddDto: AlbumAddDto): Task[Boolean] = albumDao.add(albumAddDto.toAlbumRow)

  override def update(albumUpdateDto: AlbumUpdateDto): Task[Boolean] = albumDao.getById(albumUpdateDto.id, forUpdate = true)
    .map(_.map(_._1).headOption).flatMap {
    case None => Task(false)
    case Some(album) => albumDao.update(album.copy(
      name = albumUpdateDto.name.getOrElse(album.name),
      bandId = albumUpdateDto.band.getOrElse(album.bandId),
      year = albumUpdateDto.year.getOrElse(album.year)
    ))
  }

  override def delete(id: UUID): Task[Boolean] = albumDao.deleteById(id)
}

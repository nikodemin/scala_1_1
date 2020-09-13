package ru.otus.sc.service.impl

import java.util.UUID

import ru.otus.sc.dao.AlbumDao
import ru.otus.sc.model.dto._
import ru.otus.sc.service.AlbumService

import scala.concurrent.{ExecutionContext, Future}

class AlbumServiceImpl(albumDao: AlbumDao)(implicit ec: ExecutionContext) extends AlbumService {
  override def getByBand(bandId: UUID): Future[List[AlbumGetDto]] = albumDao.getByBand(bandId).map(AlbumGetDto.fromTupleList)

  override def getAll: Future[List[AlbumGetDto]] = albumDao.getAll.map(AlbumGetDto.fromTupleList)

  override def getByName(name: String): Future[List[AlbumGetDto]] = albumDao.getByName(name).map(AlbumGetDto.fromTupleList)

  override def getById(id: UUID): Future[Option[AlbumGetDto]] = albumDao.getById(id, forUpdate = false).map(AlbumGetDto.fromTupleList(_).headOption)

  override def add(albumAddDto: AlbumAddDto): Future[Boolean] = albumDao.add(albumAddDto.toAlbumRow)

  override def update(albumUpdateDto: AlbumUpdateDto): Future[Boolean] = albumDao.getById(albumUpdateDto.id, forUpdate = true)
    .map(_.map(_._1).headOption).flatMap {
    case None => Future(false)
    case Some(album) => albumDao.update(album.copy(
      name = albumUpdateDto.name.getOrElse(album.name),
      bandId = albumUpdateDto.band.getOrElse(album.bandId),
      year = albumUpdateDto.year.getOrElse(album.year)
    ))
  }

  override def delete(id: UUID): Future[Boolean] = albumDao.deleteById(id)
}

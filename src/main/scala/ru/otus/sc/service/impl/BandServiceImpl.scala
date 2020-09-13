package ru.otus.sc.service.impl

import java.util.UUID

import ru.otus.sc.dao.BandDao
import ru.otus.sc.model.dto._
import ru.otus.sc.service.BandService

import scala.concurrent.{ExecutionContext, Future}

class BandServiceImpl(bandDao: BandDao)(implicit ec: ExecutionContext) extends BandService {

  override def getById(id: UUID): Future[Option[BandGetDto]] = bandDao.getById(id, forUpdate = false).map(BandGetDto.fromTupleList(_).headOption)

  override def getAll: Future[List[BandGetDto]] = bandDao.getAll.map(BandGetDto.fromTupleList)

  override def getByName(name: String): Future[Option[BandGetDto]] = bandDao.getByName(name).map(BandGetDto.fromTupleList(_).headOption)

  override def getBySinger(name: String): Future[List[BandGetDto]] = bandDao.getBySinger(name).map(BandGetDto.fromTupleList)

  override def add(band: BandAddDto): Future[Boolean] = bandDao.add(band.toBandRow)

  override def update(bandUpdateDto: BandUpdateDto): Future[Boolean] = {
    bandDao.getById(bandUpdateDto.id, forUpdate = true).map(_.map(_._1).headOption).flatMap {
      case None => Future(false)
      case Some(bandRow) => bandDao.update(bandRow.copy(
        name = bandUpdateDto.name.getOrElse(bandRow.name),
        singer = bandUpdateDto.singer.getOrElse(bandRow.singer),
        established = bandUpdateDto.established.getOrElse(bandRow.established)
      ))
    }
  }

  override def delete(id: UUID): Future[Boolean] = bandDao.deleteById(id)

}

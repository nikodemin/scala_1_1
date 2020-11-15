package ru.otus.sc.service.impl

import java.util.UUID

import ru.otus.sc.dao.BandDao
import ru.otus.sc.model.dto._
import ru.otus.sc.service.BandService
import zio.Task

class BandServiceImpl(bandDao: BandDao.Service) extends BandService.Service {

  override def getById(id: UUID): Task[Option[BandGetDto]] = bandDao.getById(id, forUpdate = false).map(BandGetDto.fromTupleList(_).headOption)

  override def getAll: Task[List[BandGetDto]] = bandDao.getAll.map(BandGetDto.fromTupleList)

  override def getByName(name: String): Task[Option[BandGetDto]] = bandDao.getByName(name).map(BandGetDto.fromTupleList(_).headOption)

  override def getBySinger(name: String): Task[List[BandGetDto]] = bandDao.getBySinger(name).map(BandGetDto.fromTupleList)

  override def add(band: BandAddDto): Task[Boolean] = bandDao.add(band.toBandRow)

  override def update(bandUpdateDto: BandUpdateDto): Task[Boolean] = {
    bandDao.getById(bandUpdateDto.id, forUpdate = true).map(_.map(_._1).headOption).flatMap {
      case None => Task(false)
      case Some(bandRow) => bandDao.update(bandRow.copy(
        name = bandUpdateDto.name.getOrElse(bandRow.name),
        singer = bandUpdateDto.singer.getOrElse(bandRow.singer),
        established = bandUpdateDto.established.getOrElse(bandRow.established)
      ))
    }
  }

  override def delete(id: UUID): Task[Boolean] = bandDao.deleteById(id)

}

package ru.otus.sc.dao.impl

import java.util.UUID

import ru.otus.sc.dao.BandDao
import ru.otus.sc.model.entity.Band

class BandDaoImpl extends BandDao {
  override def getBySinger(name: String): List[Band] = Bands.all.filter(band => band.singer == name)

  override def getByName(name: String): Option[Band] = Bands.all.find(band => band.name == name)

  override def getById(id: UUID): Option[Band] = Bands.all.find(band => band.id == id)

  override def getAll: List[Band] = Bands.all
}

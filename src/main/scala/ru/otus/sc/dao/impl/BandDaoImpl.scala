package ru.otus.sc.dao.impl

import java.util.UUID

import ru.otus.sc.dao.BandDao
import ru.otus.sc.model.entity.Band

class BandDaoImpl extends BandDao {
  override def getBySinger(name: String): List[Band] = Bands.all.filter(band => band.singer == name).toList

  override def getByName(name: String): Option[Band] = Bands.all.find(band => band.name == name)

  override def getById(id: UUID): Option[Band] = Bands.all.find(band => band.id == id)

  override def getAll: List[Band] = Bands.all.toList

  override def add(element: Band): Boolean = {
    Bands.all.addOne(element)
    true
  }

  override def update(element: Band): Boolean = {
    val index = Bands.all.indexWhere(band => band.id == element.id)

    if (index == -1) {
      false
    } else {
      Bands.all.remove(index)
      Bands.all.addOne(element)
      true
    }
  }

  override def deleteById(id: UUID): Boolean = {
    val index = Bands.all.indexWhere(band => band.id == id)

    if (index == -1) {
      false
    } else {
      Bands.all.remove(index)
      true
    }
  }
}

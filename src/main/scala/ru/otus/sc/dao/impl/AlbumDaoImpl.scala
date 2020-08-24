package ru.otus.sc.dao.impl

import java.util.UUID

import ru.otus.sc.dao.AlbumDao
import ru.otus.sc.model.entity.{Album, Band}


class AlbumDaoImpl extends AlbumDao {
  override def getByBand(band: Band): List[Album] = Albums.all.filter(album => album.band == band.id).toList

  override def getByName(name: String): Option[Album] = Albums.all.find(album => album.name == name)

  override def getById(id: UUID): Option[Album] = Albums.all.find(album => album.id == id)

  override def getAll: List[Album] = Albums.all.toList

  override def add(element: Album): Boolean = {
    Albums.all.addOne(element)
    true
  }

  override def update(element: Album): Boolean = {
    val index = Albums.all.indexWhere(album => album.id == element.id)

    if (index == -1) {
      false
    } else {
      Albums.all.remove(index)
      Albums.all.addOne(element)
      true
    }
  }

  override def deleteById(id: UUID): Boolean = {
    val index = Albums.all.indexWhere(album => album.id == id)

    if (index == -1) {
      false
    } else {
      Albums.all.remove(index)
      true
    }
  }
}

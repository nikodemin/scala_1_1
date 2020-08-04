package ru.otus.sc.dao.impl

import java.util.UUID

import ru.otus.sc.dao.AlbumDao
import ru.otus.sc.model.entity.{Album, Band}


class AlbumDaoImpl extends AlbumDao {
  override def getByBand(band: Band): List[Album] = Albums.all.filter(album => album.band == band.id)

  override def getByName(name: String): Option[Album] = Albums.all.find(album => album.name == name)

  override def getById(id: UUID): Option[Album] = Albums.all.find(album => album.id == id)

  override def getAll: List[Album] = Albums.all
}

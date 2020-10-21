package ru.otus.sc.dao.impl

import java.util.UUID

import ru.otus.sc.dao.TrackDao
import ru.otus.sc.model.entity.{Album, Track}

class TrackDaoImpl extends TrackDao {
  override def getByAlbum(album: Album): List[Track] = Tracks.all.filter(track => track.album == album.id).toList

  override def getByName(name: String): Option[Track] = Tracks.all.find(track => track.name == name)

  override def getById(id: UUID): Option[Track] = Tracks.all.find(track => track.id == id)

  override def getAll: List[Track] = Tracks.all.toList

  override def add(element: Track): Boolean = {
    Tracks.all.addOne(element)
    true
  }

  override def update(element: Track): Boolean = {
    val index = Tracks.all.indexWhere(track => track.id == element.id)

    if (index == -1) {
      false
    } else {
      Tracks.all.remove(index)
      Tracks.all.addOne(element)
      true
    }
  }

  override def deleteById(id: UUID): Boolean = {
    val index = Tracks.all.indexWhere(track => track.id == id)

    if (index == -1) {
      false
    } else {
      Tracks.all.remove(index)
      true
    }
  }
}

package ru.otus.sc.dao.impl

import java.util.UUID

import ru.otus.sc.dao.TrackDao
import ru.otus.sc.model.entity.{Album, Track}

class TrackDaoImpl extends TrackDao {
  override def getByAlbum(album: Album): List[Track] = Tracks.all.filter(track => track.album == album.id)

  override def getByName(name: String): Option[Track] = Tracks.all.find(track => track.name == name)

  override def getById(id: UUID): Option[Track] = Tracks.all.find(track => track.id == id)

  override def getAll: List[Track] = Tracks.all
}

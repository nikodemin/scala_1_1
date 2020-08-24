package ru.otus.sc.service.impl

import java.util.UUID

import ru.otus.sc.dao.{AlbumDao, BandDao, TrackDao}
import ru.otus.sc.model.entity.{Album, Band, Track}
import ru.otus.sc.service.MusicService

class MusicServiceImpl(albumDao: AlbumDao, bandDao: BandDao, trackDao: TrackDao) extends MusicService {
  override def getAllAlbums: List[Album] = albumDao.getAll

  override def getTracksByBandName(name: String): List[Track] = albumDao.getByBand(bandDao.getByName(name).orNull)
    .flatMap(album => trackDao.getByAlbum(album))

  override def getTracksBySingerName(name: String): List[Track] = bandDao.getBySinger(name)
    .flatMap(band => albumDao.getByBand(band)).flatMap(album => trackDao.getByAlbum(album))

  override def addBand(band: Band): Boolean = bandDao.add(band)

  override def addAlbum(bandId: UUID, album: Album): Boolean = {
    val band = bandDao.getById(bandId).orNull

    if (band == null) {
      false
    } else {
      bandDao.update(band.copy(albums = band.albums.prepended(album.id)))
      albumDao.add(album.copy(band = bandId))
    }
  }

  override def addTrack(albumId: UUID, track: Track): Boolean = {
    val album = albumDao.getById(albumId).orNull

    if (album == null) {
      false
    } else {
      albumDao.update(album.copy(tracks = album.tracks.prepended(track.id)))
      trackDao.add(track.copy(album = albumId))
    }
  }

  override def updateBand(band: Band): Boolean = bandDao.update(band)

  override def updateAlbum(album: Album): Boolean = albumDao.update(album)

  override def updateTrack(track: Track): Boolean = trackDao.update(track)

  override def deleteBand(id: UUID): Boolean = bandDao.deleteById(id)

  override def deleteAlbum(id: UUID): Boolean = albumDao.deleteById(id)

  override def deleteTrack(id: UUID): Boolean = trackDao.deleteById(id)

  override def getBandByName(name: String): Option[Band] = bandDao.getByName(name)

  override def getAlbumByName(name: String): Option[Album] = albumDao.getByName(name)

  override def getTracksByAlbumName(name: String): List[Track] = albumDao.getByName(name)
    .map(trackDao.getByAlbum).getOrElse(List.empty)
}

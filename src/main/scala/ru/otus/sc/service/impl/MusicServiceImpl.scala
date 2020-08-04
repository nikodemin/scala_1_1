package ru.otus.sc.service.impl

import ru.otus.sc.dao.{AlbumDao, BandDao, TrackDao}
import ru.otus.sc.model.entity.{Album, Track}
import ru.otus.sc.service.MusicService

class MusicServiceImpl(albumDao: AlbumDao, bandDao: BandDao, trackDao: TrackDao) extends MusicService {
  override def getAllAlbums: List[Album] = albumDao.getAll

  override def getTracksByBandName(name: String): List[Track] = albumDao.getByBand(bandDao.getByName(name).orNull)
    .flatMap(album => trackDao.getByAlbum(album))

  override def getTracksBySingerName(name: String): List[Track] = bandDao.getBySinger(name)
    .flatMap(band => albumDao.getByBand(band)).flatMap(album => trackDao.getByAlbum(album))
}

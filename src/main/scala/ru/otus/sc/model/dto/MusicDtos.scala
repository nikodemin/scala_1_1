package ru.otus.sc.model.dto

import java.time.LocalDate
import java.util.UUID

import ru.otus.sc.model.entity.MusicEntities.{AlbumRow, BandRow, TrackRow}

// Band

case class BandAddDto(name: String, singer: String, established: LocalDate) {
  def toBandRow: BandRow = BandRow(None, name, singer, established)
}

case class BandGetDto(id: UUID, name: String, singer: String, established: LocalDate, albums: List[AlbumGetDto])

object BandGetDto {
  def fromTupleList(tuples: List[(BandRow, AlbumRow)]): List[BandGetDto] = tuples.groupBy(_._1).map {
    case (band, tuples) => BandGetDto(band.id.get, band.name, band.singer, band.established,
      tuples.filter(_._1.id == band.id).map(_._2).map(AlbumGetDto.fromAlbumRow(_, List.empty)))
  }.toList
}

case class BandUpdateDto(id: UUID, name: Option[String], singer: Option[String], established: Option[LocalDate])

// Album

case class AlbumAddDto(name: String, year: LocalDate, band: UUID) {
  def toAlbumRow: AlbumRow = AlbumRow(None, name, year, band)
}

case class AlbumGetDto(id: UUID, name: String, year: LocalDate, tracks: List[TrackGetDto], band: UUID)

object AlbumGetDto {
  def fromTupleList(tuples: List[(AlbumRow, TrackRow)]): List[AlbumGetDto] = tuples.groupBy(_._1).map {
    case (album, tuples) => fromAlbumRow(album, tuples.filter(_._1.id == album.id).map(_._2))
  }.toList

  def fromAlbumRow(albumRow: AlbumRow, tracks: List[TrackRow]): AlbumGetDto = AlbumGetDto(albumRow.id.get, albumRow.name,
    albumRow.year, tracks.map(TrackGetDto.fromTrackRow), albumRow.bandId)
}

case class AlbumUpdateDto(id: UUID, name: Option[String], year: Option[LocalDate], band: Option[UUID])

// Track

case class TrackAddDto(name: String, duration: Float, album: UUID) {
  def toTrackRow: TrackRow = TrackRow(None, name, duration, album)
}

case class TrackGetDto(id: UUID, name: String, duration: Float, album: UUID)

object TrackGetDto {
  def fromTrackRow(trackRow: TrackRow): TrackGetDto = TrackGetDto(trackRow.id.get, trackRow.name, trackRow.duration,
    trackRow.albumId)
}

case class TrackUpdateDto(id: UUID, name: Option[String], duration: Option[Float], album: Option[UUID])

// Search

case class SearchDto(string: String, bands: List[BandGetDto], albums: List[AlbumGetDto], tracks: List[TrackGetDto])
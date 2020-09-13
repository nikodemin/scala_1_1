package ru.otus.sc.model.entity

import java.time.LocalDate
import java.util.UUID

import slick.jdbc.PostgresProfile.api._
import slick.lifted.Tag

object MusicEntities {

  // Band

  case class BandRow(id: Option[UUID] = None, name: String, singer: String, established: LocalDate)

  class Band(tag: Tag) extends Table[BandRow](tag, "band") {
    val id = column[UUID]("id", O.PrimaryKey, O.AutoInc)
    val name = column[String]("name", O.Unique)
    val singer = column[String]("singer")
    val established = column[LocalDate]("established")

    override def * = (id.?, name, singer, established).mapTo[BandRow]
  }

  val bands = TableQuery[Band]

  // Album

  case class AlbumRow(id: Option[UUID] = None, name: String, year: LocalDate, bandId: UUID)

  class Album(tag: Tag) extends Table[AlbumRow](tag, "album") {
    val id = column[UUID]("id", O.PrimaryKey, O.AutoInc)
    val name = column[String]("name")
    val year = column[LocalDate]("year")
    val bandId = column[UUID]("band_id")

    val fk = foreignKey("band_FK", bandId, bands)(_.id, onDelete = ForeignKeyAction.Cascade)

    override def * = (id.?, name, year, bandId).mapTo[AlbumRow]
  }

  val albums = TableQuery[Album]

  // Track

  case class TrackRow(id: Option[UUID] = None, name: String, duration: Float, albumId: UUID)

  class Track(tag: Tag) extends Table[TrackRow](tag, "track") {
    val id = column[UUID]("id", O.PrimaryKey, O.AutoInc)
    val name = column[String]("name")
    val duration = column[Float]("duration")
    val albumId = column[UUID]("album_id")

    val fk = foreignKey("album_FK", albumId, albums)(_.id, onDelete = ForeignKeyAction.Cascade)

    override def * = (id.?, name, duration, albumId).mapTo[TrackRow]
  }

  val tracks = TableQuery[Track]
}

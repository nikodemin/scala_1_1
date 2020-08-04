package ru.otus.sc.model.entity

import java.time.LocalDate
import java.util.UUID

case class Band(var name: String, var singer: String, var established: LocalDate, var albums: List[UUID],
                id: UUID = UUID.randomUUID(), version: Long = 1L)

case class Album(var name: String, var year: LocalDate, var tracks: List[UUID], var band: UUID,
                 id: UUID = UUID.randomUUID(), version: Long = 1L)

case class Track(var name: String, var duration: Float, var album: UUID,
                 id: UUID = UUID.randomUUID(), version: Long = 1L)

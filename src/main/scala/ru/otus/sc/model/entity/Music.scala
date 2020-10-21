package ru.otus.sc.model.entity

import java.time.LocalDate
import java.util.UUID

case class Band(name: String, singer: String, established: LocalDate, albums: List[UUID],
                id: UUID = UUID.randomUUID(), version: Long = 1L)

case class Album(name: String, year: LocalDate, tracks: List[UUID], band: UUID,
                 id: UUID = UUID.randomUUID(), version: Long = 1L)

case class Track(name: String, duration: Float, album: UUID,
                 id: UUID = UUID.randomUUID(), version: Long = 1L)

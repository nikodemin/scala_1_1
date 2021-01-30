package ru.otus.sc.route.util

import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}
import ru.otus.sc.model.dto._
import zio.{IO, Task, ZIO}

object Implicits {
  implicit val trackGetDtoDecoder: Decoder[TrackGetDto] = deriveDecoder[TrackGetDto]
  implicit val trackAddDtoDecoder: Decoder[TrackAddDto] = deriveDecoder[TrackAddDto]
  implicit val trackUpdateDtoDecoder: Decoder[TrackUpdateDto] = deriveDecoder[TrackUpdateDto]
  implicit val albumGetDecoder: Decoder[AlbumGetDto] = deriveDecoder[AlbumGetDto]
  implicit val albumAddDecoder: Decoder[AlbumAddDto] = deriveDecoder[AlbumAddDto]
  implicit val albumUpdateDecoder: Decoder[AlbumUpdateDto] = deriveDecoder[AlbumUpdateDto]
  implicit val bandGetDtoDecoder: Decoder[BandGetDto] = deriveDecoder[BandGetDto]
  implicit val bandAddDtoDecoder: Decoder[BandAddDto] = deriveDecoder[BandAddDto]
  implicit val bandUpdateDtoDecoder: Decoder[BandUpdateDto] = deriveDecoder[BandUpdateDto]
  implicit val searchDtoDecoder: Decoder[SearchDto] = deriveDecoder[SearchDto]

  implicit val trackGetDtoEncoder: Encoder[TrackGetDto] = deriveEncoder[TrackGetDto]
  implicit val trackAddDtoEncoder: Encoder[TrackAddDto] = deriveEncoder[TrackAddDto]
  implicit val trackUpdateDtoEncoder: Encoder[TrackUpdateDto] = deriveEncoder[TrackUpdateDto]
  implicit val albumGetEncoder: Encoder[AlbumGetDto] = deriveEncoder[AlbumGetDto]
  implicit val albumAddEncoder: Encoder[AlbumAddDto] = deriveEncoder[AlbumAddDto]
  implicit val albumUpdateEncoder: Encoder[AlbumUpdateDto] = deriveEncoder[AlbumUpdateDto]
  implicit val bandGetDtoEncoder: Encoder[BandGetDto] = deriveEncoder[BandGetDto]
  implicit val bandAddDtoEncoder: Encoder[BandAddDto] = deriveEncoder[BandAddDto]
  implicit val bandUpdateDtoEncoder: Encoder[BandUpdateDto] = deriveEncoder[BandUpdateDto]
  implicit val searchDtoEncoder: Encoder[SearchDto] = deriveEncoder[SearchDto]

  implicit val NotFoundEncoder: Encoder[NotFound] = deriveEncoder[NotFound]
  implicit val NotFoundDecoder: Decoder[NotFound] = deriveDecoder[NotFound]

  implicit val InternalServerErrorEncoder: Encoder[InternalServerError] = deriveEncoder[InternalServerError]
  implicit val InternalServerErrorDecoder: Decoder[InternalServerError] = deriveDecoder[InternalServerError]

  implicit def taskOptionToZIO[T](task: Task[Option[T]]): IO[HttpException, T] = task.catchAllCause(cause =>
    ZIO.fail(InternalServerError(cause.prettyPrint))).someOrFail(NotFound())

  implicit def taskListToZIO[T](task: Task[T]): IO[HttpException, T] = task.orElseFail(InternalServerError())
}

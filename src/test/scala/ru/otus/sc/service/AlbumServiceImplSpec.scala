package ru.otus.sc.service

import ru.otus.sc.dao.AlbumDao
import ru.otus.sc.model.dto.{AlbumAddDto, AlbumGetDto, AlbumUpdateDto}
import ru.otus.sc.model.entity.MusicEntities.{AlbumRow, TrackRow}
import zio.ZIO
import zio.test.Assertion._
import zio.test.environment.TestEnvironment
import zio.test.magnolia.DeriveGen
import zio.test.mock.Expectation._
import zio.test.mock.mockable
import zio.test.{testM, _}

object AlbumServiceImplSpec extends DefaultRunnableSpec {

  @mockable[AlbumDao.Service]
  object AlbumDaoMock

  private val albumAddDtoGen = DeriveGen[AlbumAddDto]
  private val albumUpdateDtoGen = DeriveGen[AlbumUpdateDto]
  private val albumRowTrackRowListGen = DeriveGen[List[(AlbumRow, TrackRow)]]

  private val albumService = ZIO.service[AlbumService.Service]

  override def spec: ZSpec[TestEnvironment, Any] = suite("AlbumServiceImplSpec")(
    testM("get by band id") {
      checkM(Gen.anyUUID, albumRowTrackRowListGen) { (bandId, list) =>
        val env = AlbumDaoMock.GetByBand(equalTo(bandId), value(list)) >>> AlbumService.live
        val action = albumService.flatMap(_.getByBand(bandId))

        assertM(action.provideLayer(env)) {
          equalTo(AlbumGetDto.fromTupleList(list))
        }
      }
    },
    testM("get all") {
      checkM(albumRowTrackRowListGen) { list =>
        val env = AlbumDaoMock.GetAll(value(list)) >>> AlbumService.live
        val action = albumService.flatMap(_.getAll)

        assertM(action.provideLayer(env)) {
          equalTo(AlbumGetDto.fromTupleList(list))
        }
      }
    },
    testM("get by name") {
      checkM(Gen.anyString, albumRowTrackRowListGen) { (name, list) =>
        val env = AlbumDaoMock.GetByName(equalTo(name), value(list)) >>> AlbumService.live
        val action = albumService.flatMap(_.getByName(name))

        assertM(action.provideLayer(env)) {
          equalTo(AlbumGetDto.fromTupleList(list))
        }
      }
    },
    testM("get by id") {
      checkM(Gen.anyUUID, albumRowTrackRowListGen) { (id, list) =>
        val env = AlbumDaoMock.GetById(equalTo((id, false)), value(list)) >>> AlbumService.live
        val action = albumService.flatMap(_.getById(id))

        assertM(action.provideLayer(env)) {
          equalTo(AlbumGetDto.fromTupleList(list).headOption)
        }
      }
    },
    testM("add") {
      checkM(albumAddDtoGen) { album =>
        val env = AlbumDaoMock.Add(equalTo(album.toAlbumRow), value(true)) >>> AlbumService.live
        val negativeCaseEnv = AlbumDaoMock.Add(anything, value(false)) >>> AlbumService.live
        val action = albumService.flatMap(_.add(album))

        assertM(action.provideLayer(env)) {
          isTrue
        } *> assertM(action.provideLayer(negativeCaseEnv)) {
          isFalse
        }
      }
    },
    testM("update") {
      checkM(albumUpdateDtoGen, albumRowTrackRowListGen.filter(_.nonEmpty)) { (albumUpdateDto, list) =>
        val env = AlbumDaoMock.GetById(equalTo((albumUpdateDto.id, true)), value(list)) ++
          AlbumDaoMock.Update(anything, value(true)) >>> AlbumService.live
        val negativeCaseEnv = AlbumDaoMock.GetById(equalTo((albumUpdateDto.id, true)), value(List())) >>>
          AlbumService.live
        val negativeCaseEnv2 = AlbumDaoMock.GetById(equalTo((albumUpdateDto.id, true)), value(list)) ++
          AlbumDaoMock.Update(anything, value(false)) >>> AlbumService.live
        val action = albumService.flatMap(_.update(albumUpdateDto))

        assertM(action.provideLayer(env)) {
          isTrue
        } *> assertM(action.provideLayer(negativeCaseEnv)) {
          isFalse
        } *> assertM(action.provideLayer(negativeCaseEnv2)) {
          isFalse
        }
      }
    },
    testM("delete") {
      checkM(Gen.anyUUID) { id =>
        val env = AlbumDaoMock.DeleteById(equalTo(id), value(true)) >>> AlbumService.live
        val negativeCaseEnv = AlbumDaoMock.DeleteById(equalTo(id), value(false)) >>> AlbumService.live
        val action = albumService.flatMap(_.delete(id))

        assertM(action.provideLayer(env)) {
          isTrue
        } *> assertM(action.provideLayer(negativeCaseEnv)) {
          isFalse
        }
      }
    }
  )
}

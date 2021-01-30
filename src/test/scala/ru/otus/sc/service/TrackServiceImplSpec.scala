package ru.otus.sc.service

import ru.otus.sc.dao.TrackDao
import ru.otus.sc.model.dto.{TrackAddDto, TrackGetDto, TrackUpdateDto}
import ru.otus.sc.model.entity.MusicEntities.TrackRow
import zio.ZIO
import zio.test.Assertion._
import zio.test.environment.TestEnvironment
import zio.test.magnolia.DeriveGen
import zio.test.mock.Expectation._
import zio.test.mock.mockable
import zio.test.{DefaultRunnableSpec, ZSpec, assertM, testM, _}

object TrackServiceImplSpec extends DefaultRunnableSpec {

  @mockable[TrackDao.Service]
  object TrackDaoMock

  private val trackService = ZIO.service[TrackService.Service]

  private val trackRowListGen = DeriveGen[List[TrackRow]]
  private val trackUpdateDtoGen = DeriveGen[TrackUpdateDto]
  private val trackAddDtoGen = DeriveGen[TrackAddDto]

  override def spec: ZSpec[TestEnvironment, Any] = suite("TrackServiceImplSpec ")(
    testM("get all") {
      checkM(trackRowListGen) { list =>
        val env = TrackDaoMock.GetAll(value(list)) >>> TrackService.live
        val action = trackService.flatMap(_.getAll)

        assertM(action.provideLayer(env)) {
          equalTo(list.map(TrackGetDto.fromTrackRow))
        }
      }
    },
    testM("get by album") {
      checkM(Gen.anyUUID, trackRowListGen) { (albumId, list) =>
        val env = TrackDaoMock.GetByAlbum(equalTo(albumId), value(list)) >>> TrackService.live
        val action = trackService.flatMap(_.getByAlbum(albumId))

        assertM(action.provideLayer(env)) {
          equalTo(list.map(TrackGetDto.fromTrackRow))
        }
      }
    },
    testM("get by name") {
      checkM(Gen.anyString, trackRowListGen) { (name, list) =>
        val env = TrackDaoMock.GetByName(equalTo(name), value(list)) >>> TrackService.live
        val action = trackService.flatMap(_.getByName(name))

        assertM(action.provideLayer(env)) {
          equalTo(list.map(TrackGetDto.fromTrackRow))
        }
      }
    },
    testM("get by id") {
      checkM(Gen.anyUUID, trackRowListGen) { (id, list) =>
        val env = TrackDaoMock.GetById(equalTo((id, false)), value(list)) >>> TrackService.live
        val action = trackService.flatMap(_.getById(id))

        assertM(action.provideLayer(env)) {
          equalTo(list.headOption.map(TrackGetDto.fromTrackRow))
        }
      }
    },
    testM("add") {
      checkM(trackAddDtoGen) { trackAddDto =>
        val env = TrackDaoMock.Add(equalTo(trackAddDto.toTrackRow), value(true)) >>> TrackService.live
        val negativeCaseEnv = TrackDaoMock.Add(equalTo(trackAddDto.toTrackRow), value(false)) >>>
          TrackService.live
        val action = trackService.flatMap(_.add(trackAddDto))

        assertM(action.provideLayer(env)) {
          isTrue
        } *> assertM(action.provideLayer(negativeCaseEnv)) {
          isFalse
        }
      }
    },
    testM("update") {
      checkM(trackUpdateDtoGen, trackRowListGen.filter(_.nonEmpty)) { (trackUpdateDto, list) =>
        val env = TrackDaoMock.GetById(equalTo((trackUpdateDto.id, true)), value(list)) ++
          TrackDaoMock.Update(anything, value(true)) >>> TrackService.live
        val negativeCaseEnv = TrackDaoMock.GetById(equalTo((trackUpdateDto.id, true)), value(List())) >>>
          TrackService.live
        val negativeCaseEnv2 = TrackDaoMock.GetById(equalTo((trackUpdateDto.id, true)), value(list)) ++
          TrackDaoMock.Update(anything, value(false)) >>> TrackService.live
        val action = trackService.flatMap(_.update(trackUpdateDto))

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
        val env = TrackDaoMock.DeleteById(equalTo(id), value(true)) >>> TrackService.live
        val negativeCaseEnv = TrackDaoMock.DeleteById(equalTo(id), value(false)) >>> TrackService.live
        val action = trackService.flatMap(_.delete(id))

        assertM(action.provideLayer(env)) {
          isTrue
        } *> assertM(action.provideLayer(negativeCaseEnv)) {
          isFalse
        }
      }
    },
    testM("get by album name") {
      checkM(Gen.anyString, trackRowListGen) { (albumName, list) =>
        val env = TrackDaoMock.GetByAlbumName(equalTo(albumName), value(list)) >>> TrackService.live
        val action = trackService.flatMap(_.getByAlbumName(albumName))

        assertM(action.provideLayer(env)) {
          equalTo(list.map(TrackGetDto.fromTrackRow))
        }
      }
    }
  )
}

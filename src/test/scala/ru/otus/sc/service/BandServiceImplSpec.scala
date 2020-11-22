package ru.otus.sc.service

import ru.otus.sc.dao.BandDao
import ru.otus.sc.model.dto.{BandAddDto, BandGetDto, BandUpdateDto}
import ru.otus.sc.model.entity.MusicEntities.{AlbumRow, BandRow}
import zio.ZIO
import zio.test.Assertion.{equalTo, _}
import zio.test.environment.TestEnvironment
import zio.test.magnolia.DeriveGen
import zio.test.mock.Expectation._
import zio.test.mock.mockable
import zio.test.{DefaultRunnableSpec, ZSpec, assertM, testM, _}

object BandServiceImplSpec extends DefaultRunnableSpec {

  @mockable[BandDao.Service]
  object BandDaoMock

  private val bandRowAlbumRowListGen = DeriveGen[List[(BandRow, AlbumRow)]]
  private val bandAddDtoGen = DeriveGen[BandAddDto]
  private val bandUpdateDtoGen = DeriveGen[BandUpdateDto]

  private val bandService = ZIO.service[BandService.Service]

  override def spec: ZSpec[TestEnvironment, Any] = suite("BandServiceImplSpec")(
    testM("get by id") {
      checkM(Gen.anyUUID, bandRowAlbumRowListGen) { (id, list) =>
        val env = BandDaoMock.GetById(equalTo((id, false)), value(list)) >>> BandService.live
        val action = bandService.flatMap(_.getById(id))

        assertM(action.provideLayer(env)) {
          equalTo(BandGetDto.fromTupleList(list).headOption)
        }
      }
    },
    testM("get all") {
      checkM(bandRowAlbumRowListGen) { list =>
        val env = BandDaoMock.GetAll(value(list)) >>> BandService.live
        val action = bandService.flatMap(_.getAll)

        assertM(action.provideLayer(env)) {
          equalTo(BandGetDto.fromTupleList(list))
        }
      }
    },
    testM("get by name") {
      checkM(Gen.anyString, bandRowAlbumRowListGen) { (name, list) =>
        val env = BandDaoMock.GetByName(equalTo(name), value(list)) >>> BandService.live
        val action = bandService.flatMap(_.getByName(name))

        assertM(action.provideLayer(env)) {
          equalTo(BandGetDto.fromTupleList(list).headOption)
        }
      }
    },
    testM("get by singer") {
      checkM(Gen.anyString, bandRowAlbumRowListGen) { (singerName, list) =>
        val env = BandDaoMock.GetBySinger(equalTo(singerName), value(list)) >>> BandService.live
        val action = bandService.flatMap(_.getBySinger(singerName))

        assertM(action.provideLayer(env)) {
          equalTo(BandGetDto.fromTupleList(list))
        }
      }
    },
    testM("add") {
      checkM(bandAddDtoGen) { bandAddDto =>
        val env = BandDaoMock.Add(equalTo(bandAddDto.toBandRow), value(true)) >>> BandService.live
        val negativeCaseEnv = BandDaoMock.Add(equalTo(bandAddDto.toBandRow), value(false)) >>> BandService.live
        val action = bandService.flatMap(_.add(bandAddDto))

        assertM(action.provideLayer(env)) {
          isTrue
        } *> assertM(action.provideLayer(negativeCaseEnv)) {
          isFalse
        }
      }
    },
    testM("update") {
      checkM(bandUpdateDtoGen, bandRowAlbumRowListGen.filter(_.nonEmpty)) { (bandUpdateDto, list) =>
        val env = BandDaoMock.GetById(equalTo((bandUpdateDto.id, true)), value(list)) ++
          BandDaoMock.Update(anything, value(true)) >>> BandService.live
        val negativeCaseEnv = BandDaoMock.GetById(equalTo((bandUpdateDto.id, true)), value(list)) ++
          BandDaoMock.Update(anything, value(false)) >>> BandService.live
        val negativeCaseEnv2 = BandDaoMock.GetById(equalTo((bandUpdateDto.id, true)), value(List())) >>>
          BandService.live
        val action = bandService.flatMap(_.update(bandUpdateDto))

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
        val env = BandDaoMock.DeleteById(equalTo(id), value(true)) >>> BandService.live
        val negativeCaseEnv = BandDaoMock.DeleteById(equalTo(id), value(false)) >>> BandService.live
        val action = bandService.flatMap(_.delete(id))

        assertM(action.provideLayer(env)) {
          isTrue
        } *> assertM(action.provideLayer(negativeCaseEnv)) {
          isFalse
        }
      }
    }
  )
}

package ru.otus.sc.dao.impl

import ru.otus.sc.dao.StorageValueDao
import ru.otus.sc.model.entity.StorageValue
import ru.otus.sc.model.enums.Key
import ru.otus.sc.model.enums.Key.Key

import scala.collection.mutable
import scala.util.Random

class StorageValueDaoImpl extends StorageValueDao {

  private val stringValues = List("dlfsm", "sdkfjsd", "dsfk", "sofjd", "afeij")
  private val intValues = List(23, 21, 90, 14, 64, 47)
  private val valuesCount = Key.values.size

  private lazy val strings: List[String] = LazyList.iterate(Random.between(0, stringValues.length))(_ => Random.between(0, stringValues.length))
    .map(stringValues(_))
    .take(valuesCount)
    .toList
  private lazy val ints: List[Int] = LazyList.iterate(Random.between(0, intValues.length))(_ => Random.between(0, intValues.length))
    .map(intValues(_))
    .take(valuesCount)
    .toList
  private lazy val keyValueMap: mutable.Map[Key, StorageValue] = mutable.Map.from(LazyList.from(0)
    .take(valuesCount)
    .map(i => (Key(i), StorageValue(strings(i), ints(i)))))

  override def getByKey(key: Key): StorageValue = keyValueMap(key)

  override def setKey(key: Key, value: StorageValue): Unit = keyValueMap += (key -> value)

  override def getAll: List[StorageValue] = keyValueMap.values.toList
}

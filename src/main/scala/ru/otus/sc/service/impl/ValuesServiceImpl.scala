package ru.otus.sc.service.impl

import ru.otus.sc.dao.StorageValueDao
import ru.otus.sc.model.entity.StorageValue
import ru.otus.sc.model.enums.Key.Key
import ru.otus.sc.service.ValuesService

class ValuesServiceImpl(valueDao: StorageValueDao) extends ValuesService {
  override def getByKey(key: Key): StorageValue = valueDao.getByKey(key)

  override def setKey(key: Key, value: StorageValue): Unit = valueDao.setKey(key, value)

  override def getAllValues: List[StorageValue] = valueDao.getAll
}

package ru.otus.sc.service

import ru.otus.sc.model.entity.StorageValue
import ru.otus.sc.model.enums.Key.Key

/**
 * Service for operations with StorageValue
 */
trait ValuesService {
  /**
   * Get StorageValue by key
   *
   * @param key Key
   * @return StorageValue
   */
  def getByKey(key: Key): StorageValue

  /**
   * Set key value
   *
   * @param key   Key
   * @param value StorageValue
   */
  def setKey(key: Key, value: StorageValue)

  /**
   * Get all StorageValue entities
   *
   * @return List[Key]
   */
  def getAllValues: List[StorageValue]
}

package ru.otus.sc.dao

import ru.otus.sc.model.entity.StorageValue
import ru.otus.sc.model.enums.Key.Key

/**
 * DAO for key-value storage
 */
trait StorageValueDao {
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
   * @return List[StorageValue]
   */
  def getAll: List[StorageValue]
}

package ru.otus.sc.dao

import java.util.UUID

/**
 * Abstract dao with common operations
 *
 * @tparam T - type of DAO layer
 */
abstract class AbstractDao[T] {
  def getByName(name: String): Option[T]

  def getById(id: UUID): Option[T]

  def getAll: List[T]

  /**
   * Add element
   *
   * @param element element to be added
   * @return true if collection was modified
   */
  def add(element: T): Boolean

  /**
   * Update element. You must set id of updated element
   *
   * @param element element with the id set
   * @return true if element was modified
   */
  def update(element: T): Boolean

  /**
   * Delete element
   *
   * @param id id of element to delete
   * @return true if collection was modified
   */
  def deleteById(id: UUID): Boolean
}

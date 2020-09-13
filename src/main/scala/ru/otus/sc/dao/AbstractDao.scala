package ru.otus.sc.dao

import java.util.UUID

import scala.concurrent.Future

/**
 * Abstract dao with common operations
 *
 * @tparam IN  - type of input to DAO layer
 * @tparam OUT - type of output from DAO layer
 */
abstract class AbstractDao[IN, OUT] {
  def getByName(name: String): Future[List[OUT]]

  /**
   * Returns list of joined entities
   *
   * @param id        id of entity
   * @param forUpdate if entity will be updated
   * @return
   */
  def getById(id: UUID, forUpdate: Boolean): Future[List[OUT]]

  def getAll: Future[List[OUT]]

  /**
   * Add element
   *
   * @param element element to be added
   * @return true if collection was modified
   */
  def add(element: IN): Future[Boolean]

  /**
   * Update element. You must set id of updated element
   *
   * @param element element with the id set
   * @return true if element was modified
   */
  def update(element: IN): Future[Boolean]

  /**
   * Delete element
   *
   * @param id id of element to delete
   * @return true if collection was modified
   */
  def deleteById(id: UUID): Future[Boolean]
}

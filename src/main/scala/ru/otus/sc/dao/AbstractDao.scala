package ru.otus.sc.dao

import java.util.UUID

/**
 * Abstract dao with common operations
 *
 * @tparam T - type of DAO layer
 */
trait AbstractDao[T] {
  def getByName(name: String): Option[T]

  def getById(id: UUID): Option[T]

  def getAll: List[T]
}

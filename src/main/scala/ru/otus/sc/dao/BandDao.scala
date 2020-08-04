package ru.otus.sc.dao

import ru.otus.sc.model.entity.Band

/**
 * Band DAO
 */
trait BandDao extends AbstractDao[Band] {
  def getBySinger(name: String): List[Band]
}

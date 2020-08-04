package ru.otus.sc.dao

import ru.otus.sc.model.entity.{Album, Band}

/**
 * Album DAO
 */
trait AlbumDao extends AbstractDao[Album] {
  def getByBand(band: Band): List[Album]
}

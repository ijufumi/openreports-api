package jp.ijufumi.openreports.domain.repository

import jp.ijufumi.openreports.domain.models.entity.DriverType
import slick.jdbc.JdbcBackend.Database

trait DriverTypeRepository {
  def getAll(db: Database): Seq[DriverType]
}

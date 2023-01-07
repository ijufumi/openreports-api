package jp.ijufumi.openreports.vo.response

import jp.ijufumi.openreports.entities.{DataSource => DataSourceEntity}
case class DataSource(
    id: String,
    name: String,
    url: String,
    username: String,
    password: String,
    driverTypeId: String,
)

object DataSource {
  def apply(entity: DataSourceEntity): DataSource = {
    DataSource(
      entity.id,
      entity.name,
      entity.url,
      entity.username,
      entity.password,
      entity.driverTypeId,
    )
  }
}

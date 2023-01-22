package jp.ijufumi.openreports.models.outputs

import jp.ijufumi.openreports.entities.{DataSource => DataSourceEntity, DriverType => DriverTypeEntity}
case class DataSource(
    id: String,
    name: String,
    url: String,
    username: String,
    password: String,
    driverTypeId: String,
    driverTypeName: String,
)

object DataSource {
  def apply(entity: DataSourceEntity, driverType: DriverTypeEntity): DataSource = {
    DataSource(
      entity.id,
      entity.name,
      entity.url,
      entity.username,
      entity.password,
      entity.driverTypeId,
      driverType.name,
    )
  }
}

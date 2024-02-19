package jp.ijufumi.openreports.presentation.models.responses

import jp.ijufumi.openreports.domain.models.entity.{
  DataSource => DataSourceEntity,
  DriverType => DriverTypeEntity,
}

case class DataSource(
    id: String,
    name: String,
    url: String,
    username: String,
    password: String,
    driverTypeId: String,
    driverType: Option[DriverType] = None,
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
      entity.driverType.map(d => DriverType(d)),
    )
  }
}

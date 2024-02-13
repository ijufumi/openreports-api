package jp.ijufumi.openreports.models.outputs

import jp.ijufumi.openreports.gateways.datastores.database.entities.{
  DataSource => DataSourceEntity,
  DriverType => DriverTypeEntity,
}
import jp.ijufumi.openreports.models.inputs.UpdateDataSource
import jp.ijufumi.openreports.utils.Dates
case class DataSource(
    id: String,
    name: String,
    url: String,
    username: String,
    password: String,
    driverTypeId: String,
    workspaceId: String,
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
    driverType: Option[DriverType] = None,
) {
  def toEntity: DataSourceEntity = {
    DataSourceEntity(
      this.id,
      this.name,
      this.url,
      this.username,
      this.password,
      this.driverTypeId,
      this.workspaceId,
      this.createdAt,
      this.updatedAt,
      this.versions,
    )
  }
  def copyForUpdate(input: UpdateDataSource): DataSource = {
    this.copy(name = input.name)
  }
}

object DataSource {
  def apply(entity: DataSourceEntity): DataSource = {
    DataSource(
      entity.id,
      entity.name,
      entity.url,
      entity.username,
      entity.password,
      entity.driverTypeId,
      entity.workspaceId,
      entity.createdAt,
      entity.updatedAt,
      entity.versions,
    )
  }
  def apply(entity: DataSourceEntity, driverType: DriverTypeEntity): DataSource = {
    DataSource(
      entity.id,
      entity.name,
      entity.url,
      entity.username,
      entity.password,
      entity.driverTypeId,
      entity.workspaceId,
      entity.createdAt,
      entity.updatedAt,
      entity.versions,
      Some(DriverType(driverType)),
    )
  }
}

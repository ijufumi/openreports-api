package jp.ijufumi.openreports.domain.models.entity

import jp.ijufumi.openreports.infrastructure.datastores.database.entities.{
  DataSource => DataSourceEntity,
  DriverType => DriverTypeEntity,
}
import jp.ijufumi.openreports.presentation.models.requests.UpdateDataSource
import jp.ijufumi.openreports.utils.Dates
import jp.ijufumi.openreports.presentation.models.responses.{DataSource => DataSourceResponse}
import jp.ijufumi.openreports.domain.models.entity.DriverType.conversions._

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
  def toResponse: DataSourceResponse = {
    DataSourceResponse(
      this.id,
      this.name,
      this.url,
      this.username,
      this.password,
      this.driverTypeId,
      this.driverType,
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
  def apply(entity: (DataSourceEntity, DriverTypeEntity)): DataSource = {
    DataSource(
      entity._1.id,
      entity._1.name,
      entity._1.url,
      entity._1.username,
      entity._1.password,
      entity._1.driverTypeId,
      entity._1.workspaceId,
      entity._1.createdAt,
      entity._1.updatedAt,
      entity._1.versions,
      Some(DriverType(entity._2)),
    )
  }

  object conversions {
    import scala.language.implicitConversions

    implicit def fromDataSourceEntity(model: DataSourceEntity): DataSource = {
      DataSource(model)
    }
    implicit def fromDataSourceEntity2(model: (DataSourceEntity, DriverTypeEntity)): DataSource = {
      DataSource(model)
    }
    implicit def fromDataSourceEntity(model: Option[DataSourceEntity]): Option[DataSource] = {
      model.map(d => DataSource(d))
    }
    implicit def fromDataSourceEntity2(
        model: Option[(DataSourceEntity, DriverTypeEntity)],
    ): Option[DataSource] = {
      model.map(d => DataSource(d))
    }
    implicit def fromDataSourceEntities(model: Seq[DataSourceEntity]): Seq[DataSource] = {
      model.map(d => DataSource(d))
    }
    implicit def fromDataSourceEntities2(
        model: Seq[(DataSourceEntity, DriverTypeEntity)],
    ): Seq[DataSource] = {
      model.map(d => DataSource(d))
    }
    implicit def toDataSourceEntity(model: DataSource): DataSourceEntity = {
      model.toEntity
    }
    implicit def toDataSourceResponse(model: DataSource): DataSourceResponse = {
      model.toResponse
    }
    implicit def toDataSourceResponse2(model: Option[DataSource]): Option[DataSourceResponse] = {
      model.map(m => m.toResponse)
    }
    implicit def toDataSourceResponses(model: Seq[DataSource]): Seq[DataSourceResponse] = {
      model.map(m => m.toResponse)
    }
  }
}

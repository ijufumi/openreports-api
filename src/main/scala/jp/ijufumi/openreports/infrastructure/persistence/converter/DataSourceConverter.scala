package jp.ijufumi.openreports.infrastructure.persistence.converter

import jp.ijufumi.openreports.domain.models.entity.{DataSource => DataSourceModel}
import jp.ijufumi.openreports.infrastructure.persistence.entity.{
  DataSource => DataSourceEntity,
  DriverType => DriverTypeEntity,
}
import jp.ijufumi.openreports.utils.Crypto

object DataSourceConverter {
  def toDomain(entity: DataSourceEntity): DataSourceModel = {
    DataSourceModel(
      entity.id,
      entity.name,
      entity.url,
      entity.username,
      Crypto.decrypt(entity.password),
      entity.driverTypeId,
      DataSourceModel.maxPoolSize,
      entity.workspaceId,
      entity.createdAt,
      entity.updatedAt,
      entity.versions,
    )
  }

  def toDomain(entity: (DataSourceEntity, DriverTypeEntity)): DataSourceModel = {
    DataSourceModel(
      entity._1.id,
      entity._1.name,
      entity._1.url,
      entity._1.username,
      Crypto.decrypt(entity._1.password),
      entity._1.driverTypeId,
      DataSourceModel.maxPoolSize,
      entity._1.workspaceId,
      entity._1.createdAt,
      entity._1.updatedAt,
      entity._1.versions,
      Some(DriverTypeConverter.toDomain(entity._2)),
    )
  }

  def toEntity(model: DataSourceModel): DataSourceEntity = {
    DataSourceEntity(
      model.id,
      model.name,
      model.url,
      model.username,
      Crypto.encrypt(model.password),
      model.driverTypeId,
      model.workspaceId,
      model.createdAt,
      model.updatedAt,
      model.versions,
    )
  }

  object conversions {
    import scala.language.implicitConversions

    implicit def fromDataSourceEntity(entity: DataSourceEntity): DataSourceModel = toDomain(entity)
    implicit def fromDataSourceEntity2(
        entity: (DataSourceEntity, DriverTypeEntity),
    ): DataSourceModel = toDomain(entity)
    implicit def fromDataSourceEntityOpt(
        entity: Option[DataSourceEntity],
    ): Option[DataSourceModel] = entity.map(toDomain)
    implicit def fromDataSourceEntity2Opt(
        entity: Option[(DataSourceEntity, DriverTypeEntity)],
    ): Option[DataSourceModel] = entity.map(toDomain)
    implicit def fromDataSourceEntities(entity: Seq[DataSourceEntity]): Seq[DataSourceModel] =
      entity.map(toDomain)
    implicit def fromDataSourceEntities2(
        entity: Seq[(DataSourceEntity, DriverTypeEntity)],
    ): Seq[DataSourceModel] = entity.map(toDomain)
    implicit def toDataSourceEntity(model: DataSourceModel): DataSourceEntity = toEntity(model)
  }
}

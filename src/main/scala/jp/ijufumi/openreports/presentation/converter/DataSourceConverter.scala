package jp.ijufumi.openreports.presentation.converter

import jp.ijufumi.openreports.domain.models.entity.{
  DataSource => DataSourceModel,
  DriverType => DriverTypeModel,
}
import jp.ijufumi.openreports.presentation.response.{
  DataSource => DataSourceResponse,
  DriverType => DriverTypeResponse,
}
import jp.ijufumi.openreports.presentation.request.{CreateDataSource, UpdateDataSource}
import jp.ijufumi.openreports.usecase.port.input.param.{
  CreateDataSourceInput,
  UpdateDataSourceInput,
}

object DataSourceConverter {
  def toResponse(model: DataSourceModel): DataSourceResponse = {
    DataSourceResponse(
      model.id,
      model.name,
      model.url,
      model.username,
      model.password,
      model.driverTypeId,
      model.driverType.map(DriverTypeConverter.toResponse),
      model.createdAt,
      model.updatedAt,
    )
  }

  def toCreateInput(request: CreateDataSource): CreateDataSourceInput = {
    CreateDataSourceInput(
      request.name,
      request.url,
      request.username,
      request.password,
      request.driverTypeId,
    )
  }

  def toUpdateInput(request: UpdateDataSource): UpdateDataSourceInput = {
    UpdateDataSourceInput(
      request.name,
      request.url,
      request.username,
      request.password,
      request.driverTypeId,
    )
  }

  object conversions {
    import scala.language.implicitConversions

    implicit def toDataSourceResponse(model: DataSourceModel): DataSourceResponse = toResponse(
      model,
    )
    implicit def toDataSourceResponse2(model: Option[DataSourceModel]): Option[DataSourceResponse] =
      model.map(toResponse)
    implicit def toDataSourceResponses(model: Seq[DataSourceModel]): Seq[DataSourceResponse] =
      model.map(toResponse)
  }
}

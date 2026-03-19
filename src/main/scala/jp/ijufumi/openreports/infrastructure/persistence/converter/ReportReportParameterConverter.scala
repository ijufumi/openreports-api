package jp.ijufumi.openreports.infrastructure.persistence.converter

import jp.ijufumi.openreports.domain.models.entity.{ReportReportParameter => ReportReportParameterModel}
import jp.ijufumi.openreports.infrastructure.persistence.entity.{ReportReportParameter => ReportReportParameterEntity}

object ReportReportParameterConverter {
  def toDomain(entity: ReportReportParameterEntity): ReportReportParameterModel = {
    ReportReportParameterModel(
      entity.id,
      entity.reportId,
      entity.reportParameterId,
      entity.createdAt,
      entity.updatedAt,
      entity.versions,
    )
  }

  def toEntity(model: ReportReportParameterModel): ReportReportParameterEntity = {
    ReportReportParameterEntity(
      model.id,
      model.reportId,
      model.reportParameterId,
      model.createdAt,
      model.updatedAt,
      model.versions,
    )
  }

  object conversions {
    import scala.language.implicitConversions

    implicit def toReportReportParameterEntity(model: ReportReportParameterModel): ReportReportParameterEntity = toEntity(model)
    implicit def toReportReportParameterEntities(model: Seq[ReportReportParameterModel]): Seq[ReportReportParameterEntity] = model.map(toEntity)
    implicit def fromReportReportParameterEntity(entity: ReportReportParameterEntity): ReportReportParameterModel = toDomain(entity)
    implicit def fromReportReportParameterEntities(entity: Seq[ReportReportParameterEntity]): Seq[ReportReportParameterModel] = entity.map(toDomain)
  }
}

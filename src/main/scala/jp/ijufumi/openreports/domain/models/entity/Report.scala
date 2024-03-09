package jp.ijufumi.openreports.domain.models.entity

import jp.ijufumi.openreports.infrastructure.datastores.database.entities.{Report => ReportEntity, Template => ReportTemplateEntity}
import jp.ijufumi.openreports.presentation.models.responses.{Report => ReportResponse}
import jp.ijufumi.openreports.presentation.models.requests.UpdateReport
import jp.ijufumi.openreports.utils.Dates

case class Report(
    id: String,
    name: String,
    templateId: String,
    dataSourceId: Option[String],
    workspaceId: String,
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
    reportTemplate: Option[Template] = None,
) {
  def toEntity: ReportEntity = {
    ReportEntity(
      this.id,
      this.name,
      this.templateId,
      this.dataSourceId,
      this.workspaceId,
      this.createdAt,
      this.updatedAt,
      this.versions,
    )
  }
  def toResponse: ReportResponse = {
    ReportResponse(
      this.id,
      this.name,
      this.templateId,
      this.dataSourceId,
    )
  }
  def copyForUpdate(input: UpdateReport): Report = {
    this.copy(name = input.name, templateId = input.templateId, dataSourceId = input.dataSourceId)
  }
}

object Report {
  def apply(entity: ReportEntity): Report = {
    Report(
      entity.id,
      entity.name,
      entity.templateId,
      entity.dataSourceId,
      entity.workspaceId,
      entity.createdAt,
      entity.updatedAt,
      entity.versions,
    )
  }
  def apply(entity: (ReportEntity, ReportTemplateEntity)): Report = {
    Report(
      entity._1.id,
      entity._1.name,
      entity._1.templateId,
      entity._1.dataSourceId,
      entity._1.workspaceId,
      entity._1.createdAt,
      entity._1.updatedAt,
      entity._1.versions,
      Some(Template(entity._2)),
    )
  }

  object conversions {
    import scala.language.implicitConversions

    implicit def toReportEntity(model: Report): ReportEntity = {
      model.toEntity
    }

    implicit def fromReportEntity(entity: ReportEntity): Report = {
      Report(entity)
    }

    implicit def fromReportEntity2(entity: Option[ReportEntity]): Option[Report] = {
      entity.map(e => Report(e))
    }

    implicit def fromReportEntity3(entity: (ReportEntity, ReportTemplateEntity)): Report = {
      Report(entity)
    }

    implicit def fromReportEntity4(entity: Option[(ReportEntity, ReportTemplateEntity)]): Option[Report] = {
      entity.map(e => Report(e))
    }

    implicit def fromReportEntities(entity: Seq[ReportEntity]): Seq[Report] = {
      entity.map(e => Report(e))
    }

    implicit def fromReportEntities2(entity: Seq[(ReportEntity, ReportTemplateEntity)]): Seq[Report] = {
      entity.map(e => Report(e))
    }

    implicit def toReportResponse(model: Report): ReportResponse = {
      model.toResponse
    }

    implicit def toReportResponse2(model: Option[Report]): Option[ReportResponse] = {
      model.map(m => m.toResponse)
    }

    implicit def toReportResponses(model: Seq[Report]): Seq[ReportResponse] = {
      model.map(m => m.toResponse)
    }
  }
}

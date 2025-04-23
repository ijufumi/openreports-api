package jp.ijufumi.openreports.domain.models.entity

import jp.ijufumi.openreports.domain.models.value.enums.StorageTypes
import jp.ijufumi.openreports.infrastructure.datastores.database.entities.{
  ReportTemplate => ReportTemplateEntity,
}
import jp.ijufumi.openreports.presentation.models.responses.{ReportTemplate => ReportTemplateResponse}
import jp.ijufumi.openreports.presentation.models.requests.UpdateTemplate
import jp.ijufumi.openreports.utils.Dates

case class ReportTemplate(
    id: String,
    name: String,
    filePath: String,
    workspaceId: String,
    storageType: StorageTypes.StorageType,
    fileSize: Long,
    isSeed: Boolean = false,
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
) {
  def toEntity: ReportTemplateEntity = {
    ReportTemplateEntity(
      this.id,
      this.name,
      this.filePath,
      this.workspaceId,
      this.storageType,
      this.fileSize,
      this.isSeed,
      this.createdAt,
      this.updatedAt,
      this.versions,
    )
  }
  def toResponse: ReportTemplateResponse = {
    ReportTemplateResponse(
      this.id,
      this.name,
      this.filePath,
      this.storageType,
      this.fileSize,
      this.createdAt,
      this.updatedAt,
    )
  }
  def copyForUpdate(input: UpdateTemplate): ReportTemplate = {
    this.copy(name = input.name)
  }
}

object ReportTemplate {
  def apply(entity: ReportTemplateEntity): ReportTemplate = {
    ReportTemplate(
      entity.id,
      entity.name,
      entity.filePath,
      entity.workspaceId,
      entity.storageType,
      entity.fileSize,
      entity.isSeed,
      entity.createdAt,
      entity.updatedAt,
      entity.versions,
    )
  }

  object conversions {
    import scala.language.implicitConversions

    implicit def toTemplateEntity(model: ReportTemplate): ReportTemplateEntity = {
      model.toEntity
    }

    implicit def fromTemplateEntity(entity: ReportTemplateEntity): ReportTemplate = {
      ReportTemplate(entity)
    }

    implicit def fromTemplateEntity2(entity: Option[ReportTemplateEntity]): Option[ReportTemplate] = {
      entity.map(e => ReportTemplate(e))
    }

    implicit def fromTemplateEntities(entity: Seq[ReportTemplateEntity]): Seq[ReportTemplate] = {
      entity.map(e => ReportTemplate(e))
    }

    implicit def toTemplateResponse(model: ReportTemplate): ReportTemplateResponse = {
      model.toResponse
    }

    implicit def toTemplateResponse2(model: Option[ReportTemplate]): Option[ReportTemplateResponse] = {
      model.map(m => m.toResponse)
    }

    implicit def toTemplateResponses(model: Seq[ReportTemplate]): Seq[ReportTemplateResponse] = {
      model.map(m => m.toResponse)
    }
  }
}

package jp.ijufumi.openreports.domain.models.entity

import jp.ijufumi.openreports.domain.models.value.enums.StorageTypes
import jp.ijufumi.openreports.infrastructure.datastores.database.entities.{
  Template => TemplateEntity,
}
import jp.ijufumi.openreports.presentation.models.responses.{Template => TemplateResponse}
import jp.ijufumi.openreports.presentation.models.requests.UpdateTemplate
import jp.ijufumi.openreports.utils.Dates

case class Template(
    id: String,
    name: String,
    filePath: String,
    workspaceId: String,
    storageType: StorageTypes.StorageType,
    fileSize: Long,
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
) {
  def toEntity: TemplateEntity = {
    TemplateEntity(
      this.id,
      this.name,
      this.filePath,
      this.workspaceId,
      this.storageType,
      this.fileSize,
      this.createdAt,
      this.updatedAt,
      this.versions,
    )
  }
  def toResponse: TemplateResponse = {
    TemplateResponse(
      this.id,
      this.name,
      this.filePath,
      this.storageType,
      this.fileSize,
    )
  }
  def copyForUpdate(input: UpdateTemplate): Template = {
    this.copy(name = input.name)
  }
}

object Template {
  def apply(entity: TemplateEntity): Template = {
    Template(
      entity.id,
      entity.name,
      entity.filePath,
      entity.workspaceId,
      entity.storageType,
      entity.fileSize,
      entity.createdAt,
      entity.updatedAt,
      entity.versions,
    )
  }

  object conversions {
    import scala.language.implicitConversions

    implicit def toTemplateEntity(model: Template): TemplateEntity = {
      model.toEntity
    }

    implicit def fromTemplateEntity(entity: TemplateEntity): Template = {
      Template(entity)
    }

    implicit def fromTemplateEntity2(entity: Option[TemplateEntity]): Option[Template] = {
      entity.map(e => Template(e))
    }

    implicit def fromTemplateEntities(entity: Seq[TemplateEntity]): Seq[Template] = {
      entity.map(e => Template(e))
    }

    implicit def toTemplateResponse(model: Template): TemplateResponse = {
      model.toResponse
    }

    implicit def toTemplateResponse2(model: Option[Template]): Option[TemplateResponse] = {
      model.map(m => m.toResponse)
    }

    implicit def toTemplateResponses(model: Seq[Template]): Seq[TemplateResponse] = {
      model.map(m => m.toResponse)
    }
  }
}

package jp.ijufumi.openreports.infrastructure.datastores.database.repositories

import jp.ijufumi.openreports.domain.models.entity.Template

trait TemplateRepository {

  def gets(workspaceId: String, offset: Int, limit: Int): (Seq[Template], Int)

  def getById(workspaceId: String, id: String): Option[Template]

  def register(model: Template): Option[Template]

  def update(model: Template): Unit

  def delete(workspaceId: String, id: String): Unit
}

package jp.ijufumi.openreports.repositories.db

import jp.ijufumi.openreports.entities.Template

trait TemplateRepository {

  def gets(workspaceId: String, offset: Int, limit: Int): (Seq[Template], Int)

  def getById(workspaceId: String, id: String): Option[Template]

  def register(model: Template): Option[Template]

  def update(model: Template): Unit
}

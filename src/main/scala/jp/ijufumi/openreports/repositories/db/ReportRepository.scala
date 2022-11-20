package jp.ijufumi.openreports.repositories.db

import jp.ijufumi.openreports.entities.{Report, Template}

trait ReportRepository {

  def gets(workspaceId: String, offset: Int, limit: Int): (Seq[Report], Int)

  def getsWithTemplate(workspaceId: String, offset: Int, limit: Int): (Seq[(Report, Template)], Int)

  def getById(workspaceId: String, id: String): Option[Report]

  def getByIdWithTemplate(workspaceId: String, id: String): Option[(Report, Template)]

  def register(model: Report): Option[Report]

  def update(model: Report): Unit

  def delete(workspaceId: String, id: String): Unit
}

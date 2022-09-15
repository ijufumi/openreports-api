package jp.ijufumi.openreports.repositories.db

import jp.ijufumi.openreports.entities.Report

trait ReportRepository {

  def getById(id: String): Option[Report]

  def register(model: Report): Option[Report]

  def update(model: Report): Unit
}

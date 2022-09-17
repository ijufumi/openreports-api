package jp.ijufumi.openreports.services

import jp.ijufumi.openreports.vo.response.{Report, Reports}

trait ReportService {
  def getReports(): Reports

  def getReport(id: String): Option[Report]
}

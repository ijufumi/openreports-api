package jp.ijufumi.openreports.services

import jp.ijufumi.openreports.vo.response.{Report, Reports}

import java.io.File

trait ReportService {
  def getReports(page: Int, limit: Int): Reports

  def getReport(id: String): Option[Report]

  def outputReport(id: String): Option[File]
}

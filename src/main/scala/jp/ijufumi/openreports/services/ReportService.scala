package jp.ijufumi.openreports.services

import jp.ijufumi.openreports.vo.response.{Report, Lists}

import java.io.File

trait ReportService {
  def getReports(page: Int, limit: Int): Lists

  def getReport(id: String): Option[Report]

  def outputReport(id: String): Option[File]
}

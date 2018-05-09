package jp.ijufumi.openreports.controller

import java.io.{ BufferedInputStream, InputStream }

import jp.ijufumi.openreports.service.ReportingService
import skinny.Params

import scala.util.control.Breaks

class ReportController extends ApplicationController {

  def requestParams = Params(params)

  def download: Unit = {
    val fileStream = getClass.getClassLoader.getResourceAsStream("report/sample.xlsx")
    fileDownload(fileStream, "sample.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
  }

  def outputReport: Unit = {
    val reportFile = new ReportingService("report/sample.xlsx").output()
    if (reportFile != "") {
      val fileStream = getClass.getClassLoader.getResourceAsStream(reportFile)
      fileDownload(fileStream, "sample.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    }
  }
}

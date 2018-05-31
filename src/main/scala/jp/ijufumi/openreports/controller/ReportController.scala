package jp.ijufumi.openreports.controller

import jp.ijufumi.openreports.controller.common.ApplicationController
import jp.ijufumi.openreports.service.ReportingService
import skinny.Params

class ReportController extends ApplicationController {
  val path = privatePath + "/report"
  val viewPath = privatePath + "/report"

  def requestParams = Params(params)

  def index = {
    render(render(viewPath + "/index"))
  }

  def download: Unit = {
    val fileStream = getClass.getClassLoader.getResourceAsStream("report/sample.xlsx")
    fileDownload(fileStream, "sample.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
  }

  def outputReport: Unit = {
    val reportFileOpt = ReportingService("report/sample.xlsx").output()
    if (reportFileOpt.nonEmpty) {
      var reportFile = reportFileOpt.get
      fileDownload(reportFile.getAbsolutePath, reportFile.getName, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
      deleteFile(reportFile)
    }
  }
}

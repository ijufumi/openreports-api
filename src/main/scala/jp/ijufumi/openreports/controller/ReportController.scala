package jp.ijufumi.openreports.controller

import jp.ijufumi.openreports.controller.common.ApiController
import skinny.Params
import skinny.validator.{paramKey, required}

class ReportController extends ApiController {
  val path: String = PrivatePath + "/report"

  def validateParams = validation(
    requestParams,
    paramKey("reportId") is required,
    paramKey("pageNo") is required
  )

  def requestParams = Params(params)

  def groupList = {
    okResponse("not implemented")
  }

  def reportList = {
    okResponse("not implemented")
  }

  def download = {
    okResponse("not implemented")
  }

  def prepareReport = {
    okResponse("not implemented")
  }

  def setParams = {
    okResponse("not implemented")
  }
  //  def outputReport: Unit = {
  //    val reportFileOpt = ReportingSupportService("report/sample.xlsx").output()
  //    if (reportFileOpt.nonEmpty) {
  //      var reportFile = reportFileOpt.get
  //      fileDownload(reportFile.getAbsolutePath, reportFile.getName, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
  //      deleteFile(reportFile)
  //    }
  //  }
}

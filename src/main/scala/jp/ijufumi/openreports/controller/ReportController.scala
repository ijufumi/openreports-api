package jp.ijufumi.openreports.controller

import jp.ijufumi.openreports.controller.common.ApiController
import jp.ijufumi.openreports.service.settings.ReportTemplateSettingsService
import jp.ijufumi.openreports.service.{ReportService, ReportingOutputService}
import jp.ijufumi.openreports.vo.{ApiResponse, MemberInfo}
import skinny.Params
import skinny.validator.{paramKey, required}

import scala.collection.mutable

class ReportController extends ApiController {
  val path: String = PrivatePath + "/report"

  def validateParams = validation(
    requestParams,
    paramKey("reportId") is required,
    paramKey("pageNo") is required
  )

  def requestParams = Params(params)

  def groupList = {
    okResponse()
  }

  def reportList = {
    okResponse()
  }

  def download = {
    okResponse()
  }

  def prepareReport = {
    okResponse()
  }

  def setParams = {
    okResponse()
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

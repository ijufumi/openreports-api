package jp.ijufumi.openreports.controller

import jp.ijufumi.openreports.controller.common.ApplicationController
import jp.ijufumi.openreports.service.ReportService
import jp.ijufumi.openreports.service.common.ReportingService
import jp.ijufumi.openreports.vo.MemberInfo
import skinny.Params

class ReportController extends ApplicationController {
  val path = privatePath + "/report"
  val viewPath = privatePath + "/report"

  override val activeMenu = "report"
  override val requiredMemberInfo = true

  def requestParams = Params(params)

  def index = {
    val memberInfo: Option[MemberInfo] = skinnySession.getAttribute("memberInfo").asInstanceOf[Option[MemberInfo]]
    val reportGroups = new ReportService().list(memberInfo.get.groups)
    set("reportGroups", reportGroups)
    render(viewPath + "/index")
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

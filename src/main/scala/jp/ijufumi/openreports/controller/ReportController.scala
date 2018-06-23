package jp.ijufumi.openreports.controller

import jp.ijufumi.openreports.controller.common.ApplicationController
import jp.ijufumi.openreports.service.ReportService
import jp.ijufumi.openreports.service.support.ReportingSupportService
import jp.ijufumi.openreports.vo.{MemberInfo, ReportGroupInfo}

class ReportController extends ApplicationController {
  val path = privatePath + "/report"
  val viewPath = privatePath + "/report"

  override val activeMenu = "report"
  override val requiredMemberInfo = true

  def groupList = {
    val memberInfo: Option[MemberInfo] = skinnySession.getAttribute("memberInfo").asInstanceOf[Option[MemberInfo]]
    val reportGroups = ReportService().groupList(memberInfo.get.groups)
    set("reportGroups", reportGroups.map(r => ReportGroupInfo(r.reportGroupId, r.reportGroupName)).seq)
    render(viewPath + "/index")
  }

  def reportList = params.getAs[Long]("id").map { id =>
    val reports = ReportService().reportList(id)
    set("reports", reports)
    render(viewPath + "/report-list")
  }

  def download: Unit = {
    val fileStream = getClass.getClassLoader.getResourceAsStream("report/sample.xlsx")
    fileDownload(fileStream, "sample.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
  }

  def outputReport: Unit = {
    val reportFileOpt = ReportingSupportService("report/sample.xlsx").output()
    if (reportFileOpt.nonEmpty) {
      var reportFile = reportFileOpt.get
      fileDownload(reportFile.getAbsolutePath, reportFile.getName, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
      deleteFile(reportFile)
    }
  }
}

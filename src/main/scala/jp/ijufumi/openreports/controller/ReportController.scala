package jp.ijufumi.openreports.controller

import jp.ijufumi.openreports.controller.common.ApplicationController
import jp.ijufumi.openreports.service.ReportService
import jp.ijufumi.openreports.vo.MemberInfo

class ReportController extends ApplicationController {
  val path = privatePath + "/report"
  val viewPath = privatePath + "/report"

  override val activeMenu = "report"
  override val requiredMemberInfo = true

  def groupList = {
    val memberInfo: Option[MemberInfo] = skinnySession.getAttribute("memberInfo").asInstanceOf[Option[MemberInfo]]
    val reportGroups = ReportService().groupList(memberInfo.get.groups)
    set("reportGroups", reportGroups)
    render(viewPath + "/index")
  }

  def reportList = params.getAs[Long]("id").map { id =>
    val reports = ReportService().reportList(id)
    set("reports", reports)
    render(viewPath + "/report-list")
  } getOrElse haltWithBody(404)

  def download: Unit = {
    val fileStream = getClass.getClassLoader.getResourceAsStream("report/sample.xlsx")
    fileDownload(fileStream, "sample.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
  }

  def prepareReport = params.getAs[Long]("id").map { id =>
    val pageNo = params.getAs[Int]("pageNo") getOrElse 0
    val (paramInfo, nextPageNo) = ReportService().paramInfo(id, pageNo)
    set("paramInfo", paramInfo)
    set("nextPageNo", nextPageNo)
    render(viewPath + "/prepare-report")
  } getOrElse haltWithBody(404)

  def printOutReport = {

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

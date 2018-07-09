package jp.ijufumi.openreports.service

import jp.ijufumi.openreports.model._
import jp.ijufumi.openreports.service.enums.ReportParamType._
import jp.ijufumi.openreports.vo.{ReportGroupInfo, ReportInfo, ReportParamInfo}
import skinny.logging.Logging

import scala.collection.mutable

class ReportService extends Logging {
  def report(reportId: Long): Option[ReportInfo] = {
    val report = TReport.findById(reportId)

    report.map { r => Option(ReportInfo(r.reportId, r.reportName, r.templatePath)) } getOrElse Option.empty
  }

  def groupList(groupId: Seq[Long]): Seq[ReportGroupInfo] = {

    val groups = TGroup.includes(TGroup.reportGroups).where('groupId -> groupId).apply()

    if (logger.isDebugEnabled) {
      logger.debug("groups:%s".format(groups))
    }

    val reportGroups = mutable.Set[TReportGroup]()
    for (g <- groups; rg <- g.reportGroups) {
      reportGroups += rg
    }

    if (logger.isDebugEnabled) {
      logger.debug("reportGroups:%s".format(reportGroups))
    }

    reportGroups
      .toSeq
      .sortBy(_.reportGroupId)
      .map(r => ReportGroupInfo(r.reportGroupId, r.reportGroupName))
  }

  def reportList(groupId: Long): Seq[ReportInfo] = {
    val reportGroup = TReportGroup.includes(TReportGroup.reports).findById(groupId)
    if (reportGroup.isEmpty) {
      return Seq.empty
    }

    reportGroup
      .get
      .reports
      .sortBy(_.reportId)
      .map { r => ReportInfo(r.reportId, r.reportName) }
  }

  def paramInfo(reportId: Long, pageNo: Int): (Seq[ReportParamInfo], Int) = {
    val report = TReport.includes(TReport.params).findById(reportId)

    if (report.isEmpty) {
      logger.info("report not exists. id:%d".format(reportId))
    }

    val infos =
      report
        .get
        .params
        .filter(_.pageNo == pageNo)
        .sortWith((a, b) => a.seq < b.seq)
        .map {
          _.param.get
        }
        .map { p =>
          var values: Seq[Map[String, String]] = Seq.empty

          if (LIST.equals(p.paramType)) {
            values = p.paramValues.split(",").map { v => Map(v -> v) }.toSeq
          } else if (QUERY.equals(p.paramType)) {
            // TODO:values from select query.
          }

          ReportParamInfo(p, values)
        }

    val nextPageNo =
      report
        .get
        .params
        .find(_.pageNo == pageNo + 1)
        .map(_.pageNo)
        .getOrElse(-1)

    (infos, nextPageNo)
  }
}

object ReportService {
  def apply(): ReportService = {
    new ReportService()
  }
}

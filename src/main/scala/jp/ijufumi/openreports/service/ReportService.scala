package jp.ijufumi.openreports.service

import jp.ijufumi.openreports.model._
import jp.ijufumi.openreports.model.support.ParamType._
import jp.ijufumi.openreports.vo.{ReportGroupInfo, ReportInfo, ReportParamInfo}
import skinny.logging.Logging

import scala.collection.mutable

class ReportService extends Logging {
  def groupList(groupId: Seq[Long]): Seq[ReportGroupInfo] = {

    val groups = TGroup.includes(TGroup.reportGroups).where('groupId -> groupId).apply()

    logger.debug("groups:%s".format(groups))

    val reportGroups = mutable.Set[TReportGroup]()
    for (g <- groups; rg <- g.reportGroups) {
      reportGroups += rg
    }

    logger.debug("reportGroups:%s".format(reportGroups))

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

  def paramInfo(reportId: Long): Seq[ReportParamInfo] = {
    val report = TReport.includes(TReport.params).findById(reportId)

    if (report.isEmpty) {
      logger.info("report not exists. id:%d".format(reportId))
    }

    report
      .get
      .params
      .sortWith((a, b) => {
        if (a.pageNo == b.pageNo) {
          a.seq < b.seq
        } else {
          a.pageNo < b.pageNo
        }
      })
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
  }
}

object ReportService {
  def apply(): ReportService = {
    new ReportService()
  }
}
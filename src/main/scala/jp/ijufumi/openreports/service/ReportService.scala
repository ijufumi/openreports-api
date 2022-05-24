package jp.ijufumi.openreports.service

import jp.ijufumi.openreports.model._
import jp.ijufumi.openreports.service.enums.ReportParamType._
import jp.ijufumi.openreports.service.settings.ReportSettingsService
import jp.ijufumi.openreports.vo.{ReportGroupInfo, ReportInfo, ReportParamInfo}
import skinny.logging.Logging

import scala.collection.mutable

class ReportService extends Logging {
  def report(reportId: Long): Option[ReportInfo] = {
    new ReportSettingsService().getReport(reportId)
  }

  def groupList(groupId: Seq[Long]): Array[ReportGroupInfo] = {

    val groups =
      TGroup.includes(TGroup.reportGroups).where("groupId" -> groupId).apply()

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

    reportGroups.toSeq
      .sortBy(_.reportGroupId)
      .map(r => ReportGroupInfo(r.reportGroupId, r.reportGroupName, r.createdAt, r.updatedAt, r.versions)).toArray
  }

  def reportList(groupId: Long): Array[ReportInfo] = {
    val reportGroup =
      TReportGroup.includes(TReportGroup.reports).findById(groupId)

    val service = new ReportSettingsService()
    reportGroup
      .map(
        rg =>
          rg.reports
            .sortBy(_.reportId)
            .map { r => service.getReport(r.reportId).get
          })
      .getOrElse(Seq.empty).toArray
  }

  def paramInfo(reportId: Long, pageNo: Int): (Array[ReportParamInfo], Int) = {
    val report = TReport.includes(TReport.params).findById(reportId)

    if (report.isEmpty) {
      logger.info("report not exists. id:%d".format(reportId))
    }

    val infos =
      report.get.params
        .filter(_.pageNo == pageNo)
        .sortWith((a, b) => a.seq < b.seq)
        .map {
          _.param.get
        }
        .map { p =>
          var values: Seq[Map[String, String]] = Seq.empty

          if (LIST.equals(p.paramType)) {
            values = p.paramValues
              .split(",")
              .map { v =>
                Map(v -> v)
              }
              .toSeq
          } else if (QUERY.equals(p.paramType)) {
            // TODO:values from select query.
          }

          ReportParamInfo(p, values)
        }

    val nextPageNo =
      report.get.params
        .find(_.pageNo == pageNo + 1)
        .map(_.pageNo)
        .getOrElse(-1)

    (infos.toArray, nextPageNo)
  }
}

object ReportService {
  def apply(): ReportService = {
    new ReportService()
  }
}

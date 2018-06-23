package jp.ijufumi.openreports.service

import jp.ijufumi.openreports.model.{TGroup, TReport, TReportGroup}
import skinny.logging.Logging

import scala.collection.mutable

class ReportService extends Logging {
  def groupList(groupId: Seq[Long]): Seq[TReportGroup] = {

    val groups = TGroup.includes(TGroup.reportGroups).where('groupId -> groupId).apply()

    logger.debug("groups:%s".format(groups))

    val reportGroups = mutable.Set[TReportGroup]()
    for (g <- groups; rg <- g.reportGroups) {
      reportGroups += rg
    }

    logger.debug("reportGroups:%s".format(reportGroups))

    reportGroups.toSeq.sortBy(_.reportGroupId)
  }

  def reportList(groupId: Long): Seq[TReport] = {
    val reportGroup = TReportGroup.includes(TReportGroup.reports).findById(groupId)
    if (reportGroup.isEmpty) {
      return Seq.empty
    }

    reportGroup.get.reports
  }
}

object ReportService {
  def apply(): ReportService = {
    new ReportService()
  }
}
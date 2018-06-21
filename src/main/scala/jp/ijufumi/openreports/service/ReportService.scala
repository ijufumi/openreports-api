package jp.ijufumi.openreports.service

import jp.ijufumi.openreports.model.{ TGroup, TReportGroup }
import skinny.logging.Logging

import scala.collection.mutable

class ReportService extends Logging {
  def list(groupId: Set[Long]): Seq[TReportGroup] = {
    val groups = TGroup.includes(TGroup.reportGroups).where('groupId -> Seq(1, 2, 3)).apply() // TODO:Use groupId params.

    logger.debug("groups:%s".format(groups))

    val reportGroups = mutable.Set[TReportGroup]()
    for (g <- groups; rg <- g.reportGroups) {
      reportGroups += rg
    }

    logger.debug("reportGroups:%s".format(reportGroups))

    reportGroups.toSeq.sortBy(_.reportGroupId)
  }
}

object ReportService {
  def apply(): ReportService = {
    new ReportService()
  }
}
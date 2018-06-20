package jp.ijufumi.openreports.service

import jp.ijufumi.openreports.model.{ TGroup, TReportGroup }

import scala.collection.mutable

class ReportService {
  def list(groupId: Set[Long]): Set[TReportGroup] = {
    val groups = TGroup.includes(TGroup.reportGroups).where('id -> groupId).apply()
    val reportGroups = mutable.Set[TReportGroup]()
    for (g <- groups; rg <- g.reportGroups) {
      reportGroups + rg
    }

    reportGroups.toSet
  }
}

object ReportService {
  def apply(): ReportService = {
    new ReportService()
  }
}
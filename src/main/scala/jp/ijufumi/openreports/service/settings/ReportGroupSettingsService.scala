package jp.ijufumi.openreports.service.settings

import java.sql.SQLException

import jp.ijufumi.openreports.model.TReportGroup
import jp.ijufumi.openreports.service.enums.StatusCode
import jp.ijufumi.openreports.vo.ReportGroupInfo
import org.joda.time.DateTime
import skinny.logging.Logging

class ReportGroupSettingsService extends Logging {
  def getGroups: Seq[ReportGroupInfo] = {
    TReportGroup
      .findAll()
      .map(r => ReportGroupInfo(r.reportGroupId, r.reportGroupName, r.versions))
  }

  def getGroup(reportGroupId: Long): Option[ReportGroupInfo] = {
    TReportGroup
      .findById(reportGroupId)
      .map(r => ReportGroupInfo(r.reportGroupId, r.reportGroupName, r.versions))
  }

  def registerGroup(reportGroupName: String): StatusCode.Value = {
    try {
      TReportGroup.createWithAttributes('reportGroupName -> reportGroupName)
    } catch {
      case e: SQLException => return StatusCode.of(e)
      case _               => return StatusCode.OTHER_ERROR
    }

    StatusCode.OK
  }

  def updateGroup(
      reportGroupId: Long,
      reportGroupName: String,
      versions: Long
  ): StatusCode.Value = {
    try {
      val groupOpt = TReportGroup.findById(reportGroupId)
      if (groupOpt.isEmpty) {
        return StatusCode.DATA_NOT_FOUND
      }

      val group = groupOpt.get

      val count = TReportGroup
        .updateByIdAndVersion(reportGroupId, versions)
        .withAttributes('reportGroupName -> reportGroupName,
                        'updatedAt -> DateTime.now())

      if (count != 1) {
        return StatusCode.ALREADY_UPDATED
      }
    } catch {
      case e: SQLException => return StatusCode.of(e)
      case _               => return StatusCode.OTHER_ERROR
    }

    StatusCode.OK
  }
}

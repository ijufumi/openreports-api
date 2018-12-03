package jp.ijufumi.openreports.model

import java.time.LocalDateTime

import scalikejdbc.WrappedResultSet
import skinny.orm.SkinnyCRUDMapper

case class TReportTemplateHistory(
    historyId: Long,
    templateId: Long,
    fileName: String,
    filePath: String,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
    versions: Long
)

object TReportTemplateHistory extends SkinnyCRUDMapper[TReportTemplateHistory] {

  override def tableName = "t_report_template_history"

  override def defaultAlias = createAlias("rep_temp_his")

  override def primaryKeyFieldName = "historyId"

  override def extract(rs: WrappedResultSet,
                       n: scalikejdbc.ResultName[TReportTemplateHistory])
    : TReportTemplateHistory =
    new TReportTemplateHistory(
      historyId = rs.get(n.historyId),
      templateId = rs.get(n.templateId),
      fileName = rs.get(n.fileName),
      filePath = rs.get(n.filePath),
      createdAt = rs.get(n.createdAt),
      updatedAt = rs.get(n.updatedAt),
      versions = rs.get(n.versions)
    )
}

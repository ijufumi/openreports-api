package jp.ijufumi.openreports.model

import org.joda.time.DateTime
import scalikejdbc.WrappedResultSet
import skinny.orm.SkinnyCRUDMapper
import skinny.orm.feature.OptimisticLockWithVersionFeature

case class TReportTemplate(templateId: Long,
                           fileName: String,
                           filePath: String,
                           createdAt: DateTime,
                           updatedAt: DateTime,
                           versions: Long)

object TReportTemplate
    extends SkinnyCRUDMapper[TReportTemplate]
    with OptimisticLockWithVersionFeature[TReportTemplate] {

  override def tableName = "t_report_template"

  override def defaultAlias = createAlias("rep_temp")

  override def primaryKeyFieldName = "templateId"

  override def lockVersionFieldName: String = "versions"

  override def extract(
      rs: WrappedResultSet,
      n: scalikejdbc.ResultName[TReportTemplate]): TReportTemplate =
    new TReportTemplate(
      templateId = rs.get(n.templateId),
      fileName = rs.get(n.fileName),
      filePath = rs.get(n.filePath),
      createdAt = rs.get(n.createdAt),
      updatedAt = rs.get(n.updatedAt),
      versions = rs.get(n.versions)
    )
}

package jp.ijufumi.openreports.model

import java.time.LocalDateTime

import scalikejdbc.WrappedResultSet
import scalikejdbc.interpolation.SQLSyntax
import skinny.orm.SkinnyCRUDMapper
import skinny.orm.feature.OptimisticLockWithVersionFeature

case class TReport(reportId: Long,
                   reportName: String,
                   description: String,
                   templateId: Long,
                   createdAt: LocalDateTime,
                   updatedAt: LocalDateTime,
                   versions: Long,
                   params: Seq[TReportParamConfig] = Nil,
                   groups: Seq[TReportGroup] = Nil)

object TReport
    extends SkinnyCRUDMapper[TReport]
    with OptimisticLockWithVersionFeature[TReport] {

  lazy val params = hasMany[TReportParamConfig](
    many = TReportParamConfig -> TReportParamConfig.defaultAlias,
    on = (r, p) => SQLSyntax.eq(r.field("reportId"), p.field("reportId")),
    merge = (r, params) => r.copy(params = params)
  ).includes[TReportParamConfig](
    (a, b) =>
      a.map { m =>
        m.copy(params = b)
    }
  )

  override def tableName = "t_report"

  override def defaultAlias = createAlias("rep")

  override def primaryKeyFieldName = "reportId"

  override def lockVersionFieldName: String = "versions"

  override def extract(rs: WrappedResultSet,
                       n: scalikejdbc.ResultName[TReport]): TReport =
    new TReport(
      reportId = rs.get(n.reportId),
      reportName = rs.get(n.reportName),
      description = rs.get(n.description),
      templateId = rs.get(n.templateId),
      createdAt = rs.get(n.createdAt),
      updatedAt = rs.get(n.updatedAt),
      versions = rs.get(n.versions)
    )

  hasManyThroughWithFk[TReportGroup](
    through = RReportReportGroup,
    many = TReportGroup,
    throughFk = "reportId",
    manyFk = "reportGroupId",
    merge = (a, groups) => a.copy(groups = groups)
  ).byDefault
}

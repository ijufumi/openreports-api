package jp.ijufumi.openreports.model

import org.joda.time.DateTime
import scalikejdbc.WrappedResultSet
import skinny.orm.SkinnyCRUDMapper
import skinny.orm.feature.OptimisticLockWithVersionFeature

case class TReportParamConfig(configId: Long,
                              reportId: Long,
                              paramId: Long,
                              pageNo: Int,
                              seq: Int,
                              createdAt: DateTime,
                              updatedAt: DateTime,
                              versions: Long
                             )

object TReportParamConfig extends SkinnyCRUDMapper[TReportParamConfig]
  with OptimisticLockWithVersionFeature[TReportParamConfig] {
  override def tableName = "t_report_param_config"

  override def defaultAlias = createAlias("rep_prm_cfg")

  override def primaryKeyFieldName = "configId"

  override def lockVersionFieldName: String = "versions"

  override def extract(rs: WrappedResultSet, n: scalikejdbc.ResultName[TReportParamConfig]): TReportParamConfig = new TReportParamConfig(
    configId = rs.get(n.configId),
    reportId = rs.get(n.reportId),
    paramId = rs.get(n.paramId),
    pageNo = rs.get(n.pageNo),
    seq = rs.get(n.seq),
    createdAt = rs.get(n.createdAt),
    updatedAt = rs.get(n.updatedAt),
    versions = rs.get(n.varsions)
  )
}
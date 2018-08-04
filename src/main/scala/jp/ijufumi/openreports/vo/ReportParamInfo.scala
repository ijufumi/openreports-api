package jp.ijufumi.openreports.vo

import jp.ijufumi.openreports.model.TReportParam

import scala.beans.BeanProperty

case class ReportParamInfo(@BeanProperty paramId: Long,
                           @BeanProperty paramKey: String,
                           @BeanProperty paramName: String,
                           @BeanProperty paramType: String,
                           @BeanProperty paramValues: Seq[Map[String, String]] =
                             Seq.empty)

object ReportParamInfo {
  def apply(param: TReportParam): ReportParamInfo = {
    ReportParamInfo(
      param.paramId,
      param.paramKey,
      param.paramName,
      param.paramType
    )
  }

  def apply(param: TReportParam,
            values: Seq[Map[String, String]]): ReportParamInfo = {
    ReportParamInfo(
      param.paramId,
      param.paramKey,
      param.paramName,
      param.paramType,
      values
    )
  }
}

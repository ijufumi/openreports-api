package jp.ijufumi.openreports.vo

import jp.ijufumi.openreports.model.TReportParam

case class ReportParamInfo(
    _paramId: Long,
    _paramKey: String,
    _paramName: String,
    _paramType: String,
    _paramValues: Seq[Map[String, String]] = Seq.empty
) {

  def paramId: Long = _paramId

  def paramName: String = _paramName

  def paramType: String = _paramType

  def paramValues: Seq[Map[String, String]] = _paramValues

}

object ReportParamInfo {
  def apply(param: TReportParam, values: Seq[Map[String, String]] = Seq.empty): ReportParamInfo = {
    ReportParamInfo(param.paramId, param.paramKey, param.paramName, param.paramType, values)
  }
}

package jp.ijufumi.openreports.vo

import jp.ijufumi.openreports.model.TReportParam

case class ReportParamInfo(
    _paramId: Long,
    _paramKey: String,
    _paramName: String,
    _paramType: String,
    _paramValues: Seq[String] = Seq.empty
) {

  def paramId: Long = _paramId

  def paramName: String = _paramName

  def paramType: String = _paramType

  def paramValues: Seq[String] = _paramValues

}

object ReportParamInfo {
  def apply(param: TReportParam): ReportParamInfo = {
    ReportParamInfo(param.paramId, param.paramName, param.paramName, param.paramType)
  }
}

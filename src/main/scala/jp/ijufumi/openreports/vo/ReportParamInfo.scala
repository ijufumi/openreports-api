package jp.ijufumi.openreports.vo

import jp.ijufumi.openreports.model.TReportParam

case class ReportParamInfo(_paramId: Long, _paramName: String, _paramType: String) {
  def paramId: Long = _paramId

  def paramName: String = _paramName

  def paramType: String = _paramType

  def apply(param: TReportParam): ReportParamInfo = {
    ReportParamInfo(param.paramId, param.paramName, param.paramName)
  }
}

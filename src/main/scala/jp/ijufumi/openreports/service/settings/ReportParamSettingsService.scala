package jp.ijufumi.openreports.service.settings

import java.sql.SQLException

import jp.ijufumi.openreports.model.{TReportParam, TReportParamConfig}
import jp.ijufumi.openreports.service.enums.StatusCode
import jp.ijufumi.openreports.vo.{ReportParamConfigInfo, ReportParamInfo}
import skinny.Logging

class ReportParamSettingsService
  extends Logging {
  def getReportParamConfig(): Seq[ReportParamConfigInfo] = {
    TReportParamConfig
      .findAll()
      .map(p => ReportParamConfigInfo(p
        .paramId, p
        .pageNo, p
        .seq))
  }

  def getParam(paramId: Long): Option[ReportParamInfo] = {
    TReportParam
      .findById(paramId)
      .map(p => ReportParamInfo(p))
  }

  def registerParam(
    paramKey: String,
    paramName: String,
    description: String,
    paramType: String
  ): StatusCode.Value = {
    try {
      TReportParam
        .createWithAttributes(
          'paramKey -> paramKey,
          'paramName -> paramName,
          'description -> description,
          'paramType -> paramType
        )
    } catch {
      case e: SQLException => return StatusCode
        .of(e)
      case _: Throwable => return StatusCode
        .OTHER_ERROR
    }
    StatusCode
      .OK
  }
}

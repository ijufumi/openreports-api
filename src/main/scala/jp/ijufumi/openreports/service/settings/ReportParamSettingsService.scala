package jp.ijufumi.openreports.service.settings

import java.sql.SQLException

import jp.ijufumi.openreports.model.TReportParam
import jp.ijufumi.openreports.service.enums.StatusCode
import jp.ijufumi.openreports.vo.ReportParamInfo
import org.joda.time.DateTime
import skinny.Logging

class ReportParamSettingsService extends Logging {
  def getParams: Array[ReportParamInfo] = {
    TReportParam
      .findAll()
      .map(p => ReportParamInfo(p)).toArray
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
      paramType: String,
      paramValues: String
  ): StatusCode.Value = {
    try {
      TReportParam
        .createWithAttributes(
          'paramKey -> paramKey,
          'paramName -> paramName,
          'description -> description,
          'paramType -> paramType,
          'paramValues -> paramValues
        )
    } catch {
      case e: SQLException =>
        return StatusCode
          .of(e)
      case _: Throwable => return StatusCode.OTHER_ERROR
    }
    StatusCode.OK
  }

  def updateParam(
      paramId: Long,
      paramKey: String,
      paramName: String,
      description: String,
      paramType: String,
      versions: Long
  ): StatusCode.Value = {
    try {
      val paramOpt = TReportParam.findById(paramId)

      if (paramOpt.isEmpty) {
        return StatusCode.DATA_NOT_FOUND
      }

      val param = paramOpt.get

      TReportParam
        .updateByIdAndVersion(paramId, versions)
        .withAttributes(
          'paramKey -> paramKey,
          'paramName -> paramName,
          'description -> description,
          'paramType -> paramType,
          'updatedAt -> DateTime.now()
        )
    } catch {
      case e: SQLException =>
        return StatusCode
          .of(e)
      case _: Throwable => return StatusCode.OTHER_ERROR
    }

    StatusCode.OK
  }
}

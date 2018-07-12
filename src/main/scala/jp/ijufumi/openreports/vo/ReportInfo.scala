package jp.ijufumi.openreports.vo

import scala.beans.BeanProperty

case class ReportInfo(
  @BeanProperty reportId: Long,
  @BeanProperty reportName: String,
  @BeanProperty templateFile: String = ""
)

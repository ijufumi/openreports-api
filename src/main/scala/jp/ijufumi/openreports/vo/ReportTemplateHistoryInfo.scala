package jp.ijufumi.openreports.vo

import java.time.LocalDateTime

import scala.beans.BeanProperty

case class ReportTemplateHistoryInfo(
  @BeanProperty historyId: Long,
  @BeanProperty templateId: Long,
  @BeanProperty fileName: String,
  @BeanProperty createdAt: LocalDateTime
)

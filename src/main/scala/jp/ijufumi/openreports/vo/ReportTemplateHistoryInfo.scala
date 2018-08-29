package jp.ijufumi.openreports.vo

import org.joda.time.DateTime

import scala.beans.BeanProperty

case class ReportTemplateHistoryInfo(
  @BeanProperty historyId: Long,
  @BeanProperty templateId: Long,
  @BeanProperty fileName: String,
  @BeanProperty createdAt: DateTime
)

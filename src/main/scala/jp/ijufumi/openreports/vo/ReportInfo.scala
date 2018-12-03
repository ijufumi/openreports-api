package jp.ijufumi.openreports.vo

import java.time.LocalDateTime

import scala.beans.BeanProperty

case class ReportInfo(@BeanProperty reportId: Long,
                      @BeanProperty reportName: String,
                      @BeanProperty description: String,
                      @BeanProperty templateId: Long,
                      @BeanProperty createdAt: LocalDateTime,
                      @BeanProperty updatedAt: LocalDateTime,
                      @BeanProperty versions: Long,
                      @BeanProperty templateFile: ReportTemplateInfo)

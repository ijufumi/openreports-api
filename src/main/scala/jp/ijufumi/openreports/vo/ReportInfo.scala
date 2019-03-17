package jp.ijufumi.openreports.vo

import java.time.LocalDateTime

import scala.beans.BeanProperty

case class ReportInfo(@BeanProperty reportId: Long = 0,
                      @BeanProperty reportName: String,
                      @BeanProperty description: String,
                      @BeanProperty templateId: Long,
                      @BeanProperty groups: Seq[Long],
                      @BeanProperty createdAt: LocalDateTime = LocalDateTime.now,
                      @BeanProperty updatedAt: LocalDateTime = LocalDateTime.now,
                      @BeanProperty versions: Long = 0)

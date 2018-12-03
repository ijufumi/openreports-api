package jp.ijufumi.openreports.vo

import org.joda.time.DateTime

import scala.beans.BeanProperty

case class ReportInfo(@BeanProperty reportId: Long,
                      @BeanProperty reportName: String,
                      @BeanProperty description: String,
                      @BeanProperty templateId: Long,
                      @BeanProperty createdAt: DateTime,
                      @BeanProperty updatedAt: DateTime,
                      @BeanProperty versions: Long,
                      @BeanProperty templateFile: ReportTemplateInfo)

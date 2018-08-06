package jp.ijufumi.openreports.vo

import scala.beans.BeanProperty

case class ReportTemplateInfo(@BeanProperty templateId: Long,
                              @BeanProperty fileName: String)

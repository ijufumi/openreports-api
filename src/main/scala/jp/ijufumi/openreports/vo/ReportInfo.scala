package jp.ijufumi.openreports.vo

import scala.beans.BeanProperty

case class ReportInfo(@BeanProperty reportId: Long,
                      @BeanProperty reportName: String,
                      @BeanProperty description: String,
                      @BeanProperty templateFile: String = "")

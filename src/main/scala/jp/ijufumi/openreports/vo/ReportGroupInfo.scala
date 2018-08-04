package jp.ijufumi.openreports.vo

import scala.beans.BeanProperty

case class ReportGroupInfo(@BeanProperty reportGroupId: Long,
                           @BeanProperty reportGroupName: String)

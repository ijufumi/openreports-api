package jp.ijufumi.openreports.vo

import java.time.LocalDateTime

import scala.beans.BeanProperty

case class ReportGroupInfo(@BeanProperty reportGroupId: Long,
                           @BeanProperty reportGroupName: String,
                           @BeanProperty createdAt: LocalDateTime,
                           @BeanProperty updatedAt: LocalDateTime,
                           @BeanProperty versions: Long = 0)

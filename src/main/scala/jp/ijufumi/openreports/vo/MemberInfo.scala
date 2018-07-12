package jp.ijufumi.openreports.vo

import scala.beans.BeanProperty

case class MemberInfo(
  @BeanProperty memberId: Long,
  @BeanProperty name: String,
  @BeanProperty groups: Seq[Long],
  @BeanProperty menus: Seq[Long]
)

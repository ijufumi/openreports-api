package jp.ijufumi.openreports.vo

import scala.beans.BeanProperty

case class GroupInfo(
    @BeanProperty groupId: Long,
    @BeanProperty groupName: String,
    @BeanProperty versions: Long,
    @BeanProperty isBelong: Boolean
) {

  def this(
      groupId: Long,
      groupName: String,
      versions: Long
  ) = {
    this(groupId, groupName, versions, false)
  }
}

object GroupInfo {
  def apply(
      groupId: Long,
      groupName: String,
      versions: Long
  ): GroupInfo = {
    GroupInfo(groupId, groupName, versions, false)
  }
}

package jp.ijufumi.openreports.vo
import scala.beans.BeanProperty

case class GroupInfo(@BeanProperty groupId: Long,
                     @BeanProperty groupName: String,
                    @BeanProperty isBelong: Boolean) {

  def this(groupId: Long, groupName: String) = {
    this(groupId, groupName, false)
  }
}

object GroupInfo {
  def apply(groupId: Long, groupName: String): GroupInfo = {
    GroupInfo(groupId, groupName, false)
  }
}

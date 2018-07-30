package jp.ijufumi.openreports.vo
import scala.beans.BeanProperty

case class GroupInfo(@BeanProperty groupId: Long,
                     @BeanProperty groupName: String)

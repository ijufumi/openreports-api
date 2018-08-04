package jp.ijufumi.openreports.service.settings

import java.sql.SQLException

import jp.ijufumi.openreports.model.TGroup
import jp.ijufumi.openreports.service.enums.StatusCode
import jp.ijufumi.openreports.vo.GroupInfo
import org.joda.time.DateTime
import skinny.Logging

class GroupSettingsService extends Logging {
  def getGroups(): Seq[GroupInfo] = {
    TGroup.findAll().map(g => GroupInfo(g.groupId, g.groupName, g.versions))
  }

  def getGroup(groupId: Long): Option[GroupInfo] = {
    TGroup
      .findById(groupId)
      .map(g => GroupInfo(g.groupId, g.groupName, g.versions))
  }

  def registerGroup(groupName: String): StatusCode.Value = {
    TGroup.createWithAttributes('groupName -> groupName)
    StatusCode.OK
  }

  def updateGroup(groupId: Long,
                  groupName: String,
                  versions: Long): StatusCode.Value = {

    val groupOpt = TGroup.findById(groupId)
    if (groupOpt.isEmpty) {
      return StatusCode.DATA_NOT_FOUND
    }
    val group = groupOpt.get
    val updateBuilder = TGroup.updateByIdAndVersion(groupId, versions)
    if (!groupName.equals(group.groupName)) {
      updateBuilder.withAttributes('groupNme -> groupName)
    }

    try {
      updateBuilder.withAttributes('updatedAt -> DateTime.now)
    } catch {
      case e: SQLException => return StatusCode.of(e)
      case _: Throwable    => return StatusCode.OTHER_ERROR
    }

    StatusCode.OK
  }
}

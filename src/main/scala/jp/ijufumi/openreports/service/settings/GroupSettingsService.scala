package jp.ijufumi.openreports.service.settings

import java.sql.SQLException

import jp.ijufumi.openreports.model.TGroup
import jp.ijufumi.openreports.service.enums.StatusCode
import jp.ijufumi.openreports.vo.GroupInfo
import org.joda.time.DateTime
import skinny.Logging

class GroupSettingsService extends Logging {
  def getGroups: Array[GroupInfo] = {
    TGroup.findAll().map(g => GroupInfo(g.groupId, g.groupName, g.versions)).toArray
  }

  def getGroup(groupId: Long): Option[GroupInfo] = {
    TGroup
      .findById(groupId)
      .map(g => GroupInfo(g.groupId, g.groupName, g.versions))
  }

  def registerGroup(groupName: String): StatusCode.Value = {
    TGroup.createWithAttributes("groupName" -> groupName)
    StatusCode.OK
  }

  def updateGroup(groupId: Long,
                  groupName: String,
                  versions: Long): StatusCode.Value = {

    val groupOpt = TGroup.findById(groupId)
    if (groupOpt.isEmpty) {
      return StatusCode.DATA_NOT_FOUND
    }
    try {
      TGroup.updateByIdAndVersion(groupId, versions)
        .withAttributes(
          "groupName" -> groupName,
          "updatedAt" -> DateTime.now
        )
    } catch {
      case e: SQLException =>
        logger.error(e)
        return StatusCode.of(e)
      case e: Throwable    =>
        logger.error(e)
        return StatusCode.OTHER_ERROR
    }

    StatusCode.OK
  }
}

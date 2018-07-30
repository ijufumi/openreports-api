package jp.ijufumi.openreports.service
import jp.ijufumi.openreports.model.TGroup
import jp.ijufumi.openreports.vo.GroupInfo
import skinny.LoggerProvider

class GroupSettingsService extends LoggerProvider {
  def getGroups(): Seq[GroupInfo] = {
    TGroup.findAll().map(g => GroupInfo(g.groupId, g.groupName)).toSeq
  }
}

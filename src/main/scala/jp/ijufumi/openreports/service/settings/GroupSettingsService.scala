package jp.ijufumi.openreports.service.settings

import jp.ijufumi.openreports.model.TGroup
import jp.ijufumi.openreports.vo.GroupInfo
import skinny.Logging

class GroupSettingsService extends Logging {
  def getGroups(): Seq[GroupInfo] = {
    TGroup.findAll().map(g => GroupInfo(g.groupId, g.groupName))
  }
}

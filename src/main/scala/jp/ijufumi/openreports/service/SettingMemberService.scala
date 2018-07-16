package jp.ijufumi.openreports.service

import jp.ijufumi.openreports.model.TMember
import jp.ijufumi.openreports.vo.MemberInfo
import skinny.LoggerProvider

class SettingMemberService extends LoggerProvider {
  def memberList(): Seq[MemberInfo] = {
    TMember.findAll().map(m => MemberInfo(m))
  }

  def register(name: String, emailAddress: String, password: String, isAdmin: Boolean): Unit = {
    TMember.createWithAttributes('emailAddress -> emailAddress, 'passowrd -> password, 'name -> name)
  }
}

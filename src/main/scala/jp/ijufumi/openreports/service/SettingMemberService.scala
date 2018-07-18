package jp.ijufumi.openreports.service

import jp.ijufumi.openreports.model.TMember
import jp.ijufumi.openreports.vo.MemberInfo
import skinny.LoggerProvider

class SettingMemberService extends LoggerProvider {
  def getMembers(): Seq[MemberInfo] = {
    TMember.findAll().map(m => MemberInfo(m))
  }

  def getMember(memberId: Long): Option[MemberInfo] = {
    TMember.findById(memberId).map(m => MemberInfo(m))
  }

  def registerMember(name: String,
                     emailAddress: String,
                     password: String,
                     isAdmin: Boolean): Unit = {
    TMember.createWithAttributes(
      'emailAddress -> emailAddress,
      'passowrd -> password,
      'name -> name
    )
  }

  def updateMember(memberId: Long,
                   name: String,
                   emailAddress: String,
                   password: String,
                   isAdmin: Boolean,
                   versions: Long): Unit = {
    TMember
      .updateByIdAndVersion(memberId, versions)
      .withAttributes(
        'emailAddress -> emailAddress,
        'passowrd -> password,
        'name -> name
      )
  }
}

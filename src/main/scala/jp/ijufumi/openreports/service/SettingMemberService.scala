package jp.ijufumi.openreports.service

import java.sql.SQLException

import jp.ijufumi.openreports.model.TMember
import jp.ijufumi.openreports.service.enums.StatusCode
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
                     isAdmin: Boolean): StatusCode.Value = {

    try {
      TMember.createWithAttributes(
        'emailAddress -> emailAddress,
        'password -> password,
        'name -> name
      )

    } catch {
      case e: SQLException => return StatusCode.of(e)
      case _               => return StatusCode.OTHER_ERROR
    }
    StatusCode.OK
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
        'password -> password,
        'name -> name
      )
  }
}

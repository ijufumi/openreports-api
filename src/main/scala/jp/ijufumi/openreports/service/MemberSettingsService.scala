package jp.ijufumi.openreports.service

import java.sql.SQLException

import jp.ijufumi.openreports.model.{RMemberGroup, TMember}
import jp.ijufumi.openreports.service.enums.StatusCode
import jp.ijufumi.openreports.service.support.Hash
import jp.ijufumi.openreports.vo.MemberInfo
import org.joda.time.DateTime
import scalikejdbc.SQLSyntax
import skinny.LoggerProvider

class MemberSettingsService extends LoggerProvider {
  def getMembers(): Seq[MemberInfo] = {
    TMember.findAll().map(m => MemberInfo(m))
  }

  def getMember(memberId: Long): Option[MemberInfo] = {
    TMember.findById(memberId).map(m => MemberInfo(m))
  }

  def registerMember(name: String,
                     emailAddress: String,
                     password: String,
                     isAdmin: Boolean,
                     groups: Seq[String]): StatusCode.Value = {

    try {
      val id = TMember.createWithAttributes(
        'emailAddress -> emailAddress,
        'password -> Hash.hmacSha256(HASHED_KEY, password),
        'name -> name
      )
      groups.foreach(s =>
        RMemberGroup.createWithAttributes('memberId -> id, 'groupId -> s)
      )
    } catch {
      case e: SQLException => return StatusCode.of(e)
      case _: Throwable    => return StatusCode.OTHER_ERROR
    }
    StatusCode.OK
  }

  def updateMember(memberId: Long,
                   name: String,
                   emailAddress: String,
                   password: String,
                   isAdmin: Boolean,
                   groups: Seq[String],
                   versions: Long): StatusCode.Value = {
    try {
      val memberOpt = TMember.findById(memberId)
      if (memberOpt.isEmpty) {
        return StatusCode.DATA_NOT_FOUND
      }

      val updateBuilder = TMember
        .updateByIdAndVersion(memberId, versions)

      val member = memberOpt.get
      if (name.length != 0 && !name.equals(member.name)) {
        updateBuilder.addAttributeToBeUpdated(
          (TMember.column.field("name"), name)
        )
      }
      if (emailAddress.length != 0 && !emailAddress.equals(member.emailAddress)) {
        updateBuilder.addAttributeToBeUpdated(
          (TMember.column.field("emailAddress"), emailAddress)
        )
      }
      if (password.length != 0) {
        val hashedPassword = Hash.hmacSha256(HASHED_KEY, password)
        if (!hashedPassword.equals(member.password)) {
          updateBuilder.addAttributeToBeUpdated(
            (TMember.column.field("password"), hashedPassword)
          )
        }
      }

      updateBuilder.withAttributes('updatedAt -> DateTime.now)

      RMemberGroup.deleteBy(SQLSyntax.eq(RMemberGroup.column.field("memberId"), memberId))

      groups.foreach(s =>
        RMemberGroup.createWithAttributes('memberId -> memberId, 'groupId -> s)
      )
    } catch {
      case e: SQLException => return StatusCode.of(e)
      case _: Throwable    => return StatusCode.OTHER_ERROR
    }
    StatusCode.OK
  }
}

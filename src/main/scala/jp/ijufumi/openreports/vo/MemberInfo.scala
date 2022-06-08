package jp.ijufumi.openreports.vo

import jp.ijufumi.openreports.model.TMember

import scala.beans.BeanProperty

case class MemberInfo(@BeanProperty memberId: Long,
                      @BeanProperty name: String,
                      @BeanProperty emailAddress: String,
                      @BeanProperty accessToken: String,
                      @BeanProperty versions: Long) {

  def this(member: TMember, accessToken: String) = {
    this(
      member.memberId,
      member.name,
      member.emailAddress,
      accessToken,
      member.versions
    )
  }
}

object MemberInfo {
  def apply(member: TMember, accessToken: String): MemberInfo = {
    new MemberInfo(member, accessToken)
  }
}

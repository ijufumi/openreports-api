package jp.ijufumi.openreports.vo

case class MemberInfo(memberId: Long,
                      name: String,
                      groups: Seq[Long],
                      menus: Seq[Long]
                     )

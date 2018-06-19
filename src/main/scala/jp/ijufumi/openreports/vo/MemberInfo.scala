package jp.ijufumi.openreports.vo

import scala.collection.immutable.Set

case class MemberInfo(memberId: Long,
                      name: String,
                      groups: Set[Long],
                      menus: Set[Long])

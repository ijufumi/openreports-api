package jp.ijufumi.openreports.model

import skinny.SkinnyJoinTable

case class RGroupFunction(
    groupId: Long,
    functionId: Long
)

object RGroupFunction extends SkinnyJoinTable[RGroupFunction] {
  override def tableName = "r_group_function"

  override def defaultAlias = createAlias("grp_fun")
}

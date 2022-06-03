package jp.ijufumi.openreports.model

import java.time.LocalDateTime

import scalikejdbc.{ResultName, WrappedResultSet}
import skinny.orm.SkinnyCRUDMapper
import skinny.orm.feature.OptimisticLockWithVersionFeature

case class TMember(memberId: Long = 0,
                   emailAddress: String,
                   password: String,
                   name: String,
                   isAdmin: String = "0",
                   createdAt: LocalDateTime = LocalDateTime.now(),
                   updatedAt: LocalDateTime = LocalDateTime.now(),
                   versions: Long,
                   groups: Seq[TGroup] = Nil)

object TMember
    extends SkinnyCRUDMapper[TMember]
    with OptimisticLockWithVersionFeature[TMember] {

  override def tableName = "t_member"

  override def defaultAlias = createAlias("mem")

  override def primaryKeyFieldName = "memberId"

  override def lockVersionFieldName: String = "versions"

  override def extract(rs: WrappedResultSet, n: ResultName[TMember]): TMember =
    new TMember(
      memberId = rs.get(n.memberId),
      emailAddress = rs.get(n.emailAddress),
      password = rs.get(n.password),
      name = rs.get(n.name),
      isAdmin = rs.get(n.isAdmin),
      createdAt = rs.get(n.createdAt),
      updatedAt = rs.get(n.updatedAt),
      versions = rs.get(n.versions)
    )

  // TODO: change to lazy loading.
  hasManyThroughWithFk[TGroup](
    through = RMemberGroup,
    many = TGroup,
    throughFk = "memberId",
    manyFk = "groupId",
    merge = (m, groups) => m.copy(groups = groups)
  ).byDefault
}

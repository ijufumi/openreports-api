package jp.ijufumi.openreports.model

import org.joda.time.DateTime
import scalikejdbc.{ResultName, WrappedResultSet}
import skinny.orm.SkinnyCRUDMapper

case class Member(memberId: Long, emailAddress: String, password: String, isAdmin: Char, createdAt: DateTime, updatedAt: DateTime)

object Member extends SkinnyCRUDMapper[Member] {
  override def tableName = "t_member"

  override def defaultAlias = createAlias("mem")

  override def primaryKeyFieldName = "member_id"

  override def extract(rs: WrappedResultSet, n: ResultName[Member]): Member = new Member(
    memberId = rs.get(n.memberId),
    emailAddress = rs.get(n.emailAddress),
    password = rs.get(n.password),
    isAdmin = rs.get(n.isAdmin),
    createdAt = rs.get(n.createdAt),
    updatedAt = rs.get(n.updatedAt)
  )
}


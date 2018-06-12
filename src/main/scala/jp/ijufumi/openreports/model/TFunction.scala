package jp.ijufumi.openreports.model

import org.joda.time.DateTime
import scalikejdbc.{ ResultName, WrappedResultSet }
import skinny.orm.SkinnyCRUDMapper

case class TFunction(
  functionId: Long,
  functionName: String,
  createdAt: DateTime,
  updatedAt: DateTime,
)

object TFunction extends SkinnyCRUDMapper[TFunction] {
  override def tableName = "t_member"

  override def defaultAlias = createAlias("mem")

  override def primaryKeyFieldName = "member_id"

  override def extract(rs: WrappedResultSet, n: ResultName[TFunction]): TFunction = new TFunction(
    functionId = rs.get(n.functionId),
    functionName = rs.get(n.functionName),
    createdAt = rs.get(n.createdAt),
    updatedAt = rs.get(n.updatedAt)
  )
}
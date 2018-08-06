package jp.ijufumi.openreports.model

import org.joda.time.DateTime
import scalikejdbc.{ResultName, WrappedResultSet}
import skinny.orm.SkinnyCRUDMapper

case class TFunction(functionId: Long,
                     functionName: String,
                     createdAt: DateTime,
                     updatedAt: DateTime,
                     groups: Seq[TGroup] = Nil)

object TFunction extends SkinnyCRUDMapper[TFunction] {
  lazy val groups = hasManyThroughWithFk[TGroup](
    through = RGroupFunction,
    many = TGroup,
    throughFk = "functionId",
    manyFk = "groupId",
    merge = (f, groups) => f.copy(groups = groups)
  ).includes[TGroup](
    (gf, groups) =>
      gf.map { g =>
        g.copy(
          groups =
            groups.filter(_.functions.exists(_.functionId == g.functionId))
        )
    }
  )

  override def tableName = "t_function"

  override def defaultAlias = createAlias("fun")

  override def primaryKeyFieldName = "functionId"

  override def extract(rs: WrappedResultSet,
                       n: ResultName[TFunction]): TFunction = new TFunction(
    functionId = rs.get(n.functionId),
    functionName = rs.get(n.functionName),
    createdAt = rs.get(n.createdAt),
    updatedAt = rs.get(n.updatedAt)
  )
}

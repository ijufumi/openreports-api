package jp.ijufumi.openreports.entities.enums

import slick.ast.BaseTypedType
import slick.jdbc.JdbcType
import slick.jdbc.PostgresProfile.api._


object JdbcDriverClasses extends Enumeration {
  type JdbcDriverClass = Value

  val MySQL: JdbcDriverClass = Value("com.mysql.jdbc.Driver")

  implicit val storageTypeMapper: JdbcType[JdbcDriverClass] with BaseTypedType[JdbcDriverClass] =
    MappedColumnType.base[JdbcDriverClass, String](
      e => e.toString,
      s => JdbcDriverClasses.withName(s),
    )
}

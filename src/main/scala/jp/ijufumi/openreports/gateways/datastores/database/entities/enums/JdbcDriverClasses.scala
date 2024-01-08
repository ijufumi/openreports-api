package jp.ijufumi.openreports.gateways.datastores.database.entities.enums

import slick.ast.BaseTypedType
import slick.jdbc.JdbcType
import slick.jdbc.PostgresProfile.api._

object JdbcDriverClasses extends Enumeration {
  type JdbcDriverClass = Value

  val Postgres: JdbcDriverClass = Value("org.postgresql.Driver")
  val MySQL: JdbcDriverClass = Value("com.mysql.jdbc.Driver")

  implicit val storageTypeMapper: JdbcType[JdbcDriverClass] with BaseTypedType[JdbcDriverClass] =
    MappedColumnType.base[JdbcDriverClass, String](
      e => e.toString,
      s => JdbcDriverClasses.withName(s),
    )
}

object DBNameMappings {
  private val mapping = Map(
    "MySQL" -> JdbcDriverClasses.MySQL,
    "Postgres" -> JdbcDriverClasses.Postgres,
  )

  def getDbNameByDriverClass(driverClass: JdbcDriverClasses.JdbcDriverClass): String = {
    mapping.find(v => v._2 == driverClass).map(v => v._1).orNull
  }
}

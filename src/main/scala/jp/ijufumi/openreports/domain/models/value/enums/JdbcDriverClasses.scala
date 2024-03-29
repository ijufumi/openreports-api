package jp.ijufumi.openreports.domain.models.value.enums

object JdbcDriverClasses extends Enumeration {
  type JdbcDriverClass = Value

  val Postgres: JdbcDriverClass = Value("org.postgresql.Driver")
  val MySQL: JdbcDriverClass = Value("com.mysql.jdbc.Driver")

}

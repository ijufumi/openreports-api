package jp.ijufumi.openreports.models.value.enums

object DBNames extends Enumeration {
  val Postgres = Value(JdbcDriverClasses.Postgres.toString)
  val MySQL = Value(JdbcDriverClasses.MySQL.toString)

  def getDbNameByDriverClass(driverClass: JdbcDriverClasses.JdbcDriverClass): String = {
    values.map(v => v.toString).find(v => v.equals(driverClass.toString)).orNull
  }
}

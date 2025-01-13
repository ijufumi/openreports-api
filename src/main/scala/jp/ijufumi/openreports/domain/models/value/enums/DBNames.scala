package jp.ijufumi.openreports.domain.models.value.enums

object DBNames extends Enumeration {
  val Postgres = Value("Postgres")
  val MySQL = Value("MySQL")

  def getDbNameByDriverClass(driverClass: JdbcDriverClasses.JdbcDriverClass): String = {
    driverClass match {
      case JdbcDriverClasses.Postgres => Postgres.toString
      case JdbcDriverClasses.MySQL => MySQL.toString
      case _ => throw new RuntimeException()("%s was not supported".format(driverClass))
    }
  }
}

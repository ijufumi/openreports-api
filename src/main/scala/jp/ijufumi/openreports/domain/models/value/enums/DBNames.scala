package jp.ijufumi.openreports.domain.models.value.enums

object DBNames extends Enumeration {
  type DBName = Value

  val Postgres = Value("Postgres")
  val MySQL = Value("MySQL")

  def getDbNameByDriverClass(driverClass: JdbcDriverClasses.JdbcDriverClass): DBName = {
    driverClass match {
      case JdbcDriverClasses.Postgres => Postgres
      case JdbcDriverClasses.MySQL    => MySQL
      case _ => throw new RuntimeException("%s was not supported".format(driverClass))
    }
  }
}

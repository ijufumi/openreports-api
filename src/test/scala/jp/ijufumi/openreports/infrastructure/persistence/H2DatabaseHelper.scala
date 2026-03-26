package jp.ijufumi.openreports.infrastructure.persistence

import slick.jdbc.PostgresProfile.api._
import slick.jdbc.JdbcBackend.Database

import scala.concurrent.Await
import scala.concurrent.duration._

/** Helper for creating and managing H2 in-memory databases for testing Uses PostgresProfile for
  * compatibility with main code
  */
object H2DatabaseHelper {

  /** Creates an in-memory H2 database for testing
    */
  def createDatabase(name: String = "test"): Database = {
    Database.forURL(
      url = s"jdbc:h2:mem:$name;MODE=PostgreSQL;DATABASE_TO_UPPER=false;DB_CLOSE_DELAY=-1",
      driver = "org.h2.Driver",
      keepAliveConnection = true,
    )
  }

  /** Creates database schema for the given tables
    */
  def createSchema(db: Database, tables: TableQuery[_ <: Table[_]]*): Unit = {
    val schema = tables.map(_.schema).reduce(_ ++ _)
    Await.result(db.run(schema.create), 10.seconds)
  }

  /** Drops database schema for the given tables
    */
  def dropSchema(db: Database, tables: TableQuery[_ <: Table[_]]*): Unit = {
    val schema = tables.map(_.schema).reduce(_ ++ _)
    Await.result(db.run(schema.drop), 10.seconds)
  }

  /** Truncates all tables
    */
  def truncateTables(db: Database, tables: TableQuery[_ <: Table[_]]*): Unit = {
    tables.foreach { table =>
      Await.result(db.run(table.delete), 10.seconds)
    }
  }

  /** Closes the database connection
    */
  def closeDatabase(db: Database): Unit = {
    db.close()
  }
}

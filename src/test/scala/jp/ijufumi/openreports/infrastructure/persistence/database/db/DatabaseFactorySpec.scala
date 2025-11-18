package jp.ijufumi.openreports.infrastructure.persistence.database.db

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class DatabaseFactorySpec extends AnyFlatSpec with Matchers {

  "DatabaseFactory" should "be accessible as a singleton object" in {
    noException should be thrownBy DatabaseFactory
  }

  // Note: The following tests require actual database connections
  // They should be implemented as integration tests with test databases
  // For proper testing, consider:
  // 1. Using an in-memory H2 database for testing
  // 2. Using testcontainers with PostgreSQL
  // 3. Creating a separate test configuration with test database
  // 4. Mocking the database configuration

  /*
  "createDatabase" should "create database with default name" in {
    val database = DatabaseFactory.createDatabase()

    database should not be null
    noException should be thrownBy database.close()
  }

  it should "create database with custom name" in {
    val database = DatabaseFactory.createDatabase("postgres")

    database should not be null
    noException should be thrownBy database.close()
  }

  it should "throw exception for invalid database name" in {
    assertThrows[Exception] {
      DatabaseFactory.createDatabase("invalid-db-name")
    }
  }

  "createDatabaseConfig" should "create database config with default name" in {
    val databaseConfig = DatabaseFactory.createDatabaseConfig()

    databaseConfig should not be null
    databaseConfig.db should not be null
    databaseConfig.profile should not be null

    noException should be thrownBy databaseConfig.db.close()
  }

  it should "create database config with custom name" in {
    val databaseConfig = DatabaseFactory.createDatabaseConfig("postgres")

    databaseConfig should not be null
    databaseConfig.db should not be null
    databaseConfig.profile should not be null

    noException should be thrownBy databaseConfig.db.close()
  }

  it should "use HikariCP connection pool" in {
    val database = DatabaseFactory.createDatabase()

    // Verify that the database is using HikariCP
    // This would require access to internal implementation details
    database should not be null
    database.close()
  }

  it should "configure connection pool with correct parameters" in {
    val databaseConfig = DatabaseFactory.createDatabaseConfig()

    // Verify pool configuration (requires access to internal config)
    databaseConfig should not be null
    // Check numThreads = 5
    // Check poolSize = 20
    // Check keepAliveConnection = true

    databaseConfig.db.close()
  }

  "DatabaseFactory" should "create multiple database instances independently" in {
    val db1 = DatabaseFactory.createDatabase()
    val db2 = DatabaseFactory.createDatabase()

    db1 should not be null
    db2 should not be null
    db1 should not be theSameInstanceAs(db2)

    db1.close()
    db2.close()
  }

  it should "use environment variables for database configuration" in {
    val database = DatabaseFactory.createDatabase()

    // The database should be configured using environment variables:
    // DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD
    database should not be null

    // Verify connection works
    import scala.concurrent.Await
    import scala.concurrent.duration._
    import slick.jdbc.PostgresProfile.api._

    val testQuery = sql"SELECT 1".as[Int]
    val result = Await.result(database.run(testQuery), 5.seconds)

    result should not be empty
    result.head should equal(1)

    database.close()
  }
  */
}

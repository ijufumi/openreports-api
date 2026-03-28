package jp.ijufumi.openreports.infrastructure.persistence.database.pool

import jp.ijufumi.openreports.domain.models.value.enums.JdbcDriverClasses
import jp.ijufumi.openreports.exceptions.NotFoundException
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ConnectionPoolSpec extends AnyFlatSpec with Matchers {

  "ConnectionPool" should "be accessible as a singleton object" in {
    noException should be thrownBy ConnectionPool
  }

  // Note: The following tests require actual database connections
  // They should be implemented as integration tests with test databases
  // For proper testing, consider:
  // 1. Using an in-memory H2 database for testing
  // 2. Using testcontainers with PostgreSQL
  // 3. Creating a separate test configuration with test database
  // 4. Implementing proper cleanup to avoid connection leaks

  /*
  "newConnection with parameters" should "create new connection pool" in {
    val name = "test-pool-1"
    val username = "test_user"
    val password = "test_password"
    val url = "jdbc:postgresql://localhost:5432/test_db"
    val driverClass = JdbcDriverClasses.PostgreSQL
    val maxPoolSize = 5

    val connection = ConnectionPool.newConnection(
      name = name,
      username = username,
      password = password,
      url = url,
      jdbcDriverClass = driverClass,
      maxPoolSize = maxPoolSize
    )

    connection should not be null
    connection.isClosed should be(false)
  }

  it should "return existing connection for same pool name" in {
    val name = "test-pool-2"
    val username = "test_user"
    val password = "test_password"
    val url = "jdbc:postgresql://localhost:5432/test_db"
    val driverClass = JdbcDriverClasses.PostgreSQL
    val maxPoolSize = 5

    val connection1 = ConnectionPool.newConnection(
      name = name,
      username = username,
      password = password,
      url = url,
      jdbcDriverClass = driverClass,
      maxPoolSize = maxPoolSize
    )

    val connection2 = ConnectionPool.newConnection(
      name = name,
      username = username,
      password = password,
      url = url,
      jdbcDriverClass = driverClass,
      maxPoolSize = maxPoolSize
    )

    connection1 should not be null
    connection2 should not be null
    // Both connections should be from the same pool
  }

  "newConnection with name only" should "return connection from existing pool" in {
    val name = "test-pool-3"
    val username = "test_user"
    val password = "test_password"
    val url = "jdbc:postgresql://localhost:5432/test_db"
    val driverClass = JdbcDriverClasses.PostgreSQL
    val maxPoolSize = 5

    // Create the pool first
    ConnectionPool.newConnection(
      name = name,
      username = username,
      password = password,
      url = url,
      jdbcDriverClass = driverClass,
      maxPoolSize = maxPoolSize
    )

    // Get connection from existing pool
    val connection = ConnectionPool.newConnection(name)

    connection should not be null
    connection.isClosed should be(false)
  }

  it should "throw NotFoundException when pool doesn't exist" in {
    assertThrows[NotFoundException] {
      ConnectionPool.newConnection("non-existent-pool")
    }
  }

  "ConnectionPool" should "support multiple named pools" in {
    val pool1Name = "test-pool-multi-1"
    val pool2Name = "test-pool-multi-2"
    val username = "test_user"
    val password = "test_password"
    val url = "jdbc:postgresql://localhost:5432/test_db"
    val driverClass = JdbcDriverClasses.PostgreSQL
    val maxPoolSize = 5

    val connection1 = ConnectionPool.newConnection(
      name = pool1Name,
      username = username,
      password = password,
      url = url,
      jdbcDriverClass = driverClass,
      maxPoolSize = maxPoolSize
    )

    val connection2 = ConnectionPool.newConnection(
      name = pool2Name,
      username = username,
      password = password,
      url = url,
      jdbcDriverClass = driverClass,
      maxPoolSize = maxPoolSize
    )

    connection1 should not be null
    connection2 should not be null
  }

  it should "be thread-safe for concurrent access" in {
    val poolName = "test-pool-concurrent"
    val username = "test_user"
    val password = "test_password"
    val url = "jdbc:postgresql://localhost:5432/test_db"
    val driverClass = JdbcDriverClasses.PostgreSQL
    val maxPoolSize = 10

    import scala.concurrent.{Await, Future}
    import scala.concurrent.ExecutionContext.Implicits.global
    import scala.concurrent.duration._

    val futures = (1 to 10).map { _ =>
      Future {
        ConnectionPool.newConnection(
          name = poolName,
          username = username,
          password = password,
          url = url,
          jdbcDriverClass = driverClass,
          maxPoolSize = maxPoolSize
        )
      }
    }

    val connections = Await.result(Future.sequence(futures), 10.seconds)

    connections should have length 10
    connections.foreach { conn =>
      conn should not be null
      conn.isClosed should be(false)
    }
  }
   */
}

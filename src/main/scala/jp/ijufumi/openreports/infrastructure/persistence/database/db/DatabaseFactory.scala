package jp.ijufumi.openreports.infrastructure.persistence.database.db

import com.typesafe.config.ConfigFactory
import jp.ijufumi.openreports.configs.Config._
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.JdbcProfile

import scala.jdk.CollectionConverters._

object DatabaseFactory {
  private val config = ConfigFactory.parseMap(
    Map(
      "postgres" -> Map(
        "url" -> f"jdbc:postgresql://$DB_HOST%s:$DB_PORT%s/$DB_NAME%s",
        "driver" -> "org.postgresql.Driver",
        "username" -> DB_USER,
        "password" -> DB_PASSWORD,
        "connectionPool" -> "HikariCP",
        "numThreads" -> DB_NUM_THREADS,
        "poolSize" -> DB_POOL_SIZE,
        "keepAliveConnection" -> DB_KEEP_ALIVE_CONNECTION,
      ).asJava,
    ).asJava,
  )

  def createDatabase(name: String = "postgres"): Database = {
    Database.forConfig(name, config)
  }

  def createDatabaseConfig(name: String = "postgres"): DatabaseConfig[JdbcProfile] = {
    DatabaseConfig.forConfig(name, config)
  }
}

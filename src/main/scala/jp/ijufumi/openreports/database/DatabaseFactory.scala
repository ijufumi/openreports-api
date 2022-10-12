package jp.ijufumi.openreports.database

import com.typesafe.config.ConfigFactory
import slick.jdbc.JdbcBackend.Database

import scala.jdk.CollectionConverters.mapAsJavaMap
import jp.ijufumi.openreports.config.Config._
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

object DatabaseFactory {
  private val config = ConfigFactory.parseMap(
    mapAsJavaMap(
      Map(
        "postgres" -> mapAsJavaMap(
          Map(
            "url" -> f"jdbc:postgresql://$DB_HOST%s:$DB_PORT%s/$DB_NAME%s",
            "driver" -> "org.postgresql.Driver",
            "username" -> DB_USER,
            "password" -> DB_PASSWORD,
            "connectionPool" -> "HikariCP",
            "numThreads" -> 5,
            "poolSize" -> 20,
            "keepAliveConnection" -> true,
          ),
        ),
      ),
    ),
  )

  def createDatabase(name: String = "postgres"): Database = {
    Database.forConfig(name, config)
  }

  def createDatabaseConfig(name: String = "postgres"): DatabaseConfig[JdbcProfile] = {
    DatabaseConfig.forConfig(name, config)
  }
}

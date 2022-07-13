package jp.ijufumi.openreports.database

import com.typesafe.config.ConfigFactory
import slick.jdbc.JdbcBackend.Database
import scala.jdk.CollectionConverters.MapHasAsJava
import jp.ijufumi.openreports.config.Config._

object DatabaseFactory {
  private val config = ConfigFactory.parseMap(
    Map(
      "postgres" -> Map(
        "url" -> f"jdbc:postgresql://$DB_HOST%s:$DB_PORT%s/$DB_NAME%s",
        "driver" -> "org.postgresql.Driver",
        "username" -> DB_USER,
        "password" -> DB_PASSWORD,
        "connectionPool" -> "HikariCP",
        "numThreads" -> 5,
        "poolSize" -> 20,
        "keepAliveConnection" -> true,
      ).asJava,
    ).asJava,
  )

  def createDatabase(name: String = "postgres"): Database = {
    Database.forConfig(name, config)
  }
}

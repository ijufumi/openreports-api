package jp.ijufumi.openreports.database

import com.typesafe.config.ConfigFactory
import slick.jdbc.JdbcBackend.Database
import scala.jdk.CollectionConverters.MapHasAsJava

object DatabaseFactory {
  private val dbHost: String = sys.env.getOrElse("DB_HOST", "localhost")
  private val dbName: String = sys.env.getOrElse("DB_NAME", "openreports")
  private val dbUser: String = sys.env.getOrElse("DB_USER", "postgres")
  private val dbPassword: String = sys.env.getOrElse("DB_PASSWORD", "password")
  private val dbPort: String = sys.env.getOrElse("DB_PORT", "5432")

  private val config = ConfigFactory.parseMap(
    Map(
      "postgres" -> Map(
        "url" -> f"jdbc:postgresql://$dbHost%s:$dbPort%s/$dbName%s",
        "driver" -> "org.postgresql.Driver",
        "username" -> dbUser,
        "password" -> dbPassword,
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

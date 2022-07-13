package jp.ijufumi.openreports.config

object Config {
  // for database
  val DB_HOST: String = sys.env.getOrElse("DB_HOST", "localhost")
  val DB_NAME: String = sys.env.getOrElse("DB_NAME", "openreports")
  val DB_USER: String = sys.env.getOrElse("DB_USER", "postgres")
  val DB_PASSWORD: String = sys.env.getOrElse("DB_PASSWORD", "password")
  val DB_PORT: String = sys.env.getOrElse("DB_PORT", "5432")
  // for hash
  val HASH_KEY: String = sys.env.getOrElse("HASH_KEY", "test")
}

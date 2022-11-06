package jp.ijufumi.openreports.config

import io.github.cdimascio.dotenv.DotenvBuilder

object Config {
  private val dotEnv = new DotenvBuilder().ignoreIfMissing().ignoreIfMissing().load()

  private def getEnvValue(key: String, defaultValue: String = ""): String = {
    dotEnv.get(key, sys.env.getOrElse(key, defaultValue))
  }

  val FRONTEND_URL: String = getEnvValue("FRONTEND_URL", "http://localhost:3000")

  // for database
  val DB_HOST: String = getEnvValue("DB_HOST", "localhost")
  val DB_NAME: String = getEnvValue("DB_NAME", "openreports")
  val DB_USER: String = getEnvValue("DB_USER", "postgres")
  val DB_PASSWORD: String = getEnvValue("DB_PASSWORD", "password")
  val DB_PORT: String = getEnvValue("DB_PORT", "5432")
  // for hash
  val HASH_KEY: String = getEnvValue("HASH_KEY", "test")
  val API_TOKEN_EXPIRATION_SEC: Integer =
    Integer.parseInt(getEnvValue("API_TOKEN_EXPIRATION_SEC", "3600"))
  // for cache
  val CACHE_TTL_SEC: Long = Integer.parseInt(getEnvValue("CACHE_TTL_SEC", "3600"))
  // for presigned url
  val PRESIGNED_URL_EXPIRATION: Long =
    Integer.parseInt(getEnvValue("PRESIGNED_URL_EXPIRATION", "3600"))
  // for api
  val AUTHORIZATION_HEADER = "Authorization"
  val WORKSPACE_ID_HEADER = "workspaceid"

  // for google auth
  val GOOGLE_CLIENT_ID: String = getEnvValue("GOOGLE_CLIENT_ID", "")
  val GOOGLE_CLIENT_SECRET: String = getEnvValue("GOOGLE_CLIENT_SECRET", "")
  val GOOGLE_AUTH_STATE_CACHE_TTL_SEC: Long =
    Integer.parseInt(getEnvValue("GOOGLE_AUTH_STATE_CACHE_TTL_SEC", "60"))

  // for report
  val TEMPLATE_PATH: String = getEnvValue("TEMPLATE_PATH", "report")
  val SAMPLE_REPORT_PATH: String = s"${TEMPLATE_PATH}/sample.xlsx"
  val OUTPUT_FILE_PATH: String = getEnvValue("OUTPUT_FILE_PATH", "/tmp/report/output")
}

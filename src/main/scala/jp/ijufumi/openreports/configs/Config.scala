package jp.ijufumi.openreports.configs

import io.github.cdimascio.dotenv.DotenvBuilder

object Config {
  private val dotEnv = new DotenvBuilder().ignoreIfMissing().ignoreIfMissing().load()

  private def getEnvValue(key: String, defaultValue: String = ""): String = {
    dotEnv.get(key, sys.env.getOrElse(key, defaultValue))
  }

  private def getEnvIntValue(key: String, defaultValue: Int = 0): Int = {
    Integer.parseInt(getEnvValue(key, String.valueOf(defaultValue)))
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
    getEnvIntValue("API_TOKEN_EXPIRATION_SEC", 3600)
  // for cache
  val CACHE_TTL_SEC: Long = getEnvIntValue("CACHE_TTL_SEC", 3600)
  // for presigned url
  val PRESIGNED_URL_EXPIRATION: Long =
    getEnvIntValue("PRESIGNED_URL_EXPIRATION", 3600)
  // for api
  val AUTHORIZATION_HEADER = "Authorization"
  val WORKSPACE_ID_HEADER = "x-workspace-id"
  val API_TOKEN_HEADER = "x-api-token"
  val HEADERS: Seq[String] = Seq(
    API_TOKEN_HEADER,
    AUTHORIZATION_HEADER,
    WORKSPACE_ID_HEADER,
  )
  // for google auth
  val GOOGLE_CLIENT_ID: String = getEnvValue("GOOGLE_CLIENT_ID", "")
  val GOOGLE_CLIENT_SECRET: String = getEnvValue("GOOGLE_CLIENT_SECRET", "")
  val GOOGLE_AUTH_STATE_CACHE_TTL_SEC: Long =
    Integer.parseInt(getEnvValue("GOOGLE_AUTH_STATE_CACHE_TTL_SEC", "60"))

  // for reports
  val TEMPLATE_ROOT_PATH: String = getEnvValue("TEMPLATE_ROOT_PATH", "/reports")
  val SAMPLE_REPORT_PATH: String = "sample.xlsx"
  val OUTPUT_FILE_PATH: String = getEnvValue("OUTPUT_FILE_PATH", "/tmp/reports/output")

  // for file upload
  val UPLOAD_FILE_MAX_SIZE: Long = getEnvIntValue("UPLOAD_FILE_MAX_SIZE", 1024 * 1024)
}

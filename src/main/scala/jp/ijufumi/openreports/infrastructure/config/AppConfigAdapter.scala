package jp.ijufumi.openreports.infrastructure.config

import jp.ijufumi.openreports.configs.Config
import jp.ijufumi.openreports.domain.port.AppConfigPort

class AppConfigAdapter extends AppConfigPort {
  override def accessTokenExpirationSec: Integer = Config.ACCESS_TOKEN_EXPIRATION_SEC

  override def refreshTokenExpirationSec: Integer = Config.REFRESH_TOKEN_EXPIRATION_SEC

  override def outputFilePath: String = Config.OUTPUT_FILE_PATH

  override def sampleReportPath: String = Config.SAMPLE_REPORT_PATH
}

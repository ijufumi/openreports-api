package jp.ijufumi.openreports.domain.port

trait AppConfigPort {
  def accessTokenExpirationSec: Integer

  def refreshTokenExpirationSec: Integer

  def outputFilePath: String

  def sampleReportPath: String
}

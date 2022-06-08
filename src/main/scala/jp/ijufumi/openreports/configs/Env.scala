package jp.ijufumi.openreports.configs

import sys.env

object Env {
  val TOKEN_EXPIRED_SECONDS: Integer =
    Integer.parseInt(env.getOrElse[String]("TOKEN_EXPIRED_SECONDS", "3600"))
}

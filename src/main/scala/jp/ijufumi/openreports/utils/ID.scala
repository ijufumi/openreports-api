package jp.ijufumi.openreports.utils

import com.chatwork.scala.ulid.ULID

object ID {
  def ulid(): String = {
    ULID.generate().asString
  }
}

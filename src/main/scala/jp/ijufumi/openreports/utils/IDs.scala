package jp.ijufumi.openreports.utils

import com.chatwork.scala.ulid.ULID

object IDs {
  def ulid(): String = {
    ULID.generate(timestampGenerator = Dates.currentTimestampNano).asString
  }
}

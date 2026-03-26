package jp.ijufumi.openreports.utils

import com.github.f4b6a3.ulid.UlidCreator

object IDs {
  def ulid(): String = {
    UlidCreator.getMonotonicUlid().toString
  }
}

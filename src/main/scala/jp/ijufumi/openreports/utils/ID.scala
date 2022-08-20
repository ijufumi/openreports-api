package jp.ijufumi.openreports.utils

import net.petitviolet.ulid4s.ULID

object ID {
  def ulid(): String = {
    ULID.generate
  }
}

package jp.ijufumi.openreports.utils

import de.huxhorn.sulky.ulid.ULID

object ID {
  def ulid(): String = {
    new ULID().nextULID()
  }
}

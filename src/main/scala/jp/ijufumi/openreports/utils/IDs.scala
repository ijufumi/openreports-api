package jp.ijufumi.openreports.utils

import de.huxhorn.sulky.ulid.ULID

object IDs {
  def ulid(): String = {
    new ULID().nextULID()
  }
}

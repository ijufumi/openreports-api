package jp.ijufumi.openreports.repositories.db

import scala.concurrent.duration.Duration

package object impl {
  val queryTimeout = Duration("10s")
}

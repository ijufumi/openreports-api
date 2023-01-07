package jp.ijufumi.openreports.gateways.datastores.database.repositories

import scala.concurrent.duration.Duration

package object impl {
  val queryTimeout: Duration = Duration("10s")
}

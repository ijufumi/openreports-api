package jp.ijufumi.openreports.interfaces.models.outputs

case class Lists[T](items: Seq[T], offset: Int, limit: Int, count: Int)

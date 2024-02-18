package jp.ijufumi.openreports.presentation.models.responses

case class Lists[T](items: Seq[T], offset: Int, limit: Int, count: Int)

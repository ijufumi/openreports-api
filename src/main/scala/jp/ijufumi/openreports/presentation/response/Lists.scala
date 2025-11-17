package jp.ijufumi.openreports.presentation.response

case class Lists[T](items: Seq[T], offset: Int, limit: Int, count: Int)

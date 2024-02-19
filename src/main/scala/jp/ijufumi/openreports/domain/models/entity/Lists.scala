package jp.ijufumi.openreports.domain.models.entity

case class Lists[T](items: Seq[T], offset: Int, limit: Int, count: Int)

package jp.ijufumi.openreports.vo.response

case class Reports(items: Seq[Report], offset: Int, limit: Int, count: Int)

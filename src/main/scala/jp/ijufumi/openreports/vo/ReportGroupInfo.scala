package jp.ijufumi.openreports.vo

case class ReportGroupInfo(_reportGroupId: Long, _reportGroupName: String) {
  def reportGroupId: Long = _reportGroupId

  def reportGroupName: String = _reportGroupName
}
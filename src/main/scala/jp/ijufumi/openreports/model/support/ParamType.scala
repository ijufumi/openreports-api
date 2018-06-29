package jp.ijufumi.openreports.model.support

case class ParamType(_type: String) {
  val TEXT = ParamType("0")
  val DATE = ParamType("1")
  val LIST = ParamType("2")
  val QUERY = ParamType("3")
}

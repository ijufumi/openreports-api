package jp.ijufumi.openreports.interfaces.models.value.enums

object ActionTypes extends Enumeration {
  type ActionType = Value

  val Create = Value("create")
  val Update = Value("update")
  val Delete = Value("delete")
  val Reference = Value("reference")
}

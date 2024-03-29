package jp.ijufumi.openreports.domain.models.value.enums

object ParameterTypes extends Enumeration {
  type ParameterType = Value

  val EmbeddedFunction = Value("embedded_function")
  val FixedValue = Value("fixed_value")
}

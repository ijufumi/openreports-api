package jp.ijufumi.openreports.model.support

object ParamType extends Enumeration {

  protected case class Val(value: String) extends super.Val {
    def equals(value: String): Boolean = {
      Val.this == ParamType.of(value)
    }
  }

  val TEXT = Val("1")
  val DATE = Val("2")
  val LIST = Val("3")
  val QUERY = Val("4")

  implicit def valueToVal(x: Value): Val = x.asInstanceOf[Val]

  def of(value: String): ParamType.Val = {
    values.find(_.value == value).getOrElse(TEXT)
  }
}
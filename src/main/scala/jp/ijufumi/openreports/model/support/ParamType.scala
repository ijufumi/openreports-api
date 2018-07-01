package jp.ijufumi.openreports.model.support

object ParamType extends Enumeration {

  protected case class Val(value: String) extends super.Val

  val TEXT = Value("1")
  val DATE = Value("2")
  val LIST = Value("3")
  val QUERY = Value("4")

  implicit def valueToVal(x: Value): Val = x.asInstanceOf[Val]

  def of(value: String): ParamType.Val = {
    values.find(_.value == value).getOrElse(TEXT)
  }
}
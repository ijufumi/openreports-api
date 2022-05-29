package jp.ijufumi.openreports.service.enums

import scala.language.implicitConversions

object StatusCode extends Enumeration {

  val OK = NewVal("")
  val DATA_NOT_FOUND = NewVal("not found")
  val DUPLICATE_ERR = NewVal("duplicate key value violates")
  val ALREADY_UPDATED = NewVal("already updated")
  val OTHER_ERROR = NewVal("error")

  def of(e: Exception): StatusCode.NewVal = {
    of(e.getMessage)
  }

  implicit def valueToVal(x: Value): NewVal = x.asInstanceOf[NewVal]

  def of(value: String): StatusCode.NewVal = {
    values.find(_.value.contains(value)).getOrElse(OTHER_ERROR)
  }

  protected case class NewVal(value: String) extends super.Val {
    def equals(value: String): Boolean = {
      NewVal.this == StatusCode.of(value)
    }
  }

}

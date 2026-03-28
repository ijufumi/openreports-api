package jp.ijufumi.openreports.presentation.controller.base

import jp.ijufumi.openreports.presentation.validation.{
  ValidationFailure,
  ValidationSuccess,
  Validator,
}
import org.json4s.jvalue2extractable
import org.scalatra.*
import org.scalatra.json.JacksonJsonSupport

trait ValidationSupport extends JacksonJsonSupport { self: ScalatraServlet =>

  def validateBody[T: Manifest](block: T => Any)(implicit validator: Validator[T]): Any = {
    parsedBody.extractOpt[T] match {
      case Some(data) =>
        validator.validate(data) match {
          case ValidationSuccess =>
            block(data)
          case ValidationFailure(violations) =>
            halt(
              BadRequest(
                body = violations.map(_.message).mkString(", "),
                headers = Map("Content-Type" -> "text/plain"),
              ),
            )
        }
      case None =>
        halt(
          BadRequest(
            body = "Invalid JSON structure or type mismatch",
            headers = Map("Content-Type" -> "text/plain"),
          ),
        )
    }
  }
}

package jp.ijufumi.openreports.presentation.controller.base

import com.wix.accord.{Failure, Success, Validator, validate}
import org.scalatra._
import org.scalatra.json.JacksonJsonSupport

trait ValidationSupport extends JacksonJsonSupport { self: ScalatraServlet =>

  def validateBody[T: Manifest](block: T => Any)(implicit validator: Validator[T]): Any = {
    parsedBody.extractOpt[T] match {
      case Some(data) =>
        validate(data) match {
          case Success =>
            block(data)
          case Failure(violations) =>
            halt(BadRequest(body = violations.toString, headers = Map("Content-Type" -> "text/plain")))
        }
      case None =>
        halt(BadRequest(body = "Invalid JSON structure or type mismatch", headers = Map("Content-Type" -> "text/plain")))
    }
  }
}

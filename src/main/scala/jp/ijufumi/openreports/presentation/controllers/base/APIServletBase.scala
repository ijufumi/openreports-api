package jp.ijufumi.openreports.presentation.controllers.base

import jp.ijufumi.openreports.configs.Config
import jp.ijufumi.openreports.domain.models.value.enums.{JdbcDriverClasses, RoleTypes, StorageTypes}
import jp.ijufumi.openreports.presentation.models.responses.Member
import jp.ijufumi.openreports.utils.Logging
import org.json4s.{DefaultFormats, Formats}
import org.json4s.ext.EnumNameSerializer
import org.scalatra.CorsSupport.{CORSConfig, CorsConfigKey}
import org.scalatra.{ActionResult, BadRequest, CorsSupport, Forbidden, InternalServerError, NotFound, Ok, ScalatraServlet, Unauthorized}
import org.scalatra.json.JacksonJsonSupport
import org.scalatra.forms._
import org.scalatra.i18n.I18nSupport

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

abstract class APIServletBase
    extends ScalatraServlet
    with JacksonJsonSupport
    with FormSupport
    with I18nSupport
    with Logging
    with CorsSupport {

  protected val ATTRIBUTE_KEY_MEMBER = "member"

  override protected implicit lazy val jsonFormats: Formats = {
    DefaultFormats ++ Seq(
      org.json4s.ext.JavaTimeSerializers.all,
      Seq(
        new EnumNameSerializer(StorageTypes),
        new EnumNameSerializer(RoleTypes),
        new EnumNameSerializer(JdbcDriverClasses),
      ),
    ).flatten
  }

  before() {
    contentType = formats("json")
  }

  after() {
    response.setHeader("Access-Control-Expose-Headers", Config.HEADERS.mkString(","))
  }

  options("/*") {
    Ok()
  }

  def isOptions: Boolean = {
    request.getMethod.toUpperCase().equals("OPTIONS")
  }

  def ok(obj: Any): ActionResult = {
    hookResult(Ok(obj))
  }

  // 4xx
  def notFound(obj: Any): ActionResult = {
    hookResult(NotFound(obj))
  }

  def badRequest(obj: Any): ActionResult = {
    hookResult(BadRequest(obj))
  }

  def unauthorized(obj: Any): ActionResult = {
    hookResult(Unauthorized(obj))
  }

  def forbidden(obj: Any): ActionResult = {
    hookResult(Forbidden(obj))
  }

  // 5xx
  def internalServerError(obj: Any): ActionResult = {
    hookResult(InternalServerError(obj))
  }

  def params(key: String, default: String): String = {
    params(request).getOrElse(key, default)
  }

  private def hookResult(actionResult: ActionResult): ActionResult = {
    val servletPath = request.getRequestURI
    val queryString = request.getQueryString
    val requestMethod = request.getMethod
    val requestPath =
      if (queryString == null || queryString.isEmpty) servletPath
      else s"${servletPath}?${queryString}"
    val message = s"${requestMethod} ${requestPath} ${actionResult.status}"
    actionResult.status match {
      case n if 400 <= n && n < 500 => logger.warn(message)
      case n if 500 <= n            => logger.error(message)
      case _                        => logger.info(message)
    }
    actionResult
  }

  error {
    case t => {
      logger.error(t.getMessage, t)
      internalServerError(t)
    }
  }

  protected def extractBody[T: Manifest](): T = parse(request.body).extract[T]

  protected def setMember(member: Member): Unit = {
    request.setAttribute(ATTRIBUTE_KEY_MEMBER, member)
  }

  // improvement CORS support
  def corsConfig: CORSConfig = {
    servletContext.get(CorsConfigKey).orNull.asInstanceOf[CORSConfig]
  }
  override def handle(req: HttpServletRequest, res: HttpServletResponse): Unit = {
    if (corsConfig.enabled) {
      withRequest(req) {
        withResponse(res) {
          request.requestMethod match {
            case org.scalatra.Put => {
              augmentSimpleRequest()
              super.handle(req, res)
            }
            case _ => super.handle(req, res)
          }
        }
      }
    }
  }
}

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.{DefaultServlet, ServletContextHandler}
import org.eclipse.jetty.webapp.WebAppContext
import org.scalatra.servlet.ScalatraListener
import com.google.inject.servlet.GuiceFilter

import java.util
import javax.servlet.DispatcherType

object JettyLauncher {
  private val CORS_HEADERS = Seq(
    "Cookie",
    "Host",
    "X-Forwarded-For",
    "Accept-Charset",
    "If-Modified-Since",
    "Accept-Language",
    "X-Forwarded-Port",
    "Connection",
    "X-Forwarded-Proto",
    "User-Agent",
    "Referer",
    "Accept-Encoding",
    "X-Requested-With",
    "Authorization",
    "Accept",
    "Content-Type",
    "WorkspaceId",
  )

  def main(args: Array[String]) {
    val port = sys.env.getOrElse("PORT", "8080").toInt

    val server = new Server(port)
    val context = new WebAppContext()
    context setContextPath "/"
    context.setResourceBase("src/main/webapp")
    context.setInitParameter(ScalatraListener.LifeCycleKey, "ScalatraBootstrap")
    context.addEventListener(new ScalatraListener)
    context.addServlet(classOf[DefaultServlet], "/")
    context.addFilter(classOf[GuiceFilter], "/", util.EnumSet.of(DispatcherType.REQUEST))
    context.setInitParameter("org.scalatra.cors.allowedOrigins", "*")
    context.setInitParameter("org.scalatra.cors.allowedHeaders", CORS_HEADERS.mkString(","))
    context.setInitParameter("org.scalatra.cors.allowCredentials", "false")

    server.setHandler(context)

    server.start()
    server.join()
  }
}

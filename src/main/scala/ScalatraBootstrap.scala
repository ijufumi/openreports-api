import jp.ijufumi.openreports.api.private_.{MembersServlet, ReportsServlet, ReportTemplatesServlet}
import jp.ijufumi.openreports.api.public_.{HealthServlet, LoginServlet}
import org.scalatra._

import javax.servlet.ServletContext
import jp.ijufumi.openreports.injector.Injector

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext): Unit = {
    // public endpoints
    mount(context, classOf[HealthServlet], "/health")
    mount(context, classOf[LoginServlet], "/login")
    // private endpoints
    mount(context, classOf[MembersServlet], "/members")
    mount(context, classOf[ReportsServlet], "/:workspaceId/reports")
    mount(context, classOf[ReportTemplatesServlet], "/:workspaceId/report_templates")
  }

  def mount(
      context: ServletContext,
      servletClass: Class[_ <: ScalatraServlet],
      uriPattern: String,
  ): Unit = {
    def servlet = Injector.createAndInject[ScalatraServlet](servletClass)
    context.mount(servlet, uriPattern)
  }
}

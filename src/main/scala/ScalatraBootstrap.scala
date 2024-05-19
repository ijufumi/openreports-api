import jp.ijufumi.openreports.configs.injectors.Injector
import jp.ijufumi.openreports.presentation.controllers.private_.{DataSourceServlet, MemberServlet, ReportGroupServlet, ReportServlet, TemplateServlet, WorkspaceMembersServlet, WorkspaceServlet}
import jp.ijufumi.openreports.presentation.controllers.public_.{DriverTypeServlet, HealthServlet, LoginServlet, RoleServlet}
import org.scalatra._

import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext): Unit = {
    // public endpoints
    mount(context, classOf[HealthServlet], "/health")
    mount(context, classOf[LoginServlet], "/login")
    mount(context, classOf[RoleServlet], "/roles")
    mount(context, classOf[DriverTypeServlet], "/driver-types")
    // private endpoints
    mount(context, classOf[MemberServlet], "/members")
    mount(context, classOf[ReportServlet], "/reports")
    mount(context, classOf[ReportGroupServlet], "/report-groups")
    mount(context, classOf[TemplateServlet], "/templates")
    mount(context, classOf[DataSourceServlet], "/data-sources")
    mount(context, classOf[WorkspaceServlet], "/workspaces")
    mount(context, classOf[WorkspaceMembersServlet], "/workspace-members")
  }

  private def mount(
      context: ServletContext,
      servletClass: Class[_ <: ScalatraServlet],
      uriPattern: String,
  ): Unit = {
    def servlet = Injector.createAndInject[ScalatraServlet](servletClass)
    context.mount(servlet, uriPattern)
  }
}

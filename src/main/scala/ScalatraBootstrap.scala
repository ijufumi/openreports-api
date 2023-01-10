import jp.ijufumi.openreports.apis.private_.{
  DataSourceServlet,
  MemberServlet,
  ReportServlet,
  TemplateServlet,
  WorkspaceMembersServlet,
  WorkspaceServlet,
}
import jp.ijufumi.openreports.apis.public_.{HealthServlet, LoginServlet}
import jp.ijufumi.openreports.configs.injectors.Injector
import org.scalatra._

import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext): Unit = {
    // public endpoints
    mount(context, classOf[HealthServlet], "/health")
    mount(context, classOf[LoginServlet], "/login")
    // private endpoints
    mount(context, classOf[MemberServlet], "/members")
    mount(context, classOf[ReportServlet], "/reports")
    mount(context, classOf[TemplateServlet], "/templates")
    mount(context, classOf[DataSourceServlet], "/data_sources")
    mount(context, classOf[WorkspaceServlet], "/workspaces")
    mount(context, classOf[WorkspaceMembersServlet], "/workspace_members")
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

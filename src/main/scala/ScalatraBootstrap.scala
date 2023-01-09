import jp.ijufumi.openreports.apis.private_.{
  DataSourcesServlet,
  MembersServlet,
  ReportsServlet,
  TemplatesServlet,
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
    mount(context, classOf[MembersServlet], "/members")
    mount(context, classOf[ReportsServlet], "/reports")
    mount(context, classOf[TemplatesServlet], "/templates")
    mount(context, classOf[DataSourcesServlet], "/data_sources")
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

import jp.ijufumi.openreports.api._
import org.scalatra._
import javax.servlet.ServletContext
import jp.ijufumi.openreports.injector.Injector

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    val apiServlet = new APIServlet
    Injector.inject(apiServlet)
    val loginServlet = Injector.createAndInject(classOf[LoginServlet])
    context.mount(loginServlet, "/login")
    context.mount(apiServlet, "/*")
  }
}

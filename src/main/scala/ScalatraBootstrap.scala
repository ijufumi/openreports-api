import jp.ijufumi.openreports.api._
import jp.ijufumi.openreports.api.public_.{HealthServlet, LoginServlet}
import org.scalatra._

import javax.servlet.ServletContext
import jp.ijufumi.openreports.injector.Injector

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    val apiServlet = new HealthServlet
    Injector.inject(apiServlet)
    val loginServlet = Injector.createAndInject[ScalatraServlet](classOf[LoginServlet])
    context.mount(loginServlet, "/login")
    context.mount(apiServlet, "/*")
  }
}

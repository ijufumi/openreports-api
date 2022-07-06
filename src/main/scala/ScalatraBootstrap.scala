import jp.ijufumi.openreports.api._
import org.scalatra._
import javax.servlet.ServletContext
import jp.ijufumi.openreports.injector.Injector

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    val servlet = new APIServlet
    Injector.inject(servlet)
    context.mount(servlet, "/*")
  }
}

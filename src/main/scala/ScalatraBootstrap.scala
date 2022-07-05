import jp.ijufumi.openreports.api._
import org.scalatra._
import javax.servlet.ServletContext
import jp.ijufumi.openreports.repositories.database.Config

class ScalatraBootstrap extends LifeCycle {
  val db = Config.createDatabase()

  override def init(context: ServletContext) {
    context.mount(new APIServlet, "/*")
  }

  override def destroy(context: ServletContext) {
    super.destroy(context)
    db.close()
  }
}

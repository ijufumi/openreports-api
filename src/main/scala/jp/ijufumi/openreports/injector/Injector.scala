package jp.ijufumi.openreports.injector

import com.google.inject.Guice
import org.scalatra.ScalatraServlet

object Injector {
  def createAndInject(clazz: Class[_ <: ScalatraServlet]): ScalatraServlet = {
    val injector = Guice.createInjector(new DatabaseModule(), new ServiceModule())
    injector.getInstance(clazz)
  }

  def inject(instance: AnyRef): Unit = {
    val injector = Guice.createInjector(new DatabaseModule(), new ServiceModule())
    injector.injectMembers(instance)
  }
}

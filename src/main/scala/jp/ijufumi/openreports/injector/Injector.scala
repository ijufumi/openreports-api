package jp.ijufumi.openreports.injector

import com.google.inject.Guice

object Injector {
  def createAndInject[T](clazz: Class[_ <: T]): T = {
    val injector = Guice.createInjector(new DatabaseModule(), new ServiceModule())
    injector.getInstance(clazz)
  }

  def inject(instance: AnyRef): Unit = {
    val injector = Guice.createInjector(new DatabaseModule(), new ServiceModule())
    injector.injectMembers(instance)
  }
}

package jp.ijufumi.openreports.injector

import com.google.inject.Guice

object Injector {
  def inject(instance: AnyRef): Unit = {
    val injector = Guice.createInjector(new DatabaseModule())
    injector.injectMembers(instance)
  }
}

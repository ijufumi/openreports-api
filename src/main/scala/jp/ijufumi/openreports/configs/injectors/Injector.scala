package jp.ijufumi.openreports.configs.injectors

import com.google.inject.Guice

object Injector {
  private val injector =
    Guice.createInjector(
      new DatabaseModule(),
      new ServiceModule(),
      new RepositoryModule(),
      new CacheModule,
    )

  def createAndInject[T](clazz: Class[_ <: T]): T = {
    injector.getInstance(clazz)
  }

  def inject(instance: AnyRef): Unit = {
    injector.injectMembers(instance)
  }
}

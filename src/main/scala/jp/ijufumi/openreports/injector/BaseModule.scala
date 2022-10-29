package jp.ijufumi.openreports.injector

import com.google.inject.{AbstractModule, Singleton}

abstract class BaseModule extends AbstractModule {
  override def configure(): Unit = {
    super.configure()
  }

  def bindAsSingleton[T](
      interfaceClass: java.lang.Class[T],
      implementClass: java.lang.Class[_ <: T],
  ) = {
    bind(interfaceClass)
      .to(implementClass)
      .in(classOf[Singleton])
  }

  def bindInstance[T](interfaceClass: java.lang.Class[T], instance: T) = {
    bind(interfaceClass).toInstance(instance)
  }
}

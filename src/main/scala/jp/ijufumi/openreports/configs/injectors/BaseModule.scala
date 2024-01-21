package jp.ijufumi.openreports.configs.injectors

import com.google.inject.{AbstractModule, Provider, Singleton}

abstract class BaseModule extends AbstractModule {
  override def configure(): Unit = {
    super.configure()
  }

  def bindAsSingleton[T](
      interfaceClass: java.lang.Class[T],
      implementClass: java.lang.Class[_ <: T],
  ): Unit = {
    bind(interfaceClass)
      .to(implementClass)
      .in(classOf[Singleton])
  }

  def bindInstance[T](interfaceClass: java.lang.Class[T], instance: T): Unit = {
    bind(interfaceClass).toInstance(instance)
  }

  def bindProvider[T](interfaceClass: java.lang.Class[T], f: CustomProvider[T]): Unit = {
    bind(interfaceClass).toProvider(new Provider[T]() {
      override def get(): T = {
        f.provide()
      }
    })
  }
}

@FunctionalInterface
trait CustomProvider[T] {
  def provide(): T
}

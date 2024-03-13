package jp.ijufumi.openreports.configs.injectors

import com.google.inject.servlet.RequestScoped
import com.google.inject.{AbstractModule, Provider, Scope, Singleton}

import java.lang.annotation.Annotation

abstract class BaseModule extends AbstractModule {
  val SingletonScope = classOf[Singleton]
  val RequestScope = classOf[RequestScoped]

  override def configure(): Unit = {
    super.configure()
  }

  def bindAsSingleton[T](
      interfaceClass: java.lang.Class[T],
      implementClass: java.lang.Class[_ <: T],
  ): Unit = {
    bind(interfaceClass)
      .to(implementClass)
      .in(SingletonScope)
  }

  def bindInstance[T](interfaceClass: java.lang.Class[T], instance: T): Unit = {
    bind(interfaceClass).toInstance(instance)
  }

  def bindProvider[T](interfaceClass: java.lang.Class[T], f: CustomProvider[T], scope: Class[_ <: Annotation] = SingletonScope): Unit = {
    bind(interfaceClass).toProvider(new Provider[T]() {
      override def get(): T = {
        f.provide()
      }
    }).in(scope)
  }
}

@FunctionalInterface
trait CustomProvider[T] {
  def provide(): T
}

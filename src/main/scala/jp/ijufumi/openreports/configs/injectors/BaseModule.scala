package jp.ijufumi.openreports.configs.injectors

import com.google.inject.servlet.RequestScoped
import com.google.inject.{AbstractModule, Provider, Scope, Singleton}

import java.lang.annotation.Annotation

abstract class BaseModule extends AbstractModule {
  val SingletonScope: Class[Singleton] = classOf[Singleton]
  val RequestScope: Class[RequestScoped] = classOf[RequestScoped]

  override def configure(): Unit = {
    super.configure()
  }

  def bindClass[T](
      interfaceClass: java.lang.Class[T],
      implementClass: java.lang.Class[_ <: T],
      scope: Class[_ <: Annotation] = SingletonScope
  ): Unit = {
    bind(interfaceClass)
      .to(implementClass)
      .in(scope)
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

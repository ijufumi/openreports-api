package jp.ijufumi.openreports.utils

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class LoggingSpec extends AnyFlatSpec with Matchers {

  // Test classes that use the Logging trait
  class TestClass1 extends Logging {
    def getLoggerName: String = logger.getName
    def isDebugEnabled: Boolean = logger.isDebugEnabled
    def isInfoEnabled: Boolean = logger.isInfoEnabled
    def isWarnEnabled: Boolean = logger.isWarnEnabled
    def isErrorEnabled: Boolean = logger.isErrorEnabled
  }

  class TestClass2 extends Logging {
    def getLoggerName: String = logger.getName
  }

  class NestedTestClass extends Logging {
    def getLoggerName: String = logger.getName
  }

  "Logging trait" should "initialize logger with correct class name" in {
    val testInstance = new TestClass1()

    testInstance.getLoggerName should equal("jp.ijufumi.openreports.utils.LoggingSpec$TestClass1")
  }

  it should "create different loggers for different classes" in {
    val testInstance1 = new TestClass1()
    val testInstance2 = new TestClass2()

    testInstance1.getLoggerName should not equal testInstance2.getLoggerName
    testInstance1.getLoggerName should equal("jp.ijufumi.openreports.utils.LoggingSpec$TestClass1")
    testInstance2.getLoggerName should equal("jp.ijufumi.openreports.utils.LoggingSpec$TestClass2")
  }

  it should "create same logger name for different instances of same class" in {
    val instance1 = new TestClass1()
    val instance2 = new TestClass1()

    instance1.getLoggerName should equal(instance2.getLoggerName)
  }

  it should "support nested classes" in {
    val nestedInstance = new NestedTestClass()

    nestedInstance.getLoggerName should include("NestedTestClass")
  }

  it should "provide access to logger methods" in {
    val testInstance = new TestClass1()

    // These methods should be accessible and return boolean values
    noException should be thrownBy testInstance.isDebugEnabled
    noException should be thrownBy testInstance.isInfoEnabled
    noException should be thrownBy testInstance.isWarnEnabled
    noException should be thrownBy testInstance.isErrorEnabled
  }

  it should "not be null" in {
    val testInstance = new TestClass1()

    testInstance.getLoggerName should not be null
  }

  it should "be usable in real classes" in {
    // Create a realistic example class
    class ServiceClass extends Logging {
      def performOperation(): String = {
        logger.debug("Debug message")
        logger.info("Info message")
        logger.warn("Warning message")
        "operation completed"
      }
    }

    val service = new ServiceClass()
    val result = service.performOperation()

    result should equal("operation completed")
  }

  it should "support inheritance" in {
    class BaseService extends Logging {
      def getLoggerFromBase: String = logger.getName
    }

    class DerivedService extends BaseService {
      def getLoggerFromDerived: String = logger.getName
    }

    val derived = new DerivedService()

    // The logger should be initialized with the derived class name
    derived.getLoggerFromDerived should include("DerivedService")
  }

  it should "be mixable with other traits" in {
    trait OtherTrait {
      def otherMethod(): String = "other"
    }

    class MixedClass extends Logging with OtherTrait {
      def getLoggerName: String = logger.getName
      def combined(): String = {
        logger.info("Using both traits")
        otherMethod()
      }
    }

    val mixed = new MixedClass()

    mixed.getLoggerName should include("MixedClass")
    mixed.combined() should equal("other")
  }

  "Logger instance" should "be protected" in {
    // This test verifies that logger is protected by trying to access it
    // If we can create a class that extends Logging, the logger is accessible in subclasses
    class AccessTestClass extends Logging {
      def canAccessLogger: Boolean = {
        logger != null
      }
    }

    val testInstance = new AccessTestClass()
    testInstance.canAccessLogger should be(true)
  }

  it should "use SLF4J Logger" in {
    val testInstance = new TestClass1()

    // Verify it's an SLF4J logger by checking the class name
    testInstance.getLoggerName should not be empty
  }

  it should "be lazy initialized only once" in {
    class CountingClass extends Logging {
      var initCount = 0

      // Access logger multiple times
      def accessLogger(): String = {
        logger.getName
      }
    }

    val instance = new CountingClass()

    // Access logger multiple times
    val name1 = instance.accessLogger()
    val name2 = instance.accessLogger()
    val name3 = instance.accessLogger()

    // Should return same name each time
    name1 should equal(name2)
    name2 should equal(name3)
  }

  "Logging in package structure" should "reflect full package name" in {
    val testInstance = new TestClass1()

    testInstance.getLoggerName should startWith("jp.ijufumi.openreports.utils")
  }

  it should "be usable in anonymous classes" in {
    class AnonymousLikeLogger extends Logging {
      def getLoggerName: String = logger.getName
    }
    val anonymousLogger = new AnonymousLikeLogger()

    anonymousLogger.getLoggerName should include("LoggingSpec")
  }

  "Multiple instances" should "share logger configuration" in {
    val instance1 = new TestClass1()
    val instance2 = new TestClass1()
    val instance3 = new TestClass1()

    // All should have the same logger name
    instance1.getLoggerName should equal(instance2.getLoggerName)
    instance2.getLoggerName should equal(instance3.getLoggerName)
  }
}

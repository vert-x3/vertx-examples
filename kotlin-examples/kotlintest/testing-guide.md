# Vert.x Testing Guide

This guide will cover how to test Vert.x code in a *Kotlinic* (idiomatic Kotlin) way. In order to do testing [Kotlin Test](https://github.com/kotlintest/kotlintest) will be used as the testing framework. Although Vert.x has a [testing library](http://vertx.io/docs/vertx-unit/kotlin/) it is designed with Java in mind and isn't *Kotlinic*.

For the purposes of this guide the Kotlin Test Functional testing style will be used. For more information on all the testing styles refer to the [Kotlin Test reference](https://github.com/kotlintest/kotlintest/blob/master/doc/reference.md#testing-styles) documentation. This guide is based on the work done in this [GitHub project](https://github.com/napperley/vertx-kotlintest).


## Basics

When using Kotlin Test with the Functional testing style a class is defined which extends **FunSpec**. Like JUnit the defined class is a Test Case containing one or more tests. Each test is defined as a lambda which is passed through to the **test** function, along with the name of the test. All tests are called in the **init** block of the defined class. See the following example:

```kotlin
import io.kotlintest.specs.FunSpec

class ExampleTest : FunSpec() {
  init {
    test("function x should return the correct result") {
      // Do some testing here.
    }
  }
}
```

All assertions are provided by Kotlin test as infix functions, and have the major advantage of being very concise yet readable without having to hunt around for an assertions library. Below is an example:

```kotlin
class ExampleTest : FunSpec() {
  init {
    test("function x should return the correct result") {
      val result: String
      // ...
      
      // Example assertion.
      result shouldEqual true
    }
  }
}
```

If an assertion fails during testing then a **AssertionException** is raised which causes a test to fail. Gradle can run tests using Kotlin Test through the standard **test** Gradle task. Kotlin Test uses the JUnit test runner which IntelliJ supports out of the box without needing to install a IntelliJ plugin.


## Test Lifecycle

In Kotlin Test it is possible to intercept the test lifecycle to execute tasks before and after a test (by overriding the **interceptTestCase** function), which is very useful for ensuring resources are initialised before a test and cleaned up after a test. Below is an example:

```kotlin
class ExampleTest : FunSpec() {
  // ...

  override fun interceptTestCase(context: TestCaseContext, test: () -> Unit) {
    // Before.
    // ...
    // Remember to run the test!
    test()
    // After.
    // ...
  }
}
```


## Best Practices

- In a Test Case define a variable called **vertx** with **Vertx?** as the type with the default value of *null*, eg:
  ```kotlin
    class ExampleTest : FunSpec() {
      private var vertx: Vertx? = null
    
      // ...
    }
  ```
- Every test should be in a separate named function to make it easy to manage tests, eg:
  ```kotlin
    class ExampleTest : FunSpec() {
      // ...
    
      init {
        testHttpGetStatus()
        testHttpPostStatus()
        // ...
      }
    
      private fun testHttpGetStatus() = test("Function httpGetStatus returns correct status") {
        // ...
      }
  
      private fun testHttpPostStatus() = test("Function httpPostStatus returns correct status") {
        // ...
      }
    }
  ```
- Always intercept a test to ensure that the **Vertx** object is handled properly, eg:
  ```kotlin
    import io.kotlintest.Spec
  
    fun Spec.runTest(before: () -> Unit, after: () -> Unit, block: () -> Unit) {
        try {
            before()
            block()
        } finally {
            after()
        }
    }
  
    class HttpClientExtensionsTest : FunSpec() {  
      // ...
  
      init {
        test("Function httpGetStatus returns correct status") {
          runTest(before = this::beforeTest, after = this::afterTest) {
            // ...
          }
        }
      }
    
      private fun beforeTest() {
        vertx = Vertx.vertx()
        // ...
      }
    
      private fun afterTest() {
        vertx?.close()
        vertx = null
        // ...
      }
    }
  ```
  
  Important to note that Kotlin 1.2 (currently in Beta) allows for a member function to be referenced without having to use **this**, eg ```::beforeTest```
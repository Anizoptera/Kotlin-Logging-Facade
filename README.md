# KotLog – Kotlin Logging Facade

**This library is an attempt to create a logging system that Kotlin developers could use with pleasure among various kinds of projects. KotLog uses Kotlin features and syntax sugar of the language intensively, which makes the logging process incredibly clean and simple.**

KotLog consists of 2 main parts:
1) Unified logging interface;
2) Logging adapters.

The interface has all the logging methods, but it doesn't perform the actual logging. It's just an interface that proxies calls to the connected adapter.

Since KotLog is a facade, its architecture is very familiar for developers who use SLF4J in their projects. But don't get it wrong. KotLog is not a replacement for SLF4J. In fact, KotLog can work in conjunction with SLF4J, providing quite convenient Kotlin interface.

## Installation

```gradle
repositories {
	maven { url "http://dl.bintray.com/azadev/maven" }
}

dependencies {
	// Choose the one you need:
//	compile "azadev.logging:kotlog:0.8"
//	compile "azadev.logging:kotlog-console:0.8"
//	compile "azadev.logging:kotlog-slf4j:0.8.1"
//	compile "azadev.logging:kotlog-android:0.8"
} 
```

You need to uncomment one of the above lines: either the interface, or any adapter. Adapters are depend on the interface, so you don't need to include the latter manually.

MODULE | DESCRIPTION
---- | ----
`azadev.logging:kotlog:0.8` | The main interface of KotLog. Use it and only it when you're creating a library or some non-standalone project.
`azadev.logging:kotlog-console:0.8` | Appends log events to `System.out` or `System.err`.
`azadev.logging:kotlog-slf4j:0.8.1` | Proxies log calls to SLF4J. For Kotlin developers it provides much more friendly API than SLF4J does.
`azadev.logging:kotlog-android:0.8` | Uses `android.util.Log` as the logging implementation. Has ability to log and send exceptions to Crashlytics as well.

## Usage

**KotLog** provides 9 logging methods:
- `logTrace`
- `logVerbose`
- `logDebug`
- `logConfig`
- `logInfo`
- `logWarning`
- `logError`
- `logErrorOrThrow` – logs an error (if in production mode), or throws an exception (if in dev-mode).
- `logWtf` – cases that should've happened.

Each method must be called with a receiver of type `Any`. This receiver will be used by an adapter to create a `Logger` instance (in case of SLF4J), or the log prefix (Console and Android adapters). This makes the logging process very easy. Inside any class/object you can simply call a logging method and it just works:

```kotlin
package com.myapp

class MyClass
{
	fun myMethod() {
		logDebug("myMethod called")
	}
}
```

In case of SLF4J you will never need to create and store `Logger` instance. **KotLog** will do it for you. Under the hood a `Logger` instance named `com.myapp.MyClass` will be retrieved, and then used for the actual logging. So you will be able to configure your logging system [as usual](https://logback.qos.ch/manual/configuration.html).

### If you have no receiver object

Sometimes you have no receiver object, e.g. when you want to log something inside a top-level function. In this case you simply need to log over a `String` or any another object that has a sane `toString()` method:

```kotlin
const val LOG_TAG = "com.myapp.Bootstrap"

fun main(args: Array<String>) {
	LOG_TAG.logInfo("App is launched")
	// ...
	LOG_TAG.logInfo("App is stopped")
}
```

In case of SLF4J a `Logger` named `com.myapp.Bootstrap` will be created.

### If you have many receiver objects

If you're logging inside a lambda that has its own receiver, then the logging will be performed on the basis of this lamda's receiver:

```kotlin
package com.myapp

class MyClass
{
	fun myMethod() {
		java.util.Properties().apply {
			// will use "java.util.Properties" as receiver
			logDebug("Props created")

			// will use "MyClass" as receiver
			this@MyClass.logDebug("Props created")
		}
	}
}
```

If that's not what you want, then you can use the `Logging` interface that has all the logging methods itself. And since class members in Kotlin have higher priority than extension functions, these members will be called instead:

```kotlin
package com.myapp

class MyClass : Logging
{
	fun myMethod() {
		java.util.Properties().apply {
			// will use "MyClass" as receiver
			logDebug("Props created")

			// will use "java.util.Properties" as receiver
			this@apply.logDebug("Props created")
		}
	}
}
```

## License

This software is released under the MIT License.
See [LICENSE.md](LICENSE.md) for details.

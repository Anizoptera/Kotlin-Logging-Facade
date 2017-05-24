package azadev.logging.impl

import azadev.logging.LoggingAdapter


/*
This is an example of defining a logging adapter.
Note, that all implementations must define "LOGGING_ADAPTER" variable in a file,
that named exacly like this one (instance.kt).
 */

@JvmField var LOGGING_ADAPTER: LoggingAdapter = object : LoggingAdapter() {
	override fun log(tag: Any?, level: Int, msg: CharSequence?, t: Throwable?, args: Array<out Any>) {}
	override fun logErrorOrThrow(tag: Any?, msg: CharSequence?, t: Throwable?, args: Array<out Any>) {}
}

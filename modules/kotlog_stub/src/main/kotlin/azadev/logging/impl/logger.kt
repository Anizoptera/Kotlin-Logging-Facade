package azadev.logging.impl

import azadev.logging.LoggingAdapter


@JvmField var LOGGING_ADAPTER: LoggingAdapter = object : LoggingAdapter() {
	override fun log(tag: Any?, level: Int, msg: CharSequence?, t: Throwable?, args: Array<out Any>) {}
	override fun logErrorOrThrow(tag: Any?, msg: CharSequence?, t: Throwable?, args: Array<out Any>) {}
}

package azadev.logging


abstract class LoggingAdapter
{
	abstract fun log(tag: Any?, level: Int, msg: CharSequence?, t: Throwable?, args: Array<out Any>)

	abstract fun logErrorOrThrow(tag: Any?, msg: CharSequence?, t: Throwable?, args: Array<out Any>)


	fun log(tag: Any?, level: Int, msg: CharSequence?) {}

	fun log(tag: Any?, level: Int, msg: CharSequence?, t: Throwable?) {}

	inline fun log(tag: Any?, level: Int, messageFunction: () -> CharSequence?) {}

	inline fun log(tag: Any?, level: Int, t: Throwable?, messageFunction: () -> CharSequence?) {}

	fun logErrorOrThrow(tag: Any?, msg: CharSequence?) {}

	fun logErrorOrThrow(tag: Any?, msg: CharSequence?, t: Throwable?) {}
}

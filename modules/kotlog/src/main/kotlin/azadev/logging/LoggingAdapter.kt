package azadev.logging

import azadev.logging.KotLogUtils.LOGGER_CLASS_NAME
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


/**
 * Logger base for KotLog
 *
 * @see org.slf4j.Logger
 * @see java.util.logging.Logger
 * @see org.jetbrains.anko.AnkoLogger
 * @see android.util.Log
 */
abstract class LoggingAdapter
{
	companion object
	{
		const val LOG_TAG = "Logger"

		/** Special mark for easy filtration */
		const val LOG_MARK = "[L]"

		@JvmField val ANONYMOUS_CLASS_REGEX = Pattern.compile("(\\$\\d+)+$")!!

		@JvmField val NULL_THROWABLE: Throwable? = null

		@JvmField val THROWABLE_STAB = Throwable()

		@JvmField val EMPTY_OBJECT_ARRAY: Array<Any> = emptyArray()
	}


	open val level: Int get() = LogLevel.ALL

	@JvmField val locale = Locale.US!!


	fun isLoggable(level: Int) = level >= this.level

	fun isNotLoggable(level: Int) = level < this.level


	abstract fun log(tag: Any?, level: Int, msg: CharSequence?, t: Throwable?, args: Array<out Any>)

	abstract fun logErrorOrThrow(tag: Any?, msg: CharSequence?, t: Throwable?, args: Array<out Any>)


	fun log(tag: Any?, level: Int, msg: CharSequence?) {
		log(tag, level, msg, NULL_THROWABLE, EMPTY_OBJECT_ARRAY)
	}

	fun log(tag: Any?, level: Int, msg: CharSequence?, t: Throwable?) {
		log(tag, level, msg, t, EMPTY_OBJECT_ARRAY)
	}

	inline fun log(tag: Any?, level: Int, messageFunction: () -> CharSequence?) {
		if (isLoggable(level))
			log(tag, level, messageFunction(), NULL_THROWABLE, EMPTY_OBJECT_ARRAY)
	}

	inline fun log(tag: Any?, level: Int, t: Throwable?, messageFunction: () -> CharSequence?) {
		if (isLoggable(level))
			log(tag, level, messageFunction(), t, EMPTY_OBJECT_ARRAY)
	}

	fun logErrorOrThrow(tag: Any?, msg: CharSequence?) {
		logErrorOrThrow(tag, msg, NULL_THROWABLE, EMPTY_OBJECT_ARRAY)
	}

	fun logErrorOrThrow(tag: Any?, msg: CharSequence?, t: Throwable?) {
		logErrorOrThrow(tag, msg, t, EMPTY_OBJECT_ARRAY)
	}



	protected val logTime: String
		get() = SimpleDateFormat("mm:ss.SSS", locale).format(Date())!!

	protected fun logLevelLetter(priority: Int): String {
		return when (priority) {
			LogLevel.TRACE -> 'T'
			LogLevel.VERBOSE -> 'V'
			LogLevel.DEBUG -> 'D'
			LogLevel.CONFIG -> 'C'
			LogLevel.INFO -> 'I'
			LogLevel.WARN -> 'W'
			LogLevel.ERROR -> 'E'
			LogLevel.ASSERT -> 'A'
			else -> return Integer.toString(priority)
		}.toString()
	}

	protected fun prepareException(tag: Any?, t: Throwable?, args: Array<out Any>): Throwable? {
		val stab = THROWABLE_STAB

		var exc: Throwable? = t
		if (exc === stab) exc = null

		if (exc == null)
			exc = tag as? Throwable ?: NULL_THROWABLE

		/** Fallback variant for misuse of arguments order similar to [android.util.Log], etc.. */
		if (exc == null && args.isNotEmpty())
			exc = args[0] as? Throwable

		return exc
	}

	protected fun prepareTag(tag: Any?): String {
		// Prepare tag
		var simpleTag: String? = (tag as? CharSequence)?.toString() ?: if (tag != null) tag.javaClass.simpleName else null
		if (simpleTag == null) {
			// DO NOT switch this to Thread.getCurrentThread().getStackTrace(). The test will pass
			// because Robolectric runs them on the JVM but on Android the elements are different.
			val stackTrace = Throwable().stackTrace
			val firstGoodIndex = indexOfTraceStart(stackTrace, LOGGER_CLASS_NAME)
			simpleTag = stackTrace[firstGoodIndex].createTag()
		}
		return simpleTag
	}

	protected fun prepareMessage(t: Throwable?, msg: CharSequence?, args: Array<out Any>): CharSequence? {
		var m = msg
		if (m == null || m.isEmpty()) {
			if (t != null)
				m = t.message
		}
		else if (args.isNotEmpty()) {
			try { m = java.lang.String.format(locale, m.toString(), *args) }
			catch (e: Exception) {
				if (t != null && e.cause == null)
					e.initCause(t)

				m = "$m; FORMAT ERROR: ${e.message}; ARGS: ${Arrays.toString(args)}"
				logErrorOrThrow(LOG_TAG, m, e, EMPTY_OBJECT_ARRAY)
			}
		}
		return m
	}


	protected fun indexOfTraceStart(stackTrace: Array<StackTraceElement>, classNameToDrop: String): Int {
		var lastIntrinsic = -1
		var i = 0
		val length = stackTrace.size
		while (i < length) {
			if (classNameToDrop.equals(stackTrace[i].className))
				lastIntrinsic = i
			i++
		}
		return lastIntrinsic + 1
	}

	/**
	 * Extract the tag which should be used for the message from the `element`.
	 * Will use the class name without any anonymous class suffixes (e.g., `Foo$1`
	 * becomes `Foo`).
	 *
	 * @see timber.log.Timber.DebugTree.createStackElementTag
	 */
	protected fun StackTraceElement.createTag(): String {
		var tag = className
		val matcher = ANONYMOUS_CLASS_REGEX.matcher(tag)
		if (matcher.find()) tag = matcher.replaceAll("")
		return tag.substringAfterLast('.')
	}



	protected fun sanitizeStackTrace(throwable: Throwable): Throwable {
		return KotLogUtils.sanitizeStackTrace(throwable)
	}
}

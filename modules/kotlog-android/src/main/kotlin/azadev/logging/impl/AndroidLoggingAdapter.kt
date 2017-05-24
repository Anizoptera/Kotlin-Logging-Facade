package azadev.logging.impl

import android.util.Log
import azadev.logging.*
import azadev.logging.LogLevel.ASSERT
import azadev.logging.LogLevel.DEBUG
import azadev.logging.LogLevel.ERROR
import azadev.logging.LogLevel.WARN
import com.crashlytics.android.Crashlytics


/**
 * Uses [android.util.Log] for logging.
 * Allows to send to Crashlytics (both logs and exceptions).
 *
 * @see org.jetbrains.anko.AnkoLogger
 * @link https://github.com/pawegio/KAndroid/blob/master/kandroid/src/main/java/com/pawegio/kandroid/KLog.kt
 */
class AndroidLoggingAdapter(
		override var level: Int = LogLevel.ALL,
		var productionMode: Boolean = false,
		var reportCrashes: Boolean = false
) : LoggingAdapter()
{
	companion object
	{
		// Internal field for testing purposes
		private const val RELEASE_LOGGING = false

		const val MAX_LOG_TAG_LENGTH = 23
		const val MAX_LOG_LENGTH = 4000
	}


	override fun log(tag: Any?, level: Int, msg: CharSequence?, t: Throwable?, args: Array<out Any>) {
		if (isNotLoggable(level))
			return

		val ex = prepareException(tag, t, args)
		val message = prepareMessage(ex, msg, args)?.toString()
				?: return  // Swallow message if it's null and there's no throwable

		// Notify Crashlytics about exceptions immediately
		// It may be important for OutOfMemory errors and similar
		if (reportCrashes && level >= (if (ex == null && t == null) ERROR else WARN)) {
			var e = ex
			if (e == null || message.isNotEmpty()) {
				e = if (level >= ASSERT)
					sanitizeStackTrace(IllegalStateException("WTF: $message", e))
				else
					SanitizedException("${logLevelLetter(level)}: $message", e)
			}

			// Send to Crashlytics
			try { Crashlytics.logException(e) }
			catch (ignored: Exception) {}
		}


		// Handle logging tag
		val simpleTag = prepareTag(tag)

		// Add stack trace to message
		var messageFull = message
		if (ex != null) {
			// Don't replace this with Log.getStackTraceString() - it hides
			// UnknownHostException, which is not what we want.
			var stack = getStackTraceString(ex)
			if (stack.isEmpty()) {
				val cause = ex.cause
				if (cause != null)
					stack = getStackTraceString(cause)
			}
			messageFull = "$message\n$stack"
		}


		// Send important logs to Crashlytics
		val priority = androidLogLevel(level)
		if (reportCrashes && level >= DEBUG) {
			try { Crashlytics.log(priority, simpleTag, messageFull) }
			catch (ignored: Exception) {}
		}

		// Don't bother logging in release builds if not enabled explicitly
		if (!productionMode || RELEASE_LOGGING)
			// Add special tag mark for easy filtration
			androidLog(priority, LOG_MARK + simpleTag, messageFull)

		// Show toast (is usually used as an explicit notice about warnings and errors)
//		if (SHOW_TOAST_FROM_LEVEL)
//			try {
//				val sb = StringBuilder(message.length + simpleTag.length + 3)
//				sb.append(logLevelLetter(level)).append('/')
//						.append(simpleTag).append(':').append(' ').append(message)
//
//				ctx.toastLong(sb)
//			}
//			catch (ignored: Exception) {}
	}

	override fun logErrorOrThrow(tag: Any?, msg: CharSequence?, t: Throwable?, args: Array<out Any>) {
		if (!productionMode) {
			val ex = prepareException(tag, t, args)
			val message = prepareMessage(ex, msg, args)
			throw sanitizeStackTrace(IllegalStateException("${prepareTag(tag)} Error: $message", ex))
		}
		log(tag, ERROR, msg, t, args)
	}


	/**
	 * Breaks up [msg] into maximum-length chunks (if needed) and send to either
	 * [Log.println] or [Log.wtf] for logging.
	 *
	 * @see timber.log.Timber.DebugTree.log(int, String, String, Throwable)
	 */
	private fun androidLog(priority: Int, msgTag: String, msg: String) {
		// Log.isLoggable will throw an IllegalArgumentException when the tag.length() > 23.
		// See http://developer.android.com/reference/android/util/Log.html for more information.
		var tag = msgTag
		if (tag.length > MAX_LOG_TAG_LENGTH)
			tag = tag.substring(0, MAX_LOG_TAG_LENGTH)

		val wtf = priority >= Log.ASSERT
		if (msg.length < MAX_LOG_LENGTH) {
			if (wtf) Log.wtf(tag, msg) else Log.println(priority, tag, msg)
			return
		}

		// Split by line, then ensure each line can fit into Log's maximum length.
		var i = 0
		val length = msg.length
		while (i < length) {
			var newline = msg.indexOf('\n', i)
			newline = if (newline == -1) length else newline
			do {
				val end = Math.min(newline, i + MAX_LOG_LENGTH)
				val part = msg.substring(i, end)
				if (wtf) Log.wtf(tag, part) else Log.println(priority, tag, part)
				i = end
			} while (i < newline)
			i++
		}
	}

	private fun androidLogLevel(level: Int): Int {
		return when (level) {
			LogLevel.TRACE -> Log.VERBOSE
			LogLevel.VERBOSE -> Log.VERBOSE
			LogLevel.DEBUG -> Log.DEBUG
			LogLevel.CONFIG -> Log.INFO
			LogLevel.INFO -> Log.INFO
			LogLevel.WARN -> Log.WARN
			LogLevel.ERROR -> Log.ERROR
			LogLevel.ASSERT -> Log.ASSERT
			else -> if (level > LogLevel.ASSERT)
				Log.ASSERT
			else
				Log.VERBOSE
		}
	}
}

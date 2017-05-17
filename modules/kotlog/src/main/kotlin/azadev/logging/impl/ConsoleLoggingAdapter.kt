package azadev.logging.impl

import azadev.logging.*
import java.io.StringWriter


/**
 * Appends log events to [System.out] or [System.err].
 */
class ConsoleLoggingAdapter(
		override var level: Int = LogLevel.ALL,
		var useStderrFrom: Int = LogLevel.WARN
) : LoggingAdapter()
{
	override fun log(tag: Any?, level: Int, msg: CharSequence?, t: Throwable?, args: Array<out Any>) {
		if (isNotLoggable(level))
			return

		val ex = prepareException(tag, t, args)
		val message = prepareMessage(ex, msg, args)
				?: return  // Swallow message if it's null and there's no throwable

		// Add special tag mark for easy filtration
		val markedTag = LOG_MARK + prepareTag(tag)

		val sw = StringWriter(128)
		sw.append(logTime).append(' ')
		sw.append(logLevelLetter(level)).append('/')
		sw.append(markedTag).append(':').append(' ')
		sw.append(message)
		if (ex != null) sw.append('\n').appendStackTrace(ex)

		val m = sw.toString()
		if (level >= useStderrFrom)
			System.err.println(m)
		else
			System.out.println(m)
	}

	override fun logErrorOrThrow(tag: Any?, msg: CharSequence?, t: Throwable?, args: Array<out Any>) {
		if (isNotLoggable(LogLevel.ERROR))
			return

		val ex = prepareException(tag, t, args)
		val message = prepareMessage(ex, msg, args)
		throw sanitizeStackTrace(IllegalStateException("${prepareTag(tag)} Error: $message", ex))
	}
}

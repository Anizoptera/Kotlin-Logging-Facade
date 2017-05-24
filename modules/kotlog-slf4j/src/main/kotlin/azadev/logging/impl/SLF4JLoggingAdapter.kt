package azadev.logging.impl

import azadev.logging.LogLevel
import azadev.logging.LogLevel.ERROR
import azadev.logging.LogLevel.VERBOSE
import azadev.logging.LoggingAdapter
import org.slf4j.Logger
import org.slf4j.LoggerFactory


/**
 * Proxies log messages to SLF4J.
 */
class SLF4JLoggingAdapter(
		override var level: Int = LogLevel.ALL
) : LoggingAdapter()
{
	override fun log(tag: Any?, level: Int, msg: CharSequence?, t: Throwable?, args: Array<out Any>) {
		if (isNotLoggable(level))
			return

		val ex = prepareException(tag, t, args)
		val message = prepareMessage(ex, msg, args)?.toString()
				?: return  // Swallow message if it's null and there's no throwable

		val logger = when (tag) {
			is Logger -> tag
			is String -> LoggerFactory.getLogger(tag)
			null -> LoggerFactory.getLogger("null")
			else -> LoggerFactory.getLogger(tag.javaClass)
		}

		when(level) {
			in LogLevel.ALL..LogLevel.VERBOSE -> when {
				ex != null -> logger.trace(message, ex)
				args.isNotEmpty() -> logger.trace(message, *args)
				else -> logger.trace(message)
			}
			in LogLevel.VERBOSE+1..LogLevel.DEBUG -> when {
				ex != null -> logger.debug(message, ex)
				args.isNotEmpty() -> logger.debug(message, *args)
				else -> logger.debug(message)
			}
			in LogLevel.DEBUG+1..LogLevel.INFO -> when {
				ex != null -> logger.info(message, ex)
				args.isNotEmpty() -> logger.info(message, *args)
				else -> logger.info(message)
			}
			in LogLevel.INFO+1..LogLevel.WARN -> when {
				ex != null -> logger.warn(message, ex)
				args.isNotEmpty() -> logger.warn(message, *args)
				else -> logger.warn(message)
			}
			in LogLevel.WARN+1..LogLevel.ALL -> when {
				ex != null -> logger.error(message, ex)
				args.isNotEmpty() -> logger.error(message, *args)
				else -> logger.error(message)
			}
		}
	}

	override fun logErrorOrThrow(tag: Any?, msg: CharSequence?, t: Throwable?, args: Array<out Any>) {
		if (isLoggable(VERBOSE)) {
			val ex = prepareException(tag, t, args)
			val message = prepareMessage(ex, msg, args)
			throw sanitizeStackTrace(IllegalStateException("${prepareTag(tag)} Error: $message", ex))
		}
		log(tag, ERROR, msg, t, args)
	}
}

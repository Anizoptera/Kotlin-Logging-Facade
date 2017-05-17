@file:JvmName("KotLog")
@file:Suppress("unused")

package azadev.logging

import azadev.logging.LogLevel.ASSERT
import azadev.logging.LogLevel.CONFIG
import azadev.logging.LogLevel.DEBUG
import azadev.logging.LogLevel.ERROR
import azadev.logging.LogLevel.INFO
import azadev.logging.LogLevel.VERBOSE
import azadev.logging.LogLevel.WARN
import azadev.logging.impl.LOGGER_INSTANCE
import java.io.*
import java.util.*


inline fun Any.logRaw(level: Int, msgFunction: () -> CharSequence) = LOGGER_INSTANCE.log(this, level, msgFunction)
@JvmName("log") fun Any.logRaw(msg: CharSequence, level: Int) = LOGGER_INSTANCE.log(this, level, msg)
@JvmName("log") fun Any.logRaw(msg: CharSequence, level: Int, ex: Throwable?) = LOGGER_INSTANCE.log(this, level, msg, ex)


inline fun Any.logVerbose(msgFunction: () -> CharSequence) = LOGGER_INSTANCE.log(this, VERBOSE, msgFunction)


inline fun Any.logDebug(msgFunction: () -> CharSequence) = LOGGER_INSTANCE.log(this, DEBUG, msgFunction)
@JvmName("d") fun Any.logDebug(msg: CharSequence) = LOGGER_INSTANCE.log(this, DEBUG, msg)


inline fun Any.logConfig(msgFunction: () -> CharSequence) = LOGGER_INSTANCE.log(this, CONFIG, msgFunction)
fun Any.logConfig(msg: CharSequence) = LOGGER_INSTANCE.log(this, CONFIG, msg)
fun Any.logConfig(msg: CharSequence, ex: Throwable?) = LOGGER_INSTANCE.log(this, CONFIG, msg, ex)


inline fun Any.logInfo(msgFunction: () -> CharSequence) = LOGGER_INSTANCE.log(this, INFO, msgFunction)
@JvmName("i") fun Any.logInfo(msg: CharSequence) = LOGGER_INSTANCE.log(this, INFO, msg)
@JvmName("i") fun Any.logInfo(msg: CharSequence, ex: Throwable?) = LOGGER_INSTANCE.log(this, INFO, msg, ex)


// Use exception as receiver if you don't want it to be sent to Crashlytics, but want to see it in logs
// throwable.logWarning("text")
inline fun Any.logWarning(msgFunction: () -> CharSequence) = LOGGER_INSTANCE.log(this, WARN, msgFunction)
inline fun Any.logWarning(ex: Throwable, msgFunction: () -> CharSequence) = LOGGER_INSTANCE.log(this, WARN, ex, msgFunction)
@JvmName("w") fun Any.logWarning(msg: CharSequence) = LOGGER_INSTANCE.log(this, WARN, msg)
@JvmName("w") fun Any.logWarning(ex: Throwable) = LOGGER_INSTANCE.log(this, WARN, null, ex)
@JvmName("w") fun Any.logWarning(msg: CharSequence?, ex: Throwable?) = LOGGER_INSTANCE.log(this, WARN, msg, ex ?: LoggingAdapter.THROWABLE_STAB)


inline fun Any.logError(msgFunction: () -> CharSequence) = LOGGER_INSTANCE.log(this, ERROR, msgFunction)
inline fun Any.logError(ex: Throwable, msgFunction: () -> CharSequence) = LOGGER_INSTANCE.log(this, ERROR, ex, msgFunction)
@JvmName("e") fun Any.logError(msg: CharSequence) = LOGGER_INSTANCE.log(this, ERROR, msg)
@JvmName("e") fun Any.logError(ex: Throwable) = LOGGER_INSTANCE.log(this, ERROR, null, ex)
@JvmName("e") fun Any.logError(msg: CharSequence?, ex: Throwable?) = LOGGER_INSTANCE.log(this, ERROR, msg, ex)


// Don't inline, otherwise you couldn't jump from logcat to the original place the logger is called from
fun Any.logErrorOrThrow(msg: CharSequence) = LOGGER_INSTANCE.logErrorOrThrow(this, msg)
fun Any.logErrorOrThrow(ex: Throwable) = LOGGER_INSTANCE.logErrorOrThrow(this, null, ex)
fun Any.logErrorOrThrow(msg: CharSequence?, ex: Throwable?) = LOGGER_INSTANCE.logErrorOrThrow(this, msg, ex)


inline fun Any.logWtf(msgFunction: () -> CharSequence) = LOGGER_INSTANCE.log(this, ASSERT, msgFunction)
inline fun Any.logWtf(ex: Throwable, msgFunction: () -> CharSequence) = LOGGER_INSTANCE.log(this, ASSERT, ex, msgFunction)
@JvmName("wtf") fun Any.logWtf(ex: Throwable) = LOGGER_INSTANCE.log(this, ASSERT, null, ex)
@JvmName("wtf") fun Any.logWtf(msg: CharSequence) = LOGGER_INSTANCE.log(this, ASSERT, msg)
@JvmName("wtf") fun Any.logWtf(msg: CharSequence, ex: Throwable?) = LOGGER_INSTANCE.log(this, ASSERT, msg, ex)


/** @see kotlin.jvm.internal.Intrinsics.sanitizeStackTrace */
fun sanitizeStackTrace(throwable: Throwable, classNameToDrop: String): Throwable {
	val stackTrace = throwable.stackTrace
	val size = stackTrace.size

	var lastIntrinsic = -1
	for (i in 0..size-1)
		if (classNameToDrop == stackTrace[i].className)
			lastIntrinsic = i

	val list = Arrays.asList(*stackTrace).subList(lastIntrinsic + 1, size)
	throwable.stackTrace = list.toTypedArray()
	return throwable
}

/** @see timber.log.Timber.Tree.getStackTraceString */
fun getStackTraceString(throwable: Throwable): String {
	val sw = StringWriter(512)
	sw.appendStackTrace(throwable)
	val stack: String? = sw.toString()
	return if (stack != null && stack.isNotEmpty()) stack else
		(throwable.cause ?: throwable).toString()
}

fun Writer.appendStackTrace(throwable: Throwable) {
	val pw = PrintWriter(this, false)
	@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
	(throwable as java.lang.Throwable).printStackTrace(pw)
	pw.flush()
}

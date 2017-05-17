@file:JvmName("KotLog")
@file:Suppress("unused")

package azadev.logging

import azadev.logging.LogLevel.ASSERT
import azadev.logging.LogLevel.CONFIG
import azadev.logging.LogLevel.DEBUG
import azadev.logging.LogLevel.ERROR
import azadev.logging.LogLevel.INFO
import azadev.logging.LogLevel.TRACE
import azadev.logging.LogLevel.VERBOSE
import azadev.logging.LogLevel.WARN
import azadev.logging.impl.LOGGING_ADAPTER
import java.io.*
import java.util.*


inline fun Any.logRaw(level: Int, msgFunction: () -> CharSequence) = LOGGING_ADAPTER.log(this, level, msgFunction)
inline fun Any.logRaw(ex: Throwable, level: Int, msgFunction: () -> CharSequence) = LOGGING_ADAPTER.log(this, level, ex, msgFunction)
@JvmName("log") fun Any.logRaw(msg: CharSequence, level: Int) = LOGGING_ADAPTER.log(this, level, msg)
@JvmName("log") fun Any.logRaw(ex: Throwable, level: Int) = LOGGING_ADAPTER.log(this, level, null, ex)
@JvmName("log") fun Any.logRaw(msg: CharSequence, level: Int, ex: Throwable?) = LOGGING_ADAPTER.log(this, level, msg, ex)


inline fun Any.logTrace(msgFunction: () -> CharSequence) = LOGGING_ADAPTER.log(this, TRACE, msgFunction)
inline fun Any.logTrace(ex: Throwable, msgFunction: () -> CharSequence) = LOGGING_ADAPTER.log(this, TRACE, ex, msgFunction)
@JvmName("t") fun Any.logTrace(msg: CharSequence) = LOGGING_ADAPTER.log(this, TRACE, msg)
@JvmName("t") fun Any.logTrace(ex: Throwable) = LOGGING_ADAPTER.log(this, TRACE, null, ex)
@JvmName("t") fun Any.logTrace(msg: CharSequence, ex: Throwable?) = LOGGING_ADAPTER.log(this, TRACE, msg, ex)


inline fun Any.logVerbose(msgFunction: () -> CharSequence) = LOGGING_ADAPTER.log(this, VERBOSE, msgFunction)
inline fun Any.logVerbose(ex: Throwable, msgFunction: () -> CharSequence) = LOGGING_ADAPTER.log(this, VERBOSE, ex, msgFunction)
@JvmName("v") fun Any.logVerbose(msg: CharSequence) = LOGGING_ADAPTER.log(this, VERBOSE, msg)
@JvmName("v") fun Any.logVerbose(ex: Throwable) = LOGGING_ADAPTER.log(this, VERBOSE, null, ex)
@JvmName("v") fun Any.logVerbose(msg: CharSequence, ex: Throwable?) = LOGGING_ADAPTER.log(this, VERBOSE, msg, ex)


inline fun Any.logDebug(msgFunction: () -> CharSequence) = LOGGING_ADAPTER.log(this, DEBUG, msgFunction)
inline fun Any.logDebug(ex: Throwable, msgFunction: () -> CharSequence) = LOGGING_ADAPTER.log(this, DEBUG, ex, msgFunction)
@JvmName("d") fun Any.logDebug(msg: CharSequence) = LOGGING_ADAPTER.log(this, DEBUG, msg)
@JvmName("d") fun Any.logDebug(ex: Throwable) = LOGGING_ADAPTER.log(this, DEBUG, null, ex)
@JvmName("d") fun Any.logDebug(msg: CharSequence, ex: Throwable?) = LOGGING_ADAPTER.log(this, DEBUG, msg, ex)


inline fun Any.logConfig(msgFunction: () -> CharSequence) = LOGGING_ADAPTER.log(this, CONFIG, msgFunction)
inline fun Any.logConfig(ex: Throwable, msgFunction: () -> CharSequence) = LOGGING_ADAPTER.log(this, CONFIG, ex, msgFunction)
@JvmName("config") fun Any.logConfig(msg: CharSequence) = LOGGING_ADAPTER.log(this, CONFIG, msg)
@JvmName("config") fun Any.logConfig(ex: Throwable) = LOGGING_ADAPTER.log(this, CONFIG, null, ex)
@JvmName("config") fun Any.logConfig(msg: CharSequence, ex: Throwable?) = LOGGING_ADAPTER.log(this, CONFIG, msg, ex)


inline fun Any.logInfo(msgFunction: () -> CharSequence) = LOGGING_ADAPTER.log(this, INFO, msgFunction)
inline fun Any.logInfo(ex: Throwable, msgFunction: () -> CharSequence) = LOGGING_ADAPTER.log(this, INFO, ex, msgFunction)
@JvmName("i") fun Any.logInfo(msg: CharSequence) = LOGGING_ADAPTER.log(this, INFO, msg)
@JvmName("i") fun Any.logInfo(ex: Throwable) = LOGGING_ADAPTER.log(this, INFO, null, ex)
@JvmName("i") fun Any.logInfo(msg: CharSequence, ex: Throwable?) = LOGGING_ADAPTER.log(this, INFO, msg, ex)


// Use exception as receiver if you don't want it to be sent to Crashlytics, but want to see it in logs
// throwable.logWarning("text")
inline fun Any.logWarning(msgFunction: () -> CharSequence) = LOGGING_ADAPTER.log(this, WARN, msgFunction)
inline fun Any.logWarning(ex: Throwable, msgFunction: () -> CharSequence) = LOGGING_ADAPTER.log(this, WARN, ex, msgFunction)
@JvmName("w") fun Any.logWarning(msg: CharSequence) = LOGGING_ADAPTER.log(this, WARN, msg)
@JvmName("w") fun Any.logWarning(ex: Throwable) = LOGGING_ADAPTER.log(this, WARN, null, ex)
@JvmName("w") fun Any.logWarning(msg: CharSequence?, ex: Throwable?) = LOGGING_ADAPTER.log(this, WARN, msg, ex)


inline fun Any.logError(msgFunction: () -> CharSequence) = LOGGING_ADAPTER.log(this, ERROR, msgFunction)
inline fun Any.logError(ex: Throwable, msgFunction: () -> CharSequence) = LOGGING_ADAPTER.log(this, ERROR, ex, msgFunction)
@JvmName("e") fun Any.logError(msg: CharSequence) = LOGGING_ADAPTER.log(this, ERROR, msg)
@JvmName("e") fun Any.logError(ex: Throwable) = LOGGING_ADAPTER.log(this, ERROR, null, ex)
@JvmName("e") fun Any.logError(msg: CharSequence?, ex: Throwable?) = LOGGING_ADAPTER.log(this, ERROR, msg, ex)


// Don't inline, otherwise you couldn't jump from logcat to the original place the logger is called from
@JvmName("errorOrThrow") fun Any.logErrorOrThrow(msg: CharSequence) = LOGGING_ADAPTER.logErrorOrThrow(this, msg)
@JvmName("errorOrThrow") fun Any.logErrorOrThrow(ex: Throwable) = LOGGING_ADAPTER.logErrorOrThrow(this, null, ex)
@JvmName("errorOrThrow") fun Any.logErrorOrThrow(msg: CharSequence?, ex: Throwable?) = LOGGING_ADAPTER.logErrorOrThrow(this, msg, ex)


inline fun Any.logWtf(msgFunction: () -> CharSequence) = LOGGING_ADAPTER.log(this, ASSERT, msgFunction)
inline fun Any.logWtf(ex: Throwable, msgFunction: () -> CharSequence) = LOGGING_ADAPTER.log(this, ASSERT, ex, msgFunction)
@JvmName("wtf") fun Any.logWtf(ex: Throwable) = LOGGING_ADAPTER.log(this, ASSERT, null, ex)
@JvmName("wtf") fun Any.logWtf(msg: CharSequence) = LOGGING_ADAPTER.log(this, ASSERT, msg)
@JvmName("wtf") fun Any.logWtf(msg: CharSequence, ex: Throwable?) = LOGGING_ADAPTER.log(this, ASSERT, msg, ex)


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

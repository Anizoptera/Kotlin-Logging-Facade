package azadev.logging


/**
 * Log levels of KotLog
 *
 * @see org.slf4j.event.Level
 * @see android.util.Log
 * @see java.util.logging.Level
 */
object LogLevel
{
	/**
	 * [OFF] is a special level that can be used to turn off logging.
	 * This level is initialized to _[Integer.MAX_VALUE]_
	 *
	 * @see java.util.logging.Level.OFF
	 */
	const val OFF = Integer.MAX_VALUE

	/**
	 * [ASSERT] is a special message level for cases that shouldn't happen.
	 *
	 * In general [ERROR] messages should describe events that are
	 * of considerable importance and which will prevent normal
	 * program execution. They should be reasonably intelligible
	 * to end users and to system administrators.
	 *
	 * This level is initialized to _1100_
	 *
	 * @see android.util.Log.ASSERT
	 */
	const val ASSERT = 1100

	/**
	 * [ERROR] is a message level indicating a serious failure.
	 *
	 * In general [ERROR] messages should describe events that are
	 * of considerable importance and which will prevent normal
	 * program execution. They should be reasonably intelligible
	 * to end users and to system administrators.
	 *
	 * This level is initialized to _1000_
	 *
	 * @see java.util.logging.Level.SEVERE
	 */
	const val ERROR = 1000

	/**
	 * [WARN] is a message level indicating a potential problem.
	 *
	 * In general [WARN] messages should describe events that will
	 * be of interest to end users or system managers, or which
	 * indicate potential problems.
	 *
	 * This level is initialized to _900_
	 *
	 * @see java.util.logging.Level.WARNING
	 */
	const val WARN = 900

	/**
	 * [INFO] is a message level for informational messages.
	 *
	 * Typically [INFO] messages will be written to the console
	 * or its equivalent. So the [INFO] level should only be
	 * used for reasonably significant messages that will
	 * make sense to end users and system administrators.
	 *
	 * This level is initialized to _800_
	 *
	 * @see java.util.logging.Level.INFO
	 */
	const val INFO = 800

	/**
	 * [CONFIG] is a message level for static configuration messages.
	 *
	 * [CONFIG] messages are intended to provide a variety of static
	 * configuration information, to assist in debugging problems
	 * that may be associated with particular configurations.
	 * For example, [CONFIG] message might include the CPU type,
	 * the graphics depth, the GUI look-and-feel, etc.
	 *
	 * This level is initialized to _700_
	 *
	 * @see java.util.logging.Level.CONFIG
	 */
	const val CONFIG = 700

	/**
	 * [DEBUG] is a message level providing tracing information.
	 *
	 * All of [DEBUG], [VERBOSE], and [TRACE] are intended for relatively
	 * detailed tracing.  The exact meaning of the three levels will
	 * vary between subsystems, but in general, [TRACE] should be used
	 * for the most voluminous detailed output, [VERBOSE] for somewhat
	 * less detailed output, and [DEBUG] for the lowest volume (and
	 * most important) messages.
	 *
	 * In general the [DEBUG] level should be used for information
	 * that will be broadly interesting to developers who do not have
	 * a specialized interest in the specific subsystem.
	 *
	 * [DEBUG] messages might include things like minor (recoverable)
	 * failures.  Issues indicating potential performance problems
	 * are also worth logging as [DEBUG].
	 *
	 * This level is initialized to _500_
	 *
	 * @see java.util.logging.Level.FINE
	 */
	const val DEBUG = 500

	/**
	 * [VERBOSE] indicates a fairly detailed tracing message.
	 * By default logging calls for entering, returning, or throwing
	 * an exception are traced at this level.
	 *
	 * This level is initialized to _400_
	 *
	 * @see java.util.logging.Level.FINER
	 */
	const val VERBOSE = 400

	/**
	 * [TRACE] indicates a highly detailed tracing message.
	 * This level is initialized to _300_
	 *
	 * @see java.util.logging.Level.FINEST
	 */
	const val TRACE = 300

	/**
	 * ALL indicates that all messages should be logged.
	 * This level is initialized to _[Integer.MIN_VALUE]_
	 *
	 * @see java.util.logging.Level.ALL
	 */
	const val ALL = Integer.MIN_VALUE
}

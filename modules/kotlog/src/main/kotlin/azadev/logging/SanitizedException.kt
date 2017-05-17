package azadev.logging


class SanitizedException(msg: String, ex: Throwable?) : RuntimeException(msg, ex)
{
	init { KotLogUtils.sanitizeStackTrace(this) }
}

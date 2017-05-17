package azadev.logging;


/** We can't get current static class of file in kotlin, but it's visible from Java */
public final class KotLogUtils
{
	private KotLogUtils() {}

	static final String LOGGER_CLASS_NAME = KotLog.class.getName();

	public static Throwable sanitizeStackTrace(final Throwable throwable) {
		return KotLog.sanitizeStackTrace(throwable, LOGGER_CLASS_NAME);
	}
}

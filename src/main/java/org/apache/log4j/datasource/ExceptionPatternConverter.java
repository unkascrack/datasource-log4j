package org.apache.log4j.datasource;

import org.apache.log4j.Layout;
import org.apache.log4j.helpers.PatternConverter;
import org.apache.log4j.spi.LoggingEvent;

/**
 *
 *
 * @author <a href="mailto:carlos.alonso.gonzalez@gmail.com">carlos.alonso.gonzalez@gmail.com</a>
 * @version 1.2.1 Fecha: 23/01/2019
 */
public final class ExceptionPatternConverter extends PatternConverter {

	/**
	 *
	 */
	static final String TAB = "\t";

	int maxSizeException;
	int maxTraceException;

	/**
	 * @param formattingInfo
	 * @param maxSizeException
	 * @param maxTraceException
	 */
	ExceptionPatternConverter(final int maxSizeException, final int maxTraceException) {
		super();
		this.maxSizeException = maxSizeException;
		this.maxTraceException = maxTraceException;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.apache.log4j.helpers.PatternConverter#convert(org.apache.log4j.spi.LoggingEvent)
	 */
	protected String convert(final LoggingEvent event) {
		if (event.getThrowableInformation() == null) {
			return StringUtils.EMPTY;
		}

		String exception = convertThrowable(event.getThrowableInformation().getThrowable());
		if (maxSizeException != DataSourceLayout.DEFAULT_MAX_SIZE_EXCEPTION) {
			exception = StringUtils.substring(exception, 0, maxSizeException);
		}
		return StringUtils.escapeSql(exception);
	}

	/**
	 * @param throwableInfo
	 * @return
	 */
	private String convertThrowable(final Throwable throwable) {
		if (throwable == null) {
			return StringUtils.EMPTY;
		}

		final StringBuffer out = new StringBuffer();
		out.append(throwable + Layout.LINE_SEP);

		final StackTraceElement[] stackTraces = throwable.getStackTrace();
		for (int i = 0; i < stackTraces.length
				&& (maxTraceException == DataSourceLayout.DEFAULT_MAX_TRACE_EXCEPTION || i < maxTraceException); i++) {
			final StackTraceElement trace = stackTraces[i];
			out.append(TAB + "at " + trace + Layout.LINE_SEP);
		}

		out.append(convertThrowable(throwable.getCause()));

		return out.toString();
	}

}

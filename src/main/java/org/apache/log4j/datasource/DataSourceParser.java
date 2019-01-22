/**
 *
 */
package org.apache.log4j.datasource;

import org.apache.log4j.helpers.PatternConverter;
import org.apache.log4j.helpers.PatternParser;

/**
 *
 *
 * @author <a href="mailto:carlos.alonso.gonzalez@gmail.com">carlos.alonso.gonzalez@gmail.com</a>
 * @version 1.1 Fecha: 22/01/2019
 */
public final class DataSourceParser extends PatternParser {

	/**
	 *
	 */
	protected int maxSizeMessage;
	protected int maxSizeException;
	protected int maxTraceException;

	/**
	 * @param pattern
	 * @param maxSizeMessage
	 * @param maxSizeException
	 * @param maxTraceException
	 */
	public DataSourceParser(final String pattern,
							final int maxSizeMessage,
							final int maxSizeException,
							final int maxTraceException) {
		super(pattern);
		this.maxSizeMessage = maxSizeMessage;
		this.maxSizeException = maxSizeException;
		this.maxTraceException = maxTraceException;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.apache.log4j.helpers.PatternParser#finalizeConverter(char)
	 */
	protected void finalizeConverter(final char c) {
		PatternConverter pc = null;
		switch (c) {
		case 'e':
			pc = new ExceptionPatternConverter(maxSizeException, maxTraceException);
			addConverter(pc);
			break;
		case 'm':
			pc = new MessagePatternConverter(formattingInfo, maxSizeMessage);
			addConverter(pc);
			break;
		default:
			super.finalizeConverter(c);
			break;
		}
	}
}

package org.apache.log4j.jndi.datasource;

import org.apache.log4j.helpers.FormattingInfo;
import org.apache.log4j.helpers.PatternConverter;
import org.apache.log4j.spi.LoggingEvent;

/**
 *
 *
 * @author <a href="mailto:carlos.alonso.gonzalez@gmail.com">carlos.alonso.gonzalez@gmail.com</a>
 * @version 1.2.1 Fecha: 23/01/2019
 */
final class MessagePatternConverter extends PatternConverter {

	int maxSizeMessage;

	/**
	 * @param formattingInfo
	 * @param maxSizeMessage
	 */
	public MessagePatternConverter(final FormattingInfo formattingInfo, final int maxSizeMessage) {
		super(formattingInfo);
		this.maxSizeMessage = maxSizeMessage;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.apache.log4j.helpers.PatternConverter#convert(org.apache.log4j.spi.LoggingEvent)
	 */
	protected String convert(final LoggingEvent event) {
		String message = event.getRenderedMessage();
		if (maxSizeMessage != DataSourceLayout.DEFAULT_MAX_SIZE_EXCEPTION) {
			message = StringUtils.substring(event.getRenderedMessage(), 0, maxSizeMessage);
		}
		return StringUtils.escapeSql(message);
	}

}

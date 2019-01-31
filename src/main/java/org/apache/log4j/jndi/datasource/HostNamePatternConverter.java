package org.apache.log4j.jndi.datasource;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.helpers.PatternConverter;
import org.apache.log4j.spi.LoggingEvent;

/**
 *
 * @author <a href="mailto:carlos.alonso.gonzalez@gmail.com">carlos.alonso.gonzalez@gmail.com</a>
 * @version 1.2.1 Fecha: 23/01/2019
 */
class HostNamePatternConverter extends PatternConverter {

	protected String convert(final LoggingEvent event) {
		String message = StringUtils.EMPTY;
		try {
			message = InetAddress.getLocalHost().getHostName();
		} catch (final UnknownHostException e) {
		}
		return message;
	}
}

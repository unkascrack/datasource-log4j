package org.apache.log4j.jndi.datasource;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.helpers.PatternConverter;
import org.apache.log4j.spi.LoggingEvent;

class HostNamePatternConverter extends PatternConverter {

	private String hostName;

	public HostNamePatternConverter() {
		try {
			hostName = InetAddress.getLocalHost().getHostName();
		} catch (final UnknownHostException e) {
			hostName = "unknown";
		}
	}

	@Override
	protected String convert(final LoggingEvent event) {
		return hostName;
	}
}

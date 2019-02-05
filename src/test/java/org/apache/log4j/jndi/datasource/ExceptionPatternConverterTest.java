package org.apache.log4j.jndi.datasource;

import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.Assert;
import org.junit.Test;

public class ExceptionPatternConverterTest {

	private final ExceptionPatternConverter converter = new ExceptionPatternConverter(1000, 1000);

	@Test
	public void convert_whenLogEventIsNull_shouldReturnEmptyString() {
		final String result = converter.convert(null);
		Assert.assertNotNull(result);
		Assert.assertTrue(result.length() == 0);
	}

	@Test
	public void convert_whenLogEventIsNoException_shouldReturnEmptyString() {
		final String result = converter.convert(createLoggingEvent(null));
		Assert.assertNotNull(result);
		Assert.assertTrue(result.length() == 0);
	}

	@Test
	public void convert_whenLogEventIsNoException_shouldReturnNotEmptyString() {
		final String result = converter.convert(createLoggingEvent(new RuntimeException("error")));
		Assert.assertNotNull(result);
		Assert.assertFalse(result.length() == 0);
	}

	@SuppressWarnings("deprecation")
	private LoggingEvent createLoggingEvent(final Throwable exception) {
		return new LoggingEvent("fqnOfCategoryClass", Category.getRoot(), Level.ERROR, "message", exception);
	}
}

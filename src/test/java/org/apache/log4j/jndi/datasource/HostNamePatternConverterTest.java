package org.apache.log4j.jndi.datasource;

import org.junit.Assert;
import org.junit.Test;

public class HostNamePatternConverterTest {

	private final HostNamePatternConverter converter = new HostNamePatternConverter();

	@Test
	public void convert_shouldReturnData() {
		final String result = converter.convert(null);
		Assert.assertNotNull(result);
	}
}

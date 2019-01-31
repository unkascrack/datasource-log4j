package org.apache.log4j.jndi.datasource;

import junit.framework.Assert;
import junit.framework.TestCase;

public class HostNamePatternConverterTest extends TestCase {

	private final HostNamePatternConverter converter = new HostNamePatternConverter();

	public void test_convert_shouldReturnData() {
		final String result = converter.convert(null);
		Assert.assertNotNull(result);
	}
}

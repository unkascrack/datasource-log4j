package org.apache.log4j.jndi.datasource;

import org.apache.log4j.jndi.datasource.DataSourceLayout;
import org.junit.Assert;

import junit.framework.TestCase;

public class DataSourceLayoutTest extends TestCase {

	public void test_getSqlPattern_whenPatternIsNotSet_shouldReturnDefaultValue() {
		final DataSourceLayout layout = new DataSourceLayout();
		final String result = layout.getSqlPattern();
		Assert.assertNotNull(result);
		Assert.assertEquals(DataSourceLayout.DEFAULT_CONVERSION_PATTERN, result);
	}

	public void test_getSqlPattern_whenPatternIsSet_shouldReturnValue() {
		final DataSourceLayout layout = new DataSourceLayout("mi pattern");
		final String result = layout.getSqlPattern();
		Assert.assertNotNull(result);
		Assert.assertEquals("mi pattern", result);
	}

}

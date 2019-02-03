package org.apache.log4j.jndi.datasource;

import org.junit.Assert;
import org.junit.Test;

public class DataSourceLayoutTest {

	@Test
	public void getSqlPattern_whenPatternIsNotSet_shouldReturnDefaultValue() {
		final DataSourceLayout layout = new DataSourceLayout();
		final String result = layout.getSqlPattern();
		Assert.assertNotNull(result);
		Assert.assertEquals(DataSourceLayout.DEFAULT_CONVERSION_PATTERN, result);
	}

	@Test
	public void getSqlPattern_whenPatternIsSet_shouldReturnValue() {
		final DataSourceLayout layout = new DataSourceLayout("mi pattern");
		final String result = layout.getSqlPattern();
		Assert.assertNotNull(result);
		Assert.assertEquals("mi pattern", result);
	}

}

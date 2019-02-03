package org.apache.log4j.jndi.datasource;

import org.junit.Assert;
import org.junit.Test;

public class DataSourceParserTest {

	@Test
	public void dataSourcePaserNotNull() {
		Assert.assertNotNull(new DataSourceParser("", 100, 100, 100));
	}
}

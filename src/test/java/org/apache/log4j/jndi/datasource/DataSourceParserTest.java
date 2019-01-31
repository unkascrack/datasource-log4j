package org.apache.log4j.jndi.datasource;

import org.apache.log4j.jndi.datasource.DataSourceParser;

import junit.framework.Assert;
import junit.framework.TestCase;

public class DataSourceParserTest extends TestCase {

    public void testDataSourcePaserNotNull() {
        Assert.assertNotNull(new DataSourceParser("", 100, 100, 100));
    }
}

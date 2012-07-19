package es.map.sgtic.fw.log4j;

import junit.framework.Assert;
import junit.framework.TestCase;

public class DataSourceParserTest extends TestCase {

    public void testDataSourcePaserNotNull() {
        Assert.assertNotNull(new DataSourceParser("", 100, 100, 100));
    }
}

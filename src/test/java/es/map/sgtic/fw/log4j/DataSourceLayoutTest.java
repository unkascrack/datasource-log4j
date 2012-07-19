package es.map.sgtic.fw.log4j;

import junit.framework.Assert;
import junit.framework.TestCase;

public class DataSourceLayoutTest extends TestCase {

    public void testDataSourceLayoutNotNull() {
        Assert.assertNotNull(new DataSourceLayout());
    }

}

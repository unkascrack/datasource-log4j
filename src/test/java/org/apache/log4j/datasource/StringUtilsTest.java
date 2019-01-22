package org.apache.log4j.datasource;

import org.apache.log4j.datasource.StringUtils;

import junit.framework.Assert;
import junit.framework.TestCase;

public class StringUtilsTest extends TestCase {

    public void testSubstringNull() {
        final String result = StringUtils.substring(null, 0, 0);
        Assert.assertNull(result);
    }

    public void testSubstringEmpty() {
        final String result = StringUtils.substring("", 0, 0);
        Assert.assertEquals("", result);
    }

    public void testSubstringIncorrectSizeSubstract() {
        final String result = StringUtils.substring("123456", 6, 10);
        Assert.assertEquals("", result);
    }

    public void testSubstringCorrectSizeSubstrac() {
        final String result = StringUtils.substring("123456", 5, 6);
        Assert.assertEquals("6", result);
    }
}

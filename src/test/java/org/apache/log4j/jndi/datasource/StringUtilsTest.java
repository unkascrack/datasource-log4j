package org.apache.log4j.jndi.datasource;

import org.junit.Assert;
import org.junit.Test;

public class StringUtilsTest {

	@Test
	public void substring_whenValueIsNull_shouldReturnNull() {
		final String result = StringUtils.substring(null, 0, 0);
		Assert.assertNull(result);
	}

	@Test
	public void substring_whenValueIsEmpty_shouldReturnEmpty() {
		final String result = StringUtils.substring("", 0, 0);
		Assert.assertEquals("", result);
	}

	@Test
	public void substring_whenSizeIsIncorrect_shouldReturnEmpty() {
		final String result = StringUtils.substring("123456", 6, 10);
		Assert.assertEquals("", result);
	}

	@Test
	public void substring_whenSizeIsValid_shouldReturnData() {
		final String result = StringUtils.substring("123456", 5, 6);
		Assert.assertEquals("6", result);
	}
}

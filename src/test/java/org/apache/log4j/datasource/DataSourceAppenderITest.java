package org.apache.log4j.datasource;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Appender;
import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.Assert;

import junit.framework.TestCase;

public class DataSourceAppenderITest extends TestCase {

	private static final String APPENDER_NAME = "DATABASE";
	private static final String DEFAULT_SQL_PATTERN = "INSERT INTO LOG (ID, LEVEL, CLASSNAME, MESSAGE, EXCEPTION, DATE_MESSAGE) VALUES (SEQ_LOG.nextval, '%p', '%c', '%m', '%e', SYSTIMESTAMP)";
	private static final int DEFAULT_MAX_SIZE_MESSAGE = 4000;
	private static final int DEFAULT_MAX_SIZE_EXCEPTION = 4000;

	public void test_loadConfigurationFromProgramatic() {
		final DataSourceLayout layout = new DataSourceLayout();
		layout.setSqlPattern(DEFAULT_SQL_PATTERN);
		layout.setMaxSizeMessage(DEFAULT_MAX_SIZE_MESSAGE);
		layout.setMaxSizeException(DEFAULT_MAX_SIZE_EXCEPTION);
		final DataSourceAppender appender = new DataSourceAppender();
		appender.setName(APPENDER_NAME);
		appender.setLayout(layout);
		final Logger logger = Logger.getLogger(DataSourceAppenderITest.class);
		logger.getRootLogger().addAppender(appender);
		validateLogger(logger);
	}

	public void test_loadConfigurationFromXmlFile() {
		DOMConfigurator.configure(getClass().getResource("/logger/log4j.xml"));
		validateLogger(Logger.getLogger(DataSourceAppenderITest.class));
	}

	public void test_loadConfigurationFromPropertyFile() throws IOException {
		final Properties props = new Properties();
		props.load(getClass().getResourceAsStream("/logger/log4j.properties"));
		PropertyConfigurator.configure(props);

		validateLogger(Logger.getLogger(DataSourceAppenderITest.class));
	}

	private void validateLogger(final Logger logger) {
		final Appender appender = logger.getRootLogger().getAppender(APPENDER_NAME);
		Assert.assertNotNull(appender);
		Assert.assertTrue(DataSourceAppender.class.equals(appender.getClass()));
		final Layout layout = appender.getLayout();
		Assert.assertNotNull(layout);
		Assert.assertTrue(DataSourceLayout.class.equals(layout.getClass()));
		Assert.assertNotNull(((DataSourceLayout) layout).getSqlPattern());
		Assert.assertEquals(DEFAULT_SQL_PATTERN, ((DataSourceLayout) layout).getSqlPattern());
		Assert.assertEquals(DEFAULT_MAX_SIZE_MESSAGE, ((DataSourceLayout) layout).getMaxSizeMessage());
		Assert.assertEquals(DEFAULT_MAX_SIZE_EXCEPTION, ((DataSourceLayout) layout).getMaxSizeException());
	}
}

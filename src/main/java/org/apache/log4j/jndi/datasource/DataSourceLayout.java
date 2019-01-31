/**
 *
 */
package org.apache.log4j.jndi.datasource;

import org.apache.log4j.Layout;
import org.apache.log4j.helpers.PatternConverter;
import org.apache.log4j.helpers.PatternParser;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Layout flexible y configurable, igual a <code>PatternLayout</code> de <code>log4j</code>, a&ntilde;adiendo
 * compatibildad con SQL y posibilidad de mostrar trazas de excepciones (<code>%e</code>).
 *
 * <p>
 * El objetivo del layout es formatear un evento log y devolver el resultado como un String. El resultado depende del
 * patr&oacute;n dado, por norma general una sentencia SQL.
 * </p>
 *
 * <p>
 * Par&aacute;metros definidos para la configuraci&oacute;n del layout:
 * <ul>
 * <li><code>sqlPattern</code>, [opcional] consulta que lanzar contra la base de datos.</li>
 * <li><code>maxSizeMessage</code>, [opcional] n&uacute;mero m&aacute;ximo de caracteres a mostrar en el mensaje, por
 * defecto muestra el mensaje completo.</li>
 * <li><code>maxSizeException</code>, [opcional] n&uacute;mero m&aacute;ximo de caracteres a mostrar en la
 * excepci&oacute;n, por defecto muestra la excepci&oacute;n completa.</li>
 * <li><code>maxTraceException</code>, [opcional] n&uacute;mero m&aacute;ximo de trazas por excepci&oacute;n, por
 * defecto se muestran la excepci&oacute;n completa.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Nuevos patrones definidos para este layout:
 * <ul>
 * <li><code>%m</code>, el mensaje del log es editado para poder utilizarlo en un consulta SQL (escapeSQL),
 * adem&aacute;s de poder definir el tama&ntilde;o m&aacute;ximo del mismo, <code>maxSizeMessage</code>.</li>
 * <li><code>%e</code>, muestra la traza completa de una excepci&oacute;n, si el evento log la contiene. Se puede
 * definir el tama&ntilde;o m&aacute;ximo de la excepci&oacute;n, <code>maxSizeException</code>, as&iacute; como el
 * n&uacute;mero de trazas por excepci&oacute;n, <code>maxTraceException</code></li>
 * </ul>
 * </p>
 *
 * <p>
 * Ejemplo de entrada layout en log4j.properties: <br/>
 * <code>
 * <br/>
 * log4j.rootCategory=ERROR, DATABASE<br/>
 * <br/>
 * log4j.appender.DATABASE=org.apache.log4j.datasource.DataSourceAppender<br/>
 * log4j.appender.DATABASE.datasource=java:jdbc/datasource<br/>
 * log4j.appender.DATABASE.bufferSize=1<br/>
 * <b>
 * log4j.appender.DATABASE.layout=org.apache.log4j.datasource.DataSourceLayout<br/>
 * log4j.appender.DATABASE.sqlPattern=INSERT INTO LOG (DE_NIVEL, DE_LOG, DE_EXCEPCION, FE_LOG) VALUES ('%p', '%m', '%e', TO_DATE('%d{yyyy-MM-dd HH:mm:ss}','YYYY-MM-DD HH24:MI:SS'))<br/>
 * log4j.appender.DATABASE.layout.maxSizeMessage=1000<br/>
 * log4j.appender.DATABASE.layout.maxSizeException=4000<br/>
 * log4j.appender.DATABASE.layout.maxTraceException=5<br/>
 * </b>
 * </code>
 * </p>
 *
 * @author <a href="mailto:carlos.alonso.gonzalez@gmail.com">carlos.alonso.gonzalez@gmail.com</a>
 * @version 1.2.1 Fecha: 23/01/2019
 */
public final class DataSourceLayout extends Layout {

	/**
	 *
	 */
	public static final String DEFAULT_CONVERSION_PATTERN = "INSERT INTO LOG (LEVEL, CLASSNAME, MESSAGE, EXCEPTION, DATE_MESSAGE) VALUES ('%p', '%c', '%m', '%e', '%d{dd MMM yyyy HH:mm:ss,SSS}')";
	public static final int DEFAULT_MAX_SIZE_EXCEPTION = -1;
	public static final int DEFAULT_MAX_TRACE_EXCEPTION = -1;

	/**
	 *
	 */
	private String sqlPattern;
	private PatternConverter head;
	private int maxSizeMessage = DEFAULT_MAX_SIZE_EXCEPTION;
	private int maxSizeException = DEFAULT_MAX_SIZE_EXCEPTION;
	private int maxTraceException = DEFAULT_MAX_TRACE_EXCEPTION;

	/**
	 *
	 */
	public DataSourceLayout() {
		this(DEFAULT_CONVERSION_PATTERN);
	}

	/**
	 * @param pattern
	 */
	public DataSourceLayout(final String sqlPattern) {
		this.sqlPattern = sqlPattern;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.apache.log4j.Layout#format(org.apache.log4j.spi.LoggingEvent)
	 */
	public String format(final LoggingEvent event) {
		if (head == null) {
			head = createPatternParser().parse();
		}

		final StringBuffer sbuf = new StringBuffer();
		PatternConverter c = head;
		while (c != null) {
			c.format(sbuf, event);
			c = c.next;
		}
		return sbuf.toString();
	}

	/**
	 * @param pattern
	 * @return
	 */
	private PatternParser createPatternParser() {
		return new DataSourceParser(sqlPattern, maxSizeMessage, maxSizeException, maxTraceException);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.apache.log4j.Layout#ignoresThrowable()
	 */
	public boolean ignoresThrowable() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.apache.log4j.spi.OptionHandler#activateOptions()
	 */
	public void activateOptions() {

	}

	/**
	 * @return the sqlPattern
	 */
	public String getSqlPattern() {
		return sqlPattern;
	}

	/**
	 * @param sqlPattern the sqlPattern to set
	 */
	public void setSqlPattern(final String sqlPattern) {
		this.sqlPattern = sqlPattern;
	}

	/**
	 * @return the maxSizeMessage
	 */
	public int getMaxSizeMessage() {
		return maxSizeMessage;
	}

	/**
	 * @param maxSizeMessage the maxSizeMessage to set
	 */
	public void setMaxSizeMessage(final int maxSizeMessage) {
		this.maxSizeMessage = maxSizeMessage > 0 ? maxSizeMessage : DEFAULT_MAX_SIZE_EXCEPTION;
	}

	/**
	 * @return the maxSizeException
	 */
	public int getMaxSizeException() {
		return maxSizeException;
	}

	/**
	 * @param maxSizeException the maxSizeException to set
	 */
	public void setMaxSizeException(final int maxSizeException) {
		this.maxSizeException = maxSizeException > 0 ? maxSizeException : DEFAULT_MAX_SIZE_EXCEPTION;
	}

	/**
	 * @return the maxTraceException
	 */
	public int getMaxTraceException() {
		return maxTraceException;
	}

	/**
	 * @param maxTraceException the maxTraceException to set
	 */
	public void setMaxTraceException(final int maxTraceException) {
		this.maxTraceException = maxTraceException > 0 ? maxTraceException : DEFAULT_MAX_TRACE_EXCEPTION;
	}
}

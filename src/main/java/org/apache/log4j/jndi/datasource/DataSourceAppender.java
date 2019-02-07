/**
 *
 */
package org.apache.log4j.jndi.datasource;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

/**
 * DataSourceAppender permite escribir los logs a una base de datos, mediante la definici&oacute;n de un
 * <code>datasource</code>.
 *
 * <p>
 * Cada llamada a&ntilde;ade el log a un <code>ArrayList</code> buffer. Cuando el buffer esta completo cada evento log
 * es lanzado como una sentencia SQL (configurable) a la base de datos
 * </p>
 *
 * <p>
 * El appender a invocar para utilizar esta librer&iacute;a debe ser
 * <code>org.apache.log4j.jndi.datasource.DataSourceAppender</code>
 * </p>
 *
 * <p>
 * Par&aacute;metros definidos en la configuraci&oacute;n del appender:
 * <ul>
 * <li><code>datasource</code>, [<b>obligatorio</b>] nombre del datasource.</li>
 * <li><code>layout</code>, [<b>obligatorio</b>] Es necesario definir el layout, siendo posible utilizar cualquiera,
 * aunque es conveniente utilizar el propio layout del appender, <code>DataSourceLayout</code>, el cual dispone de
 * nuevos patrones <code>%e</code> y <code>%m</code>, para la gesti&oacute;n de excepciones.</li>
 * <li><code>bufferSize</code>, [opcional] n&uacute;mero de logs que debe haberse producido para guardarlos en la base
 * de datos, por defecto es 1 (inmediato).</li>
 * </ul>
 * </p>
 *
 * <p>
 * Si durante la carga del datasource, se produce un error, se muestra un mensaje por la consola y el appender deja de
 * funcionar.
 * </p>
 *
 * <p>
 * Si al realizar una insercci&oacute;n en la base de datos se produce un error, muestra la descripci&oacute;n del error
 * por la consola, pero continua con la inserci&oacute;n del resto de logs.
 * </p>
 *
 *
 * <p>
 * Ejemplo de entrada apender en log4j.properties: <br/>
 * <code>
 * <br/>
 * log4j.rootCategory=ERROR, DATABASE<br/>
 * <br/>
 * <b>
 * log4j.appender.DATABASE=org.apache.log4j.jndi.datasource.DataSourceAppender<br/>
 * log4j.appender.DATABASE.jndi=java:jdbc/datasource<br/>
 * log4j.appender.DATABASE.bufferSize=1<br/>
 * log4j.appender.DATABASE.layout=org.apache.log4j.jndi.datasource.DataSourceLayout<br/>
 * log4j.appender.DATABASE.layout.sqlPattern=INSERT INTO LOG (LEVEL, HOSTNAME, MESSAGE, EXCEPTION, DATE_LOG) VALUES ('%p', '%h', '%m', '%e', '%d{yyyy-MM-dd HH:mm:ss}')<br/>
 * log4j.appender.DATABASE.layout.maxSizeMessage=1000<br/>
 * log4j.appender.DATABASE.layout.maxSizeException=4000<br/>
 * log4j.appender.DATABASE.layout.maxTraceException=5<br/>
 * </b>
 * </code>
 * </p>
 * <p>
 * Ejemplo de entrada apender en log4j.xml:<br/>
 * <code>
 * <br/>
 * &lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot;?&gt;<br/>
 * &lt;!DOCTYPE log4j:configuration SYSTEM &quot;log4j.dtd&quot;&gt;<br/>
 * &lt;log4j:configuration debug=&quot;false&quot;&gt;<br/>
 * &lt;appender name=&quot;DATABASE&quot; class=&quot;org.apache.log4j.jndi.datasource.DataSourceAppender&quot;&gt;<br/>
 * &lt;param name=&quot;jndi&quot; value=&quot;jdbc/datasource&quot; /&gt;<br/>
 * &lt;layout class=&quot;org.apache.log4j.jndi.datasource.DataSourceLayout&quot;&gt;<br/>
 * &lt;param name=&quot;sqlPattern&quot; value=&quot;INSERT INTO LOG (LEVEL, HOSTNAME, MESSAGE, EXCEPTION, DATE_LOG) VALUES ('%p', '%h', '%m', '%e', '%d{yyyy-MM-dd HH:mm:ss}')&quot; /&gt;<br/>
 * &lt;param name=&quot;maxSizeMessage&quot; value=&quot;4000&quot; /&gt;<br/>
 * &lt;param name=&quot;maxSizeException&quot; value=&quot;4000&quot; /&gt;<br/>
 * &lt;/layout&gt;<br/>
 * &lt;/appender&gt;<br/>
 * &lt;root&gt;<br/>
 * &lt;appender-ref ref=&quot;DATABASE&quot; /&gt;<br/>
 * &lt;/root&gt;<br/>
 * &lt;/log4j:configuration&gt;<br/>
 * </code>
 * </p>
 */
public final class DataSourceAppender extends AppenderSkeleton {

	private final DataSourceWriter writer = new DataSourceWriter();

	/**
	 * Parametros de configuraci&oacute;n de log4j
	 */
	private String jndi;
	private int bufferSize = 1;

	/**
	 *
	 */
	private List<LoggingEvent> buffer;

	/**
	 *
	 */
	public DataSourceAppender() {
		super();
		buffer = new ArrayList<LoggingEvent>(bufferSize);
	}

	/**
	 * @return the dataSource
	 */
	public String getJndi() {
		return jndi;
	}

	/**
	 * @param jndi the jndi name to set
	 */
	public void setJndi(final String jndi) {
		this.jndi = jndi;
		writer.load(jndi);
	}

	/**
	 * @return the bufferSize
	 */
	public int getBufferSize() {
		return bufferSize;
	}

	/**
	 * @param bufferSize the bufferSize to set
	 */
	public void setBufferSize(final int bufferSize) {
		this.bufferSize = bufferSize > 0 ? bufferSize : 1;
		buffer = new ArrayList<LoggingEvent>(this.bufferSize);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.apache.log4j.AppenderSkeleton#requiresLayout() w
	 */
	public boolean requiresLayout() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.apache.log4j.AppenderSkeleton#append(org.apache.log4j.spi.LoggingEvent)
	 */
	@Override
	public void append(final LoggingEvent logEvent) {
		buffer.add(logEvent);
		if (buffer.size() >= bufferSize) {
			flushBuffer();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.apache.log4j.AppenderSkeleton#close()
	 */
	public void close() {
		flushBuffer();
	}

	/**
	 *
	 */
	private void flushBuffer() {
		try {
			for (final LoggingEvent logEvent : buffer) {
				final String sql = getLayout().format(logEvent);
				writer.write(sql);
			}
		} finally {
			buffer.clear();
		}
	}
}

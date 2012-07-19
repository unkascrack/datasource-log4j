/**
 * 
 */
package es.map.sgtic.fw.log4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Appender;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

/**
 * DataSourceAppender permite escribir los logs a una base de datos, mediante la
 * definici&oacute;n de un <code>datasource</code>.
 * 
 * <p>
 * Cada llamada a&ntilde;ade el log a un <code>ArrayList</code> buffer. Cuando el buffer esta completo cada evento log
 * es lanzado como una sentencia SQL (configurable) a la base de datos
 * </p>
 * 
 * <p>
 * El appender a invocar para utilizar esta librer&iacute;a debe ser
 * <code>es.map.sgtic.fw.log4j.DataSourceAppender</code>
 * </p>
 * 
 * <p>
 * Par&aacute;metros definidos en la configuraci&oacute;n del appender:
 * <ul>
 * <li><code>datasource</code>, [<b>obligatorio</b>] nombre del datasource.</li>
 * <li><code>sql</code>, [<b>obligatorio</b>] consulta que lanzar contra la base de datos.</li>
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
 * log4j.rootCategory=ERROR, DATASOURCE<br/>
 * <br/>
 * <b>
 * log4j.appender.DATASOURCE=es.map.sgtic.fw.log4j.DataSourceAppender<br/>
 * log4j.appender.DATASOURCE.datasource=java:jdbc/datasource<br/>
 * log4j.appender.DATASOURCE.sql=INSERT INTO
 * TBT_LOG (DE_NIVEL, DE_LOG, DE_EXCEPCION, FE_LOG) VALUES
 * ('%p', '%m', '%e', TO_DATE('%d{yyyy-MM-dd
 * HH:mm:ss}','YYYY-MM-DD HH24:MI:SS'))<br/> 
 * log4j.appender.DATASOURCE.bufferSize=1<br/>
 * log4j.appender.DATABASE.layout=es.map.sgtic.fw.log4j.DataSourceLayout<br/>
 * log4j.appender.DATABASE.layout.maxSizeMessage=1000<br/>
 * log4j.appender.DATABASE.layout.maxSizeException=4000<br/>
 * log4j.appender.DATABASE.layout.maxTraceException=5<br/>
 * </b> 
 * </code>
 * </p>
 * 
 * @author <a href="mailto:carlos.alonso.gonzalez@gmail.com">carlos.alonso.gonzalez@gmail.com</a>
 * @version 2.0 Fecha: 19/07/2012
 */
public class DataSourceAppender extends AppenderSkeleton implements Appender {

    /**
     * Parametro que indica si el servicio de log para este apender esta activo
     * o no, se activa en el momento de cargar el contexto del datasource, si
     * este ser realiza correcto su valor será "true", sino sigue a "false"
     */
    private boolean activo = false;

    /**
     * Parametros de configuraci&oacute;n de log4j
     */
    private String dataSource;
    private String sqlStatement;
    private int bufferSize = 1;

    /**
	 * 
	 */
    private DataSource pool;
    private List buffer;

    /**
	 * 
	 */
    public DataSourceAppender() {
        super();
        buffer = new ArrayList(bufferSize);
    }

    /**
     * @return the dataSource
     */
    public String getDatasource() {
        return dataSource;
    }

    /**
     * @param dataSource
     *            the dataSource to set
     */
    public void setDatasource(final String dataSource) {
        this.dataSource = dataSource;

        try {
            final InitialContext context = new InitialContext();
            pool = (DataSource) context.lookup(this.dataSource);
            activo = true;
        } catch (final NamingException e) {
            LogLog.error("No se ha podido encontrar datasource, " + e.getExplanation(), e);
        } catch (final Throwable e) {
            LogLog.error("Datasource " + e.getMessage(), e);
        }
    }

    /**
     * @return the sqlStatement
     */
    public String getSql() {
        return sqlStatement;
    }

    /**
     * @param sql
     *            the sqlStatement to set
     */
    public void setSql(final String sql) {
        if (sql == null) {
            LogLog.error("Falta el parámetro \"sql\" en el appender " + getClass().getName() + ".");
            return;
        }
        sqlStatement = sql;

        if (getLayout() == null) {
            setLayout(new DataSourceLayout(sql));
        } else if (getLayout().getClass().isAssignableFrom(DataSourceLayout.class)) {
            ((DataSourceLayout) getLayout()).setConversionPattern(sql);
        } else {
            ((PatternLayout) getLayout()).setConversionPattern(sql);
        }
    }

    /**
     * @return the bufferSize
     */
    public int getBufferSize() {
        return bufferSize;
    }

    /**
     * @param bufferSize
     *            the bufferSize to set
     */
    public void setBufferSize(final int bufferSize) {
        this.bufferSize = bufferSize > 0 ? bufferSize : 1;
        buffer = new ArrayList(this.bufferSize);
    }

    /*
     * (non-Javadoc)
     * @see org.apache.log4j.AppenderSkeleton#requiresLayout()
     */
    public boolean requiresLayout() {
        return true;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.log4j.AppenderSkeleton#append(org.apache.log4j.spi.LoggingEvent)
     */
    public void append(final LoggingEvent logEvent) {
        if (activo && sqlStatement != null) {
            buffer.add(logEvent);

            if (buffer.size() >= bufferSize) {
                flushBuffer();
            }
        }
    }

    /*
     * (non-Javadoc)
     * @see org.apache.log4j.AppenderSkeleton#close()
     */
    public void close() {
        flushBuffer();
    }

    /**
	 * 
	 */
    private void flushBuffer() {
        if (activo && sqlStatement != null) {
            Connection connection = null;
            try {
                connection = getConnection();

                for (final Iterator iterator = buffer.iterator(); iterator.hasNext();) {
                    final LoggingEvent logEvent = (LoggingEvent) iterator.next();
                    final String sql = getLayout().format(logEvent);
                    try {
                        update(connection, sql);
                    } catch (final SQLException e) {
                        LogLog.error("Guardando LOG database, " + e.getMessage(), e);
                    }
                }
            } catch (final SQLException e) {
                LogLog.error("Obteniendo conexion del datasource, " + e.getMessage(), e);
            } catch (final Throwable e) {
                LogLog.error(e.toString(), e);
            } finally {
                closeQuietly(connection);
                buffer.clear();
            }
        }
    }

    private void update(final Connection connection, final String sql) throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(sql);
            stmt.executeUpdate();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    /**
     * @param connection
     */
    private void closeQuietly(final Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (final SQLException e) {
        }
    }

    /**
     * @return
     * @throws SQLException
     */
    private Connection getConnection() throws SQLException {
        final Connection connection = pool.getConnection();
        connection.setAutoCommit(true);
        return connection;
    }
}
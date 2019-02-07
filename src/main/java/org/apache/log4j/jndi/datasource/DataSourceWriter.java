package org.apache.log4j.jndi.datasource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.helpers.LogLog;

class DataSourceWriter {

	/**
	 *
	 */
	private DataSource dataSouce;

	void load(final String jndi) {
		if (jndi == null || jndi.trim().length() == 0) {
			LogLog.error("No se ha podido recuperar JNDI DataSource: '" + jndi + "'");
			return;
		}
		try {
			final InitialContext context = new InitialContext();
			dataSouce = (DataSource) context.lookup(jndi.trim());
			if (dataSouce == null) {
				LogLog.error("No se ha podido recuperar JNDI Mail Session: '" + jndi.trim() + "'");
			}
		} catch (final Throwable e) {
			LogLog.error("Error obtener JNDI DataSource: " + e.getMessage(), e);
		}
	}

	/**
	 * @return
	 */
	boolean isConnected() {
		return dataSouce != null;
	}

	/**
	 *
	 */
	void write(final String sql) {
		if (dataSouce != null && sql != null) {
			Connection connection = null;
			PreparedStatement statement = null;
			try {
				connection = dataSouce.getConnection();
				connection.setAutoCommit(false);
				statement = connection.prepareStatement(sql);
				statement.executeUpdate();
				connection.commit();
			} catch (final Throwable e) {
				LogLog.error(e.toString(), e);
				try {
					connection.rollback();
				} catch (final SQLException e1) {
				}
			} finally {
				try {
					if (statement != null) {
						statement.close();
					}
					if (connection != null) {
						connection.close();
					}
				} catch (final SQLException e) {
				}
			}
		}
	}
}

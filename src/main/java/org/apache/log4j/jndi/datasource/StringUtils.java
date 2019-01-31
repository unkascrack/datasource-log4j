package org.apache.log4j.jndi.datasource;

/**
 *
 *
 * @author <a href="mailto:carlos.alonso.gonzalez@gmail.com">carlos.alonso.gonzalez@gmail.com</a>
 * @version 1.2.1 Fecha: 23/01/2019
 */
final class StringUtils {

	private StringUtils() {
	}

	/**
	 * 
	 */
	static final String EMPTY = "";

	/**
	 * @param str
	 * @param start
	 * @param end
	 * @return
	 */
	static String substring(final String str, int start, int end) {
		if (str == null) {
			return null;
		}

		if (end < 0) {
			end = str.length() + end;
		}
		if (start < 0) {
			start = str.length() + start;
		}

		if (end > str.length()) {
			end = str.length();
		}

		if (start > end) {
			return EMPTY;
		}

		if (start < 0) {
			start = 0;
		}
		if (end < 0) {
			end = 0;
		}

		return str.substring(start, end);
	}

	/**
	 * @param str
	 * @return
	 */
	static String escapeSql(final String str) {
		return str == null ? null : str.replace("'", "''");
	}
}

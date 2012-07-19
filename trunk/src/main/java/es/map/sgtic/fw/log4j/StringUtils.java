package es.map.sgtic.fw.log4j;

/**
 * 
 * 
 * @author <a href="mailto:carlos.alonso.gonzalez@gmail.com">carlos.alonso.gonzalez@gmail.com</a>
 * @version 2.0 Fecha: 19/07/2012
 */
final class StringUtils {

    private StringUtils() {
    }

    /**
     * 
     */
    public static final String EMPTY = "";

    /**
     * @param str
     * @param start
     * @param end
     * @return
     */
    public static String substring(final String str, int start, int end) {
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
    public static String escapeSql(final String str) {
        return str == null ? null : str.replace("'", "''");
    }
}

package es.map.sgtic.fw.log4j;

import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

public class P6SpyLayout extends PatternLayout {

    private static final String SALTO_LINEA = System.getProperty("line.separator");

    public String format(final LoggingEvent event) {
        String log = super.format(event);
        if (log != null) {
            log = log.replaceAll(SALTO_LINEA, " ");
            log = log.replaceAll("\\t", " ");
            while (log.contains("  ")) {
                log = log.replaceAll("  ", " ");
            }
            log = log.trim() + SALTO_LINEA;
        }
        return log;
    }
}

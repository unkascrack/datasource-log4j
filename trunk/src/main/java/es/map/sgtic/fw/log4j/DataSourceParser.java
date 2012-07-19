/**
 * 
 */
package es.map.sgtic.fw.log4j;

import org.apache.log4j.Layout;
import org.apache.log4j.helpers.FormattingInfo;
import org.apache.log4j.helpers.PatternConverter;
import org.apache.log4j.helpers.PatternParser;
import org.apache.log4j.spi.LoggingEvent;

/**
 * 
 * 
 * @author <a href="mailto:carlos.alonso.gonzalez@gmail.com">carlos.alonso.gonzalez@gmail.com</a>
 * @version 2.0 Fecha: 19/07/2012
 */
public class DataSourceParser extends PatternParser {

    /**
	 * 
	 */
    protected int maxSizeMessage;
    protected int maxSizeException;
    protected int maxTraceException;

    /**
     * @param pattern
     * @param maxSizeMessage
     * @param maxSizeException
     * @param maxTraceException
     */
    public DataSourceParser(final String pattern, final int maxSizeMessage, final int maxSizeException,
            final int maxTraceException) {
        super(pattern);
        this.maxSizeMessage = maxSizeMessage;
        this.maxSizeException = maxSizeException;
        this.maxTraceException = maxTraceException;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.log4j.helpers.PatternParser#finalizeConverter(char)
     */
    protected void finalizeConverter(final char c) {
        PatternConverter pc = null;
        switch (c) {
            case 'e':
                pc = new ExceptionPatternConverter(maxSizeException, maxTraceException);
                addConverter(pc);
            break;
            case 'm':
                pc = new MessagePatternConverter(formattingInfo, maxSizeMessage);
                addConverter(pc);
            break;
            default:
                super.finalizeConverter(c);
            break;
        }
    }

    /**
     * @author Carlos
     * 
     */
    private class MessagePatternConverter extends PatternConverter {

        int maxSizeMessage;

        /**
         * @param formattingInfo
         * @param maxSizeMessage
         */
        public MessagePatternConverter(final FormattingInfo formattingInfo, final int maxSizeMessage) {
            super(formattingInfo);
            this.maxSizeMessage = maxSizeMessage;
        }

        /*
         * (non-Javadoc)
         * @see org.apache.log4j.helpers.PatternConverter#convert(org.apache.log4j.spi.LoggingEvent)
         */
        protected String convert(final LoggingEvent event) {
            String message = event.getRenderedMessage();
            if (maxSizeMessage != DataSourceLayout.DEFAULT_MAX_SIZE_EXCEPTION) {
                message = StringUtils.substring(event.getRenderedMessage(), 0, maxSizeMessage);
            }
            return StringUtils.escapeSql(message);
        }

    }

    /**
     * @author Carlos
     * 
     */
    private class ExceptionPatternConverter extends PatternConverter {

        /**
		 * 
		 */
        static final String TAB = "\t";

        int maxSizeException;
        int maxTraceException;

        /**
         * @param formattingInfo
         * @param maxSizeException
         * @param maxTraceException
         */
        ExceptionPatternConverter(final int maxSizeException, final int maxTraceException) {
            super();
            this.maxSizeException = maxSizeException;
            this.maxTraceException = maxTraceException;
        }

        /*
         * (non-Javadoc)
         * @see org.apache.log4j.helpers.PatternConverter#convert(org.apache.log4j.spi.LoggingEvent)
         */
        protected String convert(final LoggingEvent event) {
            if (event.getThrowableInformation() == null) {
                return StringUtils.EMPTY;
            }

            String exception = convertThrowable(event.getThrowableInformation().getThrowable());
            if (maxSizeException != DataSourceLayout.DEFAULT_MAX_SIZE_EXCEPTION) {
                exception = StringUtils.substring(exception, 0, maxSizeException);
            }
            return StringUtils.escapeSql(exception);
        }

        /**
         * @param throwableInfo
         * @return
         */
        private String convertThrowable(final Throwable throwable) {
            if (throwable == null) {
                return StringUtils.EMPTY;
            }

            final StringBuffer out = new StringBuffer();
            out.append(throwable + Layout.LINE_SEP);

            final StackTraceElement[] stackTraces = throwable.getStackTrace();
            for (int i = 0; i < stackTraces.length
                    && (maxTraceException == DataSourceLayout.DEFAULT_MAX_TRACE_EXCEPTION || i < maxTraceException); i++) {
                final StackTraceElement trace = stackTraces[i];
                out.append(TAB + "at " + trace + Layout.LINE_SEP);
            }

            out.append(convertThrowable(throwable.getCause()));

            return out.toString();
        }
    }
}

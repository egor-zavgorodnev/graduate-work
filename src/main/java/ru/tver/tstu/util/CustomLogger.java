
package   ru.tver.tstu.util;

import ru.tver.tstu.*;
import ru.tver.tstu.controllers.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CustomLogger implements Logger {
    private final MainWindow window;
    private final org.apache.log4j.Logger logger;
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final String loggerName;

    public CustomLogger(String loggerName) {
        this.loggerName = loggerName.substring(loggerName.lastIndexOf(".") + 1);
        this.window = App.getMainWindow();
        this.logger = org.apache.log4j.Logger.getLogger(loggerName);
    }

    @Override
    public void info(String log) {
        logger.info(log);
        appendLogToDisplay(log, LoggingLevel.INFO);
    }

    @Override
    public void debug(String log) {
        logger.debug(log);
        appendLogToDisplay(log, LoggingLevel.DEBUG);
    }

    @Override
    public void error(String log) {
        logger.error(log);
        appendLogToDisplay(log, LoggingLevel.ERROR);
    }

    private void appendLogToDisplay(String log, LoggingLevel loggingLevel) {
        if (window != null) {
            if (loggingLevel.equals(LoggingLevel.ERROR)) {
                window.appendLog(String.format("%s - %s \n", loggingLevel, log));
            } else {
                window.appendLog(String.format("%s \n", log));
            }
        }
        // window.appendLog(String.format("%s - %s \n",loggingLevel,log));
        // window.appendLog(String.format("%s %s %s - %s \n", getDate(),loggingLevel, loggerName,log));
    }

    private String getDate() {
        return dateFormat.format(new Date());
    }

    private enum LoggingLevel {
        INFO, DEBUG, ERROR
    }
}


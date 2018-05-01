package com.phonepe.logger.impl;

import java.time.ZonedDateTime;

import com.phonepe.logger.LogLevel;
import com.phonepe.logger.LogMessage;
import com.phonepe.logger.LogType;
import com.phonepe.logger.sink.Sink;
import com.phonepe.logger.util.Constants;

/**
 * Factory class to create {@link LogMessage}s
 *
 * @author Kaustubh Khasnis
 */
public class LogMessageFactory {
    /**
     * Create a {@link LogMessage} with {@link LogType#USER_LOG}
     *
     * @param logLevel
     *            {@link LogLevel} of this {@link LogMessage}
     * @param date
     *            {@link ZonedDateTime} the {@link LogMessage} was generated on
     * @param namespace
     *            namespace for this {@link LogMessage}
     * @param message
     *            message required for {@link LogMessage}
     * @return {@link LogMessage}
     */
    public LogMessage createLogMessage(LogLevel logLevel, ZonedDateTime date,
                    String namespace, String message) {
        LogMessageImpl logMessageImpl = new LogMessageImpl();
        logMessageImpl.setDate(date);
        logMessageImpl.setLogLevel(logLevel);
        logMessageImpl.setMessage(message);
        logMessageImpl.setNamespace(namespace);
        logMessageImpl.setLogType(LogType.USER_LOG);
        return logMessageImpl;
    }

    /**
     * Create a {@link LogMessage} to indicate {@link Sink}s to close operation
     *
     * @return {@link LogMessage} asking {@link Sink} to close. It has
     *         {@link LogType#FRAMEWORK_LOG} as {@link LogType} and
     *         {@link Constants#CLOSE_SINK_MESSAGE} as message. Rest of the
     *         fields remain uninitialized
     */
    public LogMessage sinkCloseLogMessage() {
        LogMessageImpl logMessageImpl = new LogMessageImpl();
        logMessageImpl.setLogType(LogType.FRAMEWORK_LOG);
        logMessageImpl.setMessage(Constants.CLOSE_SINK_MESSAGE);
        return logMessageImpl;
    }
}

package com.phonepe.logger;

import java.time.ZonedDateTime;

/**
 * Interface representing a single log message internally.
 *
 * @author Kaustubh Khasnis
 */
public interface LogMessage {
    /**
     * {@link LogType} associated with this log. Mandatory field.
     *
     * @return {@link LogType} associated
     */
    public LogType getLogType();

    /**
     * {@link LogLevel} associated with this log. Mandatory for
     * {@link LogType#USER_LOG}s
     *
     * @return {@link LogLevel} associated
     */
    public LogLevel getLogLevel();

    /**
     * {@link ZonedDateTime} associated with this log. Mandatory for
     * {@link LogType#USER_LOG}s
     * 
     * @return {@link ZonedDateTime}
     */
    public ZonedDateTime getDate();

    /**
     * namespace associated with this log. Mandatory for
     * {@link LogType#USER_LOG}s
     * 
     * @return namespace associated
     */
    public String getNamespace();

    /**
     * Message being logged. Mandatory for all kind of logs
     * 
     * @return message
     */
    public String getMessage();
}

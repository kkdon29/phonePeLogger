package com.phonepe.logger;

import java.time.ZonedDateTime;

public interface LogMessage {
    public LogType getLogType();

    public LogLevel getLogLevel();

    public ZonedDateTime getDate();

    public String getNamespace();

    public String getMessage();
}

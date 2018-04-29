package com.phonepe.logger.impl;

import java.time.ZonedDateTime;

import com.phonepe.logger.LogLevel;
import com.phonepe.logger.LogMessage;
import com.phonepe.logger.LogType;
import com.phonepe.logger.util.Constants;

public class LogMessageFactory {
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

    public LogMessage sinkCloseLogMessage() {
        LogMessageImpl logMessageImpl = new LogMessageImpl();
        logMessageImpl.setLogType(LogType.FRAMEWORK_LOG);
        logMessageImpl.setMessage(Constants.CLOSE_SINK_MESSAGE);
        return logMessageImpl;
    }
}

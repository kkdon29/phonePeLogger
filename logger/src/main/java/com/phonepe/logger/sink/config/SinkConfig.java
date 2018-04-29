package com.phonepe.logger.sink.config;

import java.time.format.DateTimeFormatter;
import java.util.List;

import com.phonepe.logger.LogLevel;

public interface SinkConfig {

    DateTimeFormatter getTimeFormat();

    List<LogLevel> getLogLevels();

    String getSinkType();

    boolean isThreadSafe();

    boolean isAsyncMode();

    String getStringProperty(String key);

}
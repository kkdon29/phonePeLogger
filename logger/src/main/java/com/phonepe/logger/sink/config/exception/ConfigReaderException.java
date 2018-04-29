package com.phonepe.logger.sink.config.exception;

public class ConfigReaderException extends RuntimeException {
    public ConfigReaderException(String configFile, Throwable ex) {
        super("Error reading configFile " + configFile, ex);
    }
}

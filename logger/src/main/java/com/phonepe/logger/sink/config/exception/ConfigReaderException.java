package com.phonepe.logger.sink.config.exception;

/**
 * Exception thrown when reading of configuration failed
 * 
 * @author Kaustubh Khasnis
 */
public class ConfigReaderException extends RuntimeException {
    public ConfigReaderException(String configFile, Throwable ex) {
        super("Error reading configFile " + configFile, ex);
    }
}

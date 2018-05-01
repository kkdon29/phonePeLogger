package com.phonepe.logger.sink.config.exception;

/**
 * Exception thrown when configuration provided is invalid
 * 
 * @author Kaustubh Khasnis
 */
public class InvalidConfigException extends RuntimeException {
    public InvalidConfigException(String message) {
        super(message);
    }
}

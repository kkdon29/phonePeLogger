package com.phonepe.logger.sink.config.exception;

public class InvalidConfigException extends RuntimeException {
    public InvalidConfigException(String message) {
        super(message);
    }
}

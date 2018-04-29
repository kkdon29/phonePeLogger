package com.phonepe.logger.sink.exception;

public class SinkLoaderException extends RuntimeException {
    public SinkLoaderException(String className, Throwable ex) {
        super("Error loading class " + className, ex);
    }
}

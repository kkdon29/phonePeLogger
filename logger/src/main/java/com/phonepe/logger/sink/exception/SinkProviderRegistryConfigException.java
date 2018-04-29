package com.phonepe.logger.sink.exception;

public class SinkProviderRegistryConfigException extends RuntimeException {
    public SinkProviderRegistryConfigException(String message) {
        super(message);
    }

    public SinkProviderRegistryConfigException(String message, Throwable ex) {
        super(message, ex);
    }
}

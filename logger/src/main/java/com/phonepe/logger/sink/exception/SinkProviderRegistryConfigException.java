package com.phonepe.logger.sink.exception;

import com.phonepe.logger.sink.SinkProvider;

/**
 * {@link RuntimeException} indicating provided {@link SinkProvider} registry
 * config is invalid
 *
 * @author Kaustubh Khasnis
 */
public class SinkProviderRegistryConfigException extends RuntimeException {
    public SinkProviderRegistryConfigException(String message) {
        super(message);
    }

    public SinkProviderRegistryConfigException(String message, Throwable ex) {
        super(message, ex);
    }
}

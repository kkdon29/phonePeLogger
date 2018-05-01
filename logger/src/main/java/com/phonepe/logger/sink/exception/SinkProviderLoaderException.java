package com.phonepe.logger.sink.exception;

import com.phonepe.logger.sink.SinkProvider;

/**
 * {@link RuntimeException} indicating there was error while loading
 * {@link SinkProvider} at runtime
 *
 * @author Kaustubh Khasnis
 */
public class SinkProviderLoaderException extends RuntimeException {
    public SinkProviderLoaderException(String className, Throwable ex) {
        super("Error loading class " + className, ex);
    }
}

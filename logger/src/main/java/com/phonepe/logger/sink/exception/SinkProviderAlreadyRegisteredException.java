package com.phonepe.logger.sink.exception;

import com.phonepe.logger.sink.SinkProvider;

/**
 * {@link RuntimeException} indicating duplication of SinkTypes in provided
 * sinktype to {@link SinkProvider} mapping
 *
 * @author Kaustubh Khasnis
 */
public class SinkProviderAlreadyRegisteredException extends RuntimeException {

    public SinkProviderAlreadyRegisteredException(String sinkType) {
        super("sink provider " + sinkType + " already registered");
    }
}

package com.phonepe.logger.sink.exception;

public class SinkProviderAlreadyRegisteredException extends RuntimeException {

    public SinkProviderAlreadyRegisteredException(String sinkType) {
        super("sink provider " + sinkType + " already registered");
    }
}

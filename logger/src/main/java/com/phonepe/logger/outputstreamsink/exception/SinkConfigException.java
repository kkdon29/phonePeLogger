package com.phonepe.logger.outputstreamsink.exception;

public class SinkConfigException extends RuntimeException {
    public SinkConfigException(String property) {
        super(property + " not provided in sink config");
    }
}

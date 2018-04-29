package com.phonepe.logger.outputstreamsink.exception;

public class SinkClosedException extends RuntimeException {
    public SinkClosedException(String message) {
        super("Sink is already closed");
    }
}

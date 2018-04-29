package com.phonepe.logger.outputstreamsink.exception;

public class StreamOpenFailedException extends RuntimeException {
    public StreamOpenFailedException(Throwable ex) {
        super("Failed to open stream", ex);
    }
}

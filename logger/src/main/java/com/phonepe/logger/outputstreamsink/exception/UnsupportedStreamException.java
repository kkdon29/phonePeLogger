package com.phonepe.logger.outputstreamsink.exception;

public class UnsupportedStreamException extends RuntimeException {
    public UnsupportedStreamException(String streamType) {
        super("Unsupported stream " + streamType);
    }
}

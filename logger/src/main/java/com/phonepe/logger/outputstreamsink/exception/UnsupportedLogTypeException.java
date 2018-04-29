package com.phonepe.logger.outputstreamsink.exception;

import com.phonepe.logger.LogType;

public class UnsupportedLogTypeException extends RuntimeException {
    public UnsupportedLogTypeException(LogType logType) {
        super(logType + " logtype not supported");
    }

}

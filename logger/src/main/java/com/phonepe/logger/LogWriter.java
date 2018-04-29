package com.phonepe.logger;

import java.io.IOException;

import com.phonepe.logger.outputstreamsink.exception.UnsupportedLogTypeException;

public interface LogWriter {

    void log(LogMessage logMessage)
                    throws IOException, UnsupportedLogTypeException;

}
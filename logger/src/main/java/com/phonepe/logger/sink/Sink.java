package com.phonepe.logger.sink;

import com.phonepe.logger.LogMessage;

public interface Sink {

    public void log(LogMessage logMessage);

    public void close();
}

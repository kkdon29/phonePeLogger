package com.phonepe.logger.sink;

import com.phonepe.logger.LogLevel;
import com.phonepe.logger.LogMessage;
import com.phonepe.logger.LogType;
import com.phonepe.logger.sink.config.SinkConfig;

/**
 * Interface representing a sink, i.e. a specific log store such as file or
 * database etc. Underlying implementation should be supported by
 * {@link SinkConfig} to provide various configurations needed for operation. A
 * {@link Sink} instance may support multiple {@link LogType}.Also different
 * instances of {@link Sink} can support different {@link LogLevel}s even if
 * they are of same {@link SinkConfig#getSinkType()}
 *
 * @author Kaustubh Khasnis
 */
public interface Sink {

    /**
     * Logs given {@link LogMessage} to underlying log store such as file
     *
     * @param logMessage
     *            {@link LogMessage} to log
     */
    public void log(LogMessage logMessage);

    /**
     * Closes this {@link Sink} and underlying log store
     */
    public void close();
}

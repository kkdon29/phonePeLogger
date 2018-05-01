package com.phonepe.logger;

import java.io.IOException;

import com.phonepe.logger.outputstreamsink.exception.UnsupportedLogTypeException;
import com.phonepe.logger.sink.Sink;
import com.phonepe.logger.sink.config.SinkConfig;
import com.phonepe.logger.util.Constants;

/**
 * Interface to facilitate writes to underlying log store such as file or a
 * buffer. The {@link LogWriter} is expected to obey thread safety symantics
 * provided in {@link SinkConfig} for {@link Sink} this {@link LogWriter}
 * corresponds to.
 *
 * @author Kaustubh Khasnis
 */
public interface LogWriter {

    /**
     * Writes the {@link LogMessage} to underlying store.The expectation is, if
     * the {@link LogMessage} is of {@link LogType#USER_LOG} it should be
     * written into store. If its of {@link LogType#FRAMEWORK_LOG} however, the
     * action specified by {@link LogMessage#getMessage()} should be undertaken.
     * E.g. in case of {@link Constants#CLOSE_SINK_MESSAGE}, underlying
     * {@link Sink} or log store needs to be closed. In case of a chain of
     * {@link LogWriter}s and {@link LogReader}s, all these messages also need
     * to be forwarded to next link in the chain
     *
     * @param logMessage
     *            {@link LogMessage} to be written
     * @throws IOException
     *             when there is error writing log to the store
     * @throws UnsupportedLogTypeException
     *             when {@link LogType} of {@link LogMessage} is not supported
     */
    void log(LogMessage logMessage)
                    throws IOException, UnsupportedLogTypeException;

}
package com.phonepe.logger.sink.config;

import java.time.format.DateTimeFormatter;
import java.util.List;

import com.phonepe.logger.LogLevel;
import com.phonepe.logger.LogMessage;
import com.phonepe.logger.sink.Sink;

/**
 * Interface representing a typical {@link Sink} configuration
 *
 * @author Kaustubh Khasnis
 */
public interface SinkConfig {

    /**
     * {@link DateTimeFormatter} using which {@link LogMessage#getDate()} will
     * be serialized
     *
     * @return {@link DateTimeFormatter} instance
     */
    DateTimeFormatter getTimeFormat();

    /**
     * @return {@link List} of {@link LogLevel}s supported by this {@link Sink}
     */
    List<LogLevel> getLogLevels();

    /**
     * @return Type of this {@link Sink} such as console, db , file ,etc
     */
    String getSinkType();

    /**
     * Indicates if {@link Sink} should be threadsafe.
     *
     * @return true/false value indicating same
     */
    boolean isThreadSafe();

    /**
     * Indicates if {@link Sink} should be synchronous/asynchronous.
     *
     * @return true/false value indicating same (true when asynchronous)
     */

    boolean isAsyncMode();

    /**
     * Get a custom property relevant to given {@link Sink} implementation
     *
     * @param key
     *            to fetch the property
     * @return corresponding value
     */
    String getStringProperty(String key);

}
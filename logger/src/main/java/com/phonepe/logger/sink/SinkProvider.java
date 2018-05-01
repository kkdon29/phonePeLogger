package com.phonepe.logger.sink;

import com.phonepe.logger.LogMessage;
import com.phonepe.logger.LogType;
import com.phonepe.logger.impl.LogMessageFactory;
import com.phonepe.logger.sink.config.SinkConfig;
import com.phonepe.logger.sink.config.exception.InvalidConfigException;

/**
 * Interface to create {@link Sink} instances of a certain type.
 *
 * @author Kaustubh Khasnis
 */
public interface SinkProvider {

    /**
     * Creates {@link Sink} using provided {@link SinkConfig}. All {@link Sink}
     * instances returned by this should be having same implementation and
     * {@link SinkConfig#getSinkType()}
     *
     * @param config
     *            {@link SinkConfig} to use
     * @param logMessageFactory
     *            {@link LogMessageFactory} to be used to create internal
     *            {@link LogMessage}s such as {@link LogType#FRAMEWORK_LOG}
     * @return {@link Sink} instance created
     * @throws InvalidConfigException
     *             When provided {@link SinkConfig} is not valid
     */
    public Sink createSink(SinkConfig config,
                    LogMessageFactory logMessageFactory)
                    throws InvalidConfigException;
}

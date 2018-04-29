package com.phonepe.logger.sink;

import com.phonepe.logger.impl.LogMessageFactory;
import com.phonepe.logger.sink.config.SinkConfig;
import com.phonepe.logger.sink.config.exception.InvalidConfigException;

public interface SinkProvider {

    public Sink createSink(SinkConfig config,
                    LogMessageFactory logMessageFactory)
                    throws InvalidConfigException;
}

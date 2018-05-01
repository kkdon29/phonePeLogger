package com.phonepe.logger.sink;

import com.phonepe.logger.impl.LogMessageFactory;
import com.phonepe.logger.sink.config.SinkConfig;

public class SinkProviderImpl implements SinkProvider {

    @Override
    public Sink createSink(SinkConfig config,
                    LogMessageFactory logMessageFactory) {
        return null;
    }

}

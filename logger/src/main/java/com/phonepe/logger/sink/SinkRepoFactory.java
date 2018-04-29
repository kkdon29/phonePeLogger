package com.phonepe.logger.sink;

import java.util.List;
import java.util.Properties;

import com.phonepe.logger.LogLevel;
import com.phonepe.logger.impl.LogMessageFactory;
import com.phonepe.logger.sink.config.SinkConfig;
import com.phonepe.logger.sink.config.SinkConfigReader;
import com.phonepe.logger.sink.config.impl.SinkConfigReaderProvider;

public class SinkRepoFactory {
    public SinkRepo createSinkRepo(Properties loggerProperties,
                    SinkProviderRegistry sinkProviderRegistry,
                    SinkConfigReaderProvider sinkConfigReaderProvider,
                    LogMessageFactory logMessageFactory) {
        SinkRepo sinkRepo = new SinkRepo();
        SinkConfigReader sinkConfigReader = sinkConfigReaderProvider
                        .createSinkConfigReader(loggerProperties);
        List<SinkConfig> sinkConfigs = sinkConfigReader.getConfigs();
        for (SinkConfig sinkConfig : sinkConfigs) {
            SinkProvider sinkProvider = sinkProviderRegistry
                            .getProvider(sinkConfig.getSinkType());
            Sink sink = sinkProvider.createSink(sinkConfig, logMessageFactory);
            for (LogLevel logLevel : sinkConfig.getLogLevels()) {
                sinkRepo.registerSink(logLevel, sink);
            }
        }
        return sinkRepo;
    }
}

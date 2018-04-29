package com.phonepe.logger.sink;

import java.util.List;
import java.util.Properties;

import com.phonepe.logger.LogMessage;
import com.phonepe.logger.impl.LogMessageFactory;
import com.phonepe.logger.sink.config.impl.SinkConfigReaderProvider;

public class SinksManager {

    private SinkProviderRegistry sinkProviderRegistry;
    private SinkRepo             sinkRepo;

    public SinksManager(Properties loggerProperties,
                    SinkProviderRegistryFactory sinkProviderRegistryFactory,
                    SinkRepoFactory sinkRepoFactory,
                    SinkConfigReaderProvider sinkConfigReaderProvider,
                    DynamicSinkProviderLoader dynamicSinkProviderLoader,
                    LogMessageFactory logMessageFactory) {
        this.sinkProviderRegistry = sinkProviderRegistryFactory
                        .createSinkProviderRegistry(loggerProperties,
                                        dynamicSinkProviderLoader);
        this.sinkRepo = sinkRepoFactory.createSinkRepo(loggerProperties,
                        this.sinkProviderRegistry, sinkConfigReaderProvider,
                        logMessageFactory);
    }

    public void log(LogMessage logMessage) {

        List<Sink> sinkList = this.sinkRepo
                        .getSinksForLogLevel(logMessage.getLogLevel());

        for (Sink sink : sinkList) {
            sink.log(logMessage);
        }
    }

    public void close() {
        this.sinkRepo.closeSinks();
    }

}

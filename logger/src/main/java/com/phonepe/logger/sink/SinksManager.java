package com.phonepe.logger.sink;

import java.util.List;
import java.util.Properties;

import com.phonepe.logger.LogLevel;
import com.phonepe.logger.LogMessage;
import com.phonepe.logger.impl.LogMessageFactory;
import com.phonepe.logger.sink.config.impl.SinkConfigReaderProvider;

/**
 * A manager class for all {@link Sink}s.Responsible for initiating
 * {@link SinkProviderRegistry}, {@link SinkRepo} and passing down logs to
 * relevant {@link Sink}s
 *
 * @author Kaustubh Khasnis
 */
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

    /**
     * logs given {@link LogMessage} to relevant {@link Sink}s, i.e.
     * {@link Sink}s interested in corresponding {@link LogLevel}
     *
     * @param logMessage
     *            {@link LogMessage} to log
     */
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

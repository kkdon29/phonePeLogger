package com.phonepe.logger;

import java.time.ZonedDateTime;
import java.util.Properties;

import com.phonepe.logger.impl.LogMessageFactory;
import com.phonepe.logger.sink.DynamicSinkProviderLoader;
import com.phonepe.logger.sink.SinkProviderRegistryFactory;
import com.phonepe.logger.sink.SinkRepoFactory;
import com.phonepe.logger.sink.SinksManager;
import com.phonepe.logger.sink.config.impl.SinkConfigReaderProvider;
import com.phonepe.logger.util.Constants;

public class Logger {
    private static Logger       logger = null;
    private static final Object lock   = new Object();

    private SinksManager      sinksManager;
    private LogMessageFactory logMessageFactory;

    private Logger() {
        Properties loggerProperties = new Properties();
        String sinkConfigFile = System.getProperty(
                        Constants.CONFIG_FILE_LOCATION_KEY,
                        Constants.DEFAULT_CONFIG_FILE_LOCATION);
        loggerProperties.put(Constants.CONFIG_FILE_LOCATION_KEY,
                        sinkConfigFile);
        loggerProperties.put(Constants.SINK_PROVIDER_REGISTRY_FILE_KEY, System
                        .getProperty(Constants.SINK_PROVIDER_REGISTRY_FILE_KEY,
                                        Constants.DEFAULT_SINK_PROVIDER_CONFIG));

        SinkProviderRegistryFactory sinkProviderRegistryFactory = new SinkProviderRegistryFactory();
        SinkRepoFactory sinkRepoFactory = new SinkRepoFactory();
        SinkConfigReaderProvider sinkConfigReaderProvider = new SinkConfigReaderProvider();
        DynamicSinkProviderLoader dynamicSinkProviderLoader = new DynamicSinkProviderLoader();
        this.logMessageFactory = new LogMessageFactory();

        this.sinksManager = new SinksManager(loggerProperties,
                        sinkProviderRegistryFactory, sinkRepoFactory,
                        sinkConfigReaderProvider, dynamicSinkProviderLoader,
                        this.logMessageFactory);

    }

    public void close() {
        this.sinksManager.close();
    }

    public static Logger getLogger() {
        if (Logger.logger != null) {
            return Logger.logger;
        }
        synchronized (Logger.lock) {
            if (Logger.logger == null) {
                Logger.logger = new Logger();
            }
            return Logger.logger;
        }
    }

    public void log(LogLevel level, String namespace, String message) {
        LogMessage logMessage = this.logMessageFactory.createLogMessage(level,
                        ZonedDateTime.now(), namespace, message);
        this.sinksManager.log(logMessage);
    }
}

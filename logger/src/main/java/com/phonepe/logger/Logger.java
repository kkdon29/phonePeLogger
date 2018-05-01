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

/**
 * Top level class for logging. I hate that its singleton, but I dont see any
 * other way around with the lack of DI framework
 * Applications using library are expected to interact with only this class. The
 * class expects couple of system properties to be set already dictated by
 * {@link Constants#CONFIG_FILE_LOCATION_KEY}
 * Constants.CONFIG_FILE_LOCATION_KEY and
 * {@link Constants#SINK_PROVIDER_REGISTRY_FILE_KEY}
 * In absence of these properties defaults are used. {@link
 * Constants#DEFAULT_CONFIG_FILE_LOCATION} and {@link
 * Constants#DEFAULT_SINK_PROVIDER_CONFIG}
 * The main job of this class is to interact with {@link SinksManager} and deal
 * with its lifecycle.
 *
 * @author Kaustubh Khasnis
 */
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

    /**
     * Closes the underlying resources such as {@link SinksManager}. Resets the
     * singleton. Applications are expected to clear their cached Logger
     * instances after calling this.
     */
    public void close() {
        this.sinksManager.close();
        Logger.logger = null;
    }

    /**
     * Singleton method to return instance of logger.
     *
     * @return {@link Logger}
     */
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

    /**
     * Logs a log message. Constructs the {@link LogMessage} and passes it down
     * to {@link SinksManager}
     *
     * @param level
     *            {@link LogLevel} of this log
     * @param namespace
     *            Namespace for the log
     * @param message
     *            Actual message to log
     */
    public void log(LogLevel level, String namespace, String message) {
        LogMessage logMessage = this.logMessageFactory.createLogMessage(level,
                        ZonedDateTime.now(), namespace, message);
        this.sinksManager.log(logMessage);
    }
}

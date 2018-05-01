package com.phonepe.logger.sink.config.impl;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.phonepe.logger.LogLevel;
import com.phonepe.logger.sink.config.SinkConfig;
import com.phonepe.logger.sink.config.exception.InvalidConfigException;
import com.phonepe.logger.util.Constants;
import com.phonepe.logger.util.Utils;

/**
 * {@link SinkConfig} populated using {@link Properties} object.
 *
 * @author Kaustubh Khasnis
 */
class PropertiesBasedSinkConfig implements SinkConfig {
    private DateTimeFormatter   timeFormat;
    private List<LogLevel>      logLevels;
    private String              sinkType;
    private boolean             threadSafe;
    private boolean             asyncMode;
    private Map<String, String> customConfig;

    /**
     * Create the {@link SinkConfig} using {@link Properties} provided.
     *
     * @param sinkProperties
     *            {@link Properties} to be used
     * @param configId
     *            Id of the {@link SinkConfig} being read (such as section no in
     *            properties file)
     * @throws InvalidConfigException
     *             when config provided is invalid. This happens when some value
     *             so missing or malformed
     */
    PropertiesBasedSinkConfig(Properties sinkProperties, int configId)
                    throws InvalidConfigException {
        /*
         * Create a local copy since provided Properties object will be changed
         * internally.
         */
        Properties localSinkProperties = new Properties();
        localSinkProperties.putAll(sinkProperties);
        String timeFormat = localSinkProperties
                        .getProperty(Constants.TS_FORMAT_KEY);
        if (Utils.checkNullOrEmpty(timeFormat)) {
            throw new InvalidConfigException("Config no " + configId
                            + " missing " + Constants.TS_FORMAT_KEY);
        }
        this.timeFormat = DateTimeFormatter.ofPattern(timeFormat);
        /*
         * each time we process a key, we remove it from properties object so
         * that it doesn't go into custom properties map (variable config)
         */
        localSinkProperties.remove(Constants.TS_FORMAT_KEY);

        String logLevels = localSinkProperties
                        .getProperty(Constants.LOG_LEVEL_KEY);
        if (Utils.checkNullOrEmpty(logLevels)) {
            throw new InvalidConfigException("Config no " + configId
                            + " missing " + Constants.LOG_LEVEL_KEY);
        }
        logLevels = logLevels.toUpperCase();
        this.logLevels = new ArrayList<>();
        /*
         * Loglevels are separated by comma. Splithem using same to get
         * individual LogLevel list
         */
        for (String logLevel : logLevels.split(",")) {
            this.logLevels.add(LogLevel.valueOf(logLevel.trim()));
        }
        localSinkProperties.remove(Constants.LOG_LEVEL_KEY);

        this.sinkType = localSinkProperties
                        .getProperty(Constants.SINK_TYPE_KEY);
        if (this.sinkType == null) {
            throw new InvalidConfigException("Config no " + configId
                            + " missing " + Constants.SINK_TYPE_KEY);
        }
        this.sinkType = this.sinkType.toLowerCase();
        localSinkProperties.remove(Constants.SINK_TYPE_KEY);

        String threadMode = localSinkProperties.getProperty(
                        Constants.THREAD_MODEL_KEY,
                        Constants.DEFAULT_THREAD_MODE);
        switch (threadMode) {
            case Constants.SINGLE_THREADED_MODE:
                this.threadSafe = false;
                break;
            case Constants.MULTI_THREADED_MODE:
                this.threadSafe = true;
                break;
            default:
                throw new InvalidConfigException("Invalid value " + threadMode
                                + " for key " + Constants.THREAD_MODEL_KEY
                                + " for config no " + configId);
        }

        String asyncMode = localSinkProperties.getProperty(
                        Constants.WRITE_MODE_KEY, Constants.DEFAULT_WRITE_MODE);
        switch (asyncMode) {
            case Constants.ASYNC_WRITE_MODE:
                this.asyncMode = true;
                break;
            case Constants.SYNC_WRITE_MODE:
                this.asyncMode = false;
                break;
            default:
                throw new InvalidConfigException("Invalid value " + asyncMode
                                + " for key " + Constants.WRITE_MODE_KEY
                                + " for config no " + configId);
        }
        this.customConfig = new HashMap<>();

        for (Map.Entry<Object, Object> entry : localSinkProperties.entrySet()) {
            this.customConfig.put(entry.getKey().toString(),
                            entry.getValue().toString());
        }
    }

    @Override
    public DateTimeFormatter getTimeFormat() {
        return this.timeFormat;
    }

    @Override
    public List<LogLevel> getLogLevels() {
        return this.logLevels;
    }

    @Override
    public String getSinkType() {
        return this.sinkType;
    }

    @Override
    public boolean isThreadSafe() {
        return this.threadSafe;
    }

    @Override
    public boolean isAsyncMode() {
        return this.asyncMode;
    }

    @Override
    public String getStringProperty(String key) {
        return this.customConfig.get(key);
    }

}

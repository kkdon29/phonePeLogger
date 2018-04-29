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

class PropertiesBasedSinkConfig implements SinkConfig {
    private static final String DEFAULT_THREAD_MODE = Constants.SINGLE_THREADED_MODE;
    private static final String DEFAULT_WRITE_MODE  = Constants.SYNC_WRITE_MODE;

    private DateTimeFormatter   timeFormat;
    private List<LogLevel>      logLevels;
    private String              sinkType;
    private boolean             threadSafe;
    private boolean             asyncMode;
    private Map<String, String> config;

    PropertiesBasedSinkConfig(Properties sinkProperties, int configId)
                    throws InvalidConfigException {
        String timeFormat = sinkProperties.getProperty(Constants.TS_FORMAT_KEY);
        if (Utils.checkNullOrEmpty(timeFormat)) {
            throw new InvalidConfigException("Config no " + configId
                            + " missing " + Constants.TS_FORMAT_KEY);
        }
        this.timeFormat = DateTimeFormatter.ofPattern(timeFormat);
        sinkProperties.remove(Constants.TS_FORMAT_KEY);

        String logLevels = sinkProperties.getProperty(Constants.LOG_LEVEL_KEY);
        if (Utils.checkNullOrEmpty(logLevels)) {
            throw new InvalidConfigException("Config no " + configId
                            + " missing " + Constants.LOG_LEVEL_KEY);
        }
        logLevels = logLevels.toUpperCase();
        this.logLevels = new ArrayList<>();
        for (String logLevel : logLevels.split(",")) {
            this.logLevels.add(LogLevel.valueOf(logLevel.trim()));
        }
        sinkProperties.remove(Constants.LOG_LEVEL_KEY);

        this.sinkType = sinkProperties.getProperty(Constants.SINK_TYPE_KEY);
        if (this.sinkType == null) {
            throw new InvalidConfigException("Config no " + configId
                            + " missing " + Constants.SINK_TYPE_KEY);
        }
        this.sinkType = this.sinkType.toLowerCase();
        sinkProperties.remove(Constants.SINK_TYPE_KEY);

        String threadMode = sinkProperties.getProperty(
                        Constants.THREAD_MODEL_KEY,
                        PropertiesBasedSinkConfig.DEFAULT_THREAD_MODE);
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

        String asyncMode = sinkProperties.getProperty(Constants.WRITE_MODE_KEY,
                        PropertiesBasedSinkConfig.DEFAULT_WRITE_MODE);
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
        this.config = new HashMap<>();

        for (Map.Entry<Object, Object> entry : sinkProperties.entrySet()) {
            this.config.put(entry.getKey().toString(),
                            entry.getValue().toString());
        }
    }

    /*
     * (non-Javadoc)
     * @see com.phonepe.logger.sink.config.impl.SinkConfig#getTimeFormat()
     */
    @Override
    public DateTimeFormatter getTimeFormat() {
        return this.timeFormat;
    }

    /*
     * (non-Javadoc)
     * @see com.phonepe.logger.sink.config.impl.SinkConfig#getLogLevel()
     */
    @Override
    public List<LogLevel> getLogLevels() {
        return this.logLevels;
    }

    /*
     * (non-Javadoc)
     * @see com.phonepe.logger.sink.config.impl.SinkConfig#getSinkType()
     */
    @Override
    public String getSinkType() {
        return this.sinkType;
    }

    /*
     * (non-Javadoc)
     * @see com.phonepe.logger.sink.config.impl.SinkConfig#isThreadSafe()
     */
    @Override
    public boolean isThreadSafe() {
        return this.threadSafe;
    }

    /*
     * (non-Javadoc)
     * @see com.phonepe.logger.sink.config.impl.SinkConfig#isAsyncMode()
     */
    @Override
    public boolean isAsyncMode() {
        return this.asyncMode;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.phonepe.logger.sink.config.impl.SinkConfig#getStringProperty(java.
     * lang.
     * String)
     */
    @Override
    public String getStringProperty(String key) {
        return this.config.get(key);
    }

}

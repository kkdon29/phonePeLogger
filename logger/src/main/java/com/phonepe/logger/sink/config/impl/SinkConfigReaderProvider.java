package com.phonepe.logger.sink.config.impl;

import java.util.Properties;

import com.phonepe.logger.sink.config.SinkConfigReader;
import com.phonepe.logger.sink.config.exception.ConfigReaderException;
import com.phonepe.logger.sink.config.exception.InvalidConfigException;
import com.phonepe.logger.util.Constants;

public class SinkConfigReaderProvider {

    public SinkConfigReader createSinkConfigReader(Properties loggerProperties)
                    throws ConfigReaderException, InvalidConfigException {
        String configFileLocation = loggerProperties
                        .getProperty(Constants.CONFIG_FILE_LOCATION_KEY);
        SinkConfigReader sinkConfigReader = new PropertyFileSinkConfigReader(
                        configFileLocation);
        return sinkConfigReader;
    }
}

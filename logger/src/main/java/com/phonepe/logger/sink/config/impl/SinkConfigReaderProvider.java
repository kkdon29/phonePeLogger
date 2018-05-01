package com.phonepe.logger.sink.config.impl;

import java.util.Properties;

import com.phonepe.logger.sink.config.SinkConfig;
import com.phonepe.logger.sink.config.SinkConfigReader;
import com.phonepe.logger.sink.config.exception.ConfigReaderException;
import com.phonepe.logger.sink.config.exception.InvalidConfigException;
import com.phonepe.logger.util.Constants;

/**
 * Factory class for {@link SinkConfigReader}
 *
 * @author Kaustubh Khasnis
 */
public class SinkConfigReaderProvider {

    /**
     * Creates {@link SinkConfigReader} using provided framework level
     * {@link Properties} object. Currently it supports only
     * {@link PropertyFileSinkConfigReader}. It uses
     * {@link Constants#CONFIG_FILE_LOCATION_KEY} from it to figure out location
     * of configuration file storing {@link SinkConfig}s and constructs
     * {@link PropertyFileSinkConfigReader}.
     *
     * @param loggerProperties
     *            Framework level {@link Properties} object
     * @return {@link SinkConfigReader} created {@link SinkConfigReader}
     * @throws ConfigReaderException
     *             When there is error reading config.
     * @throws InvalidConfigException
     *             when the provided configuration is invalid
     */
    public SinkConfigReader createSinkConfigReader(Properties loggerProperties)
                    throws ConfigReaderException, InvalidConfigException {
        String configFileLocation = loggerProperties
                        .getProperty(Constants.CONFIG_FILE_LOCATION_KEY);
        SinkConfigReader sinkConfigReader = new PropertyFileSinkConfigReader(
                        configFileLocation);
        return sinkConfigReader;
    }
}

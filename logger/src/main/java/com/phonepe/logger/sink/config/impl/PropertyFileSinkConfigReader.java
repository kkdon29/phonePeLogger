package com.phonepe.logger.sink.config.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.phonepe.logger.sink.config.SinkConfig;
import com.phonepe.logger.sink.config.SinkConfigReader;
import com.phonepe.logger.sink.config.exception.ConfigReaderException;
import com.phonepe.logger.sink.config.exception.InvalidConfigException;

/**
 * A {@link SinkConfigReader} that parses properties file to create
 * {@link SinkConfig}s required. Internally it requires the {@link SinkConfig}
 * sections in properties file to be separated by one or more blank lines. Each
 * section must represent {@link SinkConfig} in properties file format i.e. in
 * terms of key-value pairs
 *
 * @author Kaustubh Khasnis
 */
class PropertyFileSinkConfigReader implements SinkConfigReader {
    private List<SinkConfig> configs;

    private void processSinkConfigFromString(String configString)
                    throws IOException {
        if (configString.isEmpty()) {
            return;
        }
        /*
         * Create a StringReader from given string so that it can be used as
         * argument to Properties.load method.
         */
        try (StringReader stringReader = new StringReader(configString)) {
            Properties sinkProperties = new Properties();
            sinkProperties.load(stringReader);
            this.configs.add(new PropertiesBasedSinkConfig(sinkProperties,
                            this.configs.size() + 1));
        }
    }

    /**
     * Loads the properties file at specified configFileLocation to generate a
     * lost of {@link SinkConfig}s
     *
     * @param configFileLocation
     *            location of properties file
     * @throws ConfigReaderException
     *             When there is an error reading the properties file
     * @throws InvalidConfigException
     *             When the provided configuration is invalid
     */
    public PropertyFileSinkConfigReader(String configFileLocation)
                    throws ConfigReaderException, InvalidConfigException {
        try (BufferedReader br = new BufferedReader(
                        new FileReader(configFileLocation))) {
            this.configs = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            while (true) {
                /*
                 * Read the properties file section( separated from other
                 * section with one or more blank lines) into a string first.
                 * ONce it has been read, call processSinkConfigFromString on it
                 * to get the SinkConfig object
                 */
                String line = br.readLine();
                if (line == null) {
                    this.processSinkConfigFromString(sb.toString());
                    break;
                }
                line = line.trim();
                if (!line.isEmpty()) {
                    sb.append(line).append('\n');
                    continue;
                }
                this.processSinkConfigFromString(sb.toString());
                sb = new StringBuilder();
            }
        }
        catch (IOException ex) {
            throw new ConfigReaderException(configFileLocation, ex);
        }
    }

    @Override
    public List<SinkConfig> getConfigs() {
        return this.configs;
    }
}

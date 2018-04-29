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

class PropertyFileSinkConfigReader implements SinkConfigReader {
    private List<SinkConfig> configs;

    private void processSinkConfigFromString(String configString)
                    throws IOException {
        if (configString.isEmpty()) {
            return;
        }
        try (StringReader stringReader = new StringReader(configString)) {
            Properties sinkProperties = new Properties();
            sinkProperties.load(stringReader);
            this.configs.add(new PropertiesBasedSinkConfig(sinkProperties,
                            this.configs.size() + 1));
        }
    }

    public PropertyFileSinkConfigReader(String configFileLocation)
                    throws ConfigReaderException, InvalidConfigException {
        try (BufferedReader br = new BufferedReader(
                        new FileReader(configFileLocation))) {
            this.configs = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            while (true) {
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

    /*
     * (non-Javadoc)
     * @see com.phonepe.logger.sink.config.SinkConfigReader#getConfigs()
     */
    @Override
    public List<SinkConfig> getConfigs() {
        return this.configs;
    }
}

package com.phonepe.logger.sink.config.impl;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

import com.phonepe.logger.LogLevel;
import com.phonepe.logger.sink.config.SinkConfig;
import com.phonepe.logger.sink.config.SinkConfigReader;
import com.phonepe.logger.util.Constants;

public class SinkConfigReaderProviderTest {
    @Test
    public void testCreateSinkConfigReader() {
        SinkConfigReaderProvider sinkConfigReaderProvider = new SinkConfigReaderProvider();
        Properties loggerProperties = new Properties();
        loggerProperties.put(Constants.CONFIG_FILE_LOCATION_KEY,
                        "src/test/resources/validSingleSinkConfig.properties");
        SinkConfigReader sinkConfigReader = sinkConfigReaderProvider
                        .createSinkConfigReader(loggerProperties);
        List<SinkConfig> configs = sinkConfigReader.getConfigs();
        Assert.assertEquals(1, configs.size());
        SinkConfig sinkConfig = configs.get(0);
        Assert.assertEquals(
                        DateTimeFormatter.ofPattern("dd:MM:yy hh:mm:ss")
                                        .toString(),
                        sinkConfig.getTimeFormat().toString());

        Assert.assertArrayEquals(
                        new LogLevel[] { LogLevel.INFO, LogLevel.ERROR,
                                        LogLevel.DEBUG },
                        sinkConfig.getLogLevels().toArray(new LogLevel[0]));
        Assert.assertEquals("file", sinkConfig.getSinkType());
        Assert.assertTrue(sinkConfig.isThreadSafe());
        Assert.assertTrue(sinkConfig.isAsyncMode());

        Assert.assertEquals("customvalue1",
                        sinkConfig.getStringProperty("custom_config1"));
        Assert.assertEquals("customvalue2",
                        sinkConfig.getStringProperty("custom_config2"));

    }

}

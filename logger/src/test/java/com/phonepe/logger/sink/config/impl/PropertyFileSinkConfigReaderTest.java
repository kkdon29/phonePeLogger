package com.phonepe.logger.sink.config.impl;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.phonepe.logger.LogLevel;
import com.phonepe.logger.sink.config.SinkConfig;
import com.phonepe.logger.sink.config.SinkConfigReader;
import com.phonepe.logger.sink.config.exception.ConfigReaderException;

public class PropertyFileSinkConfigReaderTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testPropertyFileSinkConfigReaderSingleConfig() {
        SinkConfigReader sinkConfigReader = new PropertyFileSinkConfigReader(
                        "src/test/resources/validSingleSinkConfig.properties");
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

    @Test
    public void testPropertyFileSinkConfigReaderMultipleConfigs() {
        SinkConfigReader sinkConfigReader = new PropertyFileSinkConfigReader(
                        "src/test/resources/validMultipleSinkConfigs.properties");
        List<SinkConfig> configs = sinkConfigReader.getConfigs();
        Assert.assertEquals(4, configs.size());
        SinkConfig sinkConfig1 = configs.get(0);
        Assert.assertEquals(
                        DateTimeFormatter.ofPattern("dd:MM:yy hh:mm:ss")
                                        .toString(),
                        sinkConfig1.getTimeFormat().toString());

        Assert.assertArrayEquals(
                        new LogLevel[] { LogLevel.INFO, LogLevel.ERROR,
                                        LogLevel.DEBUG },
                        sinkConfig1.getLogLevels().toArray(new LogLevel[0]));
        Assert.assertEquals("file", sinkConfig1.getSinkType());
        Assert.assertFalse(sinkConfig1.isThreadSafe());
        Assert.assertTrue(sinkConfig1.isAsyncMode());

        Assert.assertEquals("customvalue1",
                        sinkConfig1.getStringProperty("custom_config1"));
        Assert.assertEquals("customvalue2",
                        sinkConfig1.getStringProperty("custom_config2"));

        SinkConfig sinkConfig2 = configs.get(1);
        Assert.assertEquals(
                        DateTimeFormatter.ofPattern("dd:MM:yy hh:mm:ss")
                                        .toString(),
                        sinkConfig2.getTimeFormat().toString());
        Assert.assertArrayEquals(new LogLevel[] { LogLevel.FATAL },
                        sinkConfig2.getLogLevels().toArray(new LogLevel[0]));
        Assert.assertEquals("console", sinkConfig2.getSinkType());
        Assert.assertFalse(sinkConfig2.isThreadSafe());
        Assert.assertTrue(sinkConfig2.isAsyncMode());

        Assert.assertEquals("customvalue1",
                        sinkConfig2.getStringProperty("custom_config1"));
        Assert.assertEquals("customvalue2",
                        sinkConfig2.getStringProperty("custom_config2"));

        SinkConfig sinkConfig3 = configs.get(2);
        Assert.assertEquals(
                        DateTimeFormatter.ofPattern("dd:MM:yy hh:mm:ss")
                                        .toString(),
                        sinkConfig3.getTimeFormat().toString());
        Assert.assertArrayEquals(
                        new LogLevel[] { LogLevel.ERROR, LogLevel.FATAL,
                                        LogLevel.WARN },
                        sinkConfig3.getLogLevels().toArray(new LogLevel[0]));
        Assert.assertEquals("db", sinkConfig3.getSinkType());
        Assert.assertTrue(sinkConfig3.isThreadSafe());
        Assert.assertTrue(sinkConfig3.isAsyncMode());

        SinkConfig sinkConfig4 = configs.get(3);
        Assert.assertEquals(
                        DateTimeFormatter.ofPattern("dd:MM:yy hh:mm:ss")
                                        .toString(),
                        sinkConfig4.getTimeFormat().toString());
        Assert.assertArrayEquals(new LogLevel[] { LogLevel.INFO },
                        sinkConfig4.getLogLevels().toArray(new LogLevel[0]));
        Assert.assertEquals("db", sinkConfig4.getSinkType());
        Assert.assertFalse(sinkConfig4.isThreadSafe());
        Assert.assertFalse(sinkConfig4.isAsyncMode());
    }

    @Test(expected = ConfigReaderException.class)
    public void testPropertyFileSinkConfigReaderInvalidFile() {
        new PropertyFileSinkConfigReader(
                        "src/test/resources/nonExistantSinkConfig.properties");
    }

}

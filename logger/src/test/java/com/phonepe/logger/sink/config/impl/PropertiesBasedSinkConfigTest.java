package com.phonepe.logger.sink.config.impl;

import java.time.format.DateTimeFormatter;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

import com.phonepe.logger.LogLevel;
import com.phonepe.logger.sink.config.SinkConfig;
import com.phonepe.logger.sink.config.exception.InvalidConfigException;
import com.phonepe.logger.util.Constants;

public class PropertiesBasedSinkConfigTest {

    @Test
    public void testValidPropertiesBasedSinkConfigMultiThreadedAsyncWrite() {
        Properties properties = new Properties();
        properties.put(Constants.TS_FORMAT_KEY, "dd:MM:yyyy hh:mm:ss");
        properties.put(Constants.LOG_LEVEL_KEY, "INFO,ERROR,DEBUG");
        properties.put(Constants.SINK_TYPE_KEY, "FILE");
        properties.put(Constants.THREAD_MODEL_KEY,
                        Constants.MULTI_THREADED_MODE);
        properties.put(Constants.WRITE_MODE_KEY, Constants.ASYNC_WRITE_MODE);

        properties.put("customconfig1", "customvalue1");
        properties.put("customconfig2", "customvalue2");

        SinkConfig sinkConfig = new PropertiesBasedSinkConfig(properties, 1);

        Assert.assertEquals(
                        DateTimeFormatter.ofPattern("dd:MM:yyyy hh:mm:ss")
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
                        sinkConfig.getStringProperty("customconfig1"));
        Assert.assertEquals("customvalue2",
                        sinkConfig.getStringProperty("customconfig2"));
    }

    @Test
    public void testValidPropertiesBasedSinkConfigSingleLogLevel() {
        Properties properties = new Properties();
        properties.put(Constants.TS_FORMAT_KEY, "dd:MM:yyyy hh:mm:ss");
        properties.put(Constants.LOG_LEVEL_KEY, "INFO");
        properties.put(Constants.SINK_TYPE_KEY, "FILE");
        properties.put(Constants.THREAD_MODEL_KEY,
                        Constants.MULTI_THREADED_MODE);
        properties.put(Constants.WRITE_MODE_KEY, Constants.ASYNC_WRITE_MODE);

        properties.put("customconfig1", "customvalue1");
        properties.put("customconfig2", "customvalue2");

        SinkConfig sinkConfig = new PropertiesBasedSinkConfig(properties, 1);

        Assert.assertEquals(
                        DateTimeFormatter.ofPattern("dd:MM:yyyy hh:mm:ss")
                                        .toString(),
                        sinkConfig.getTimeFormat().toString());

        Assert.assertArrayEquals(new LogLevel[] { LogLevel.INFO },
                        sinkConfig.getLogLevels().toArray(new LogLevel[0]));
        Assert.assertEquals("file", sinkConfig.getSinkType());
        Assert.assertTrue(sinkConfig.isThreadSafe());
        Assert.assertTrue(sinkConfig.isAsyncMode());

        Assert.assertEquals("customvalue1",
                        sinkConfig.getStringProperty("customconfig1"));
        Assert.assertEquals("customvalue2",
                        sinkConfig.getStringProperty("customconfig2"));
    }

    @Test
    public void testValidPropertiesBasedSinkConfigNoAdditionalConfig() {
        Properties properties = new Properties();
        properties.put(Constants.TS_FORMAT_KEY, "dd:MM:yyyy hh:mm:ss");
        properties.put(Constants.LOG_LEVEL_KEY, "INFO");
        properties.put(Constants.SINK_TYPE_KEY, "FILE");
        properties.put(Constants.THREAD_MODEL_KEY,
                        Constants.MULTI_THREADED_MODE);
        properties.put(Constants.WRITE_MODE_KEY, Constants.ASYNC_WRITE_MODE);

        SinkConfig sinkConfig = new PropertiesBasedSinkConfig(properties, 1);

        Assert.assertEquals(
                        DateTimeFormatter.ofPattern("dd:MM:yyyy hh:mm:ss")
                                        .toString(),
                        sinkConfig.getTimeFormat().toString());

        Assert.assertArrayEquals(new LogLevel[] { LogLevel.INFO },
                        sinkConfig.getLogLevels().toArray(new LogLevel[0]));
        Assert.assertEquals("file", sinkConfig.getSinkType());
        Assert.assertTrue(sinkConfig.isThreadSafe());
        Assert.assertTrue(sinkConfig.isAsyncMode());
    }

    @Test
    public void testValidPropertiesBasedSinkConfigSingleThreadedASyncWrite() {
        Properties properties = new Properties();
        properties.put(Constants.TS_FORMAT_KEY, "dd:MM:yyyy hh:mm:ss");
        properties.put(Constants.LOG_LEVEL_KEY, "INFO,ERROR,DEBUG");
        properties.put(Constants.SINK_TYPE_KEY, "FILE");
        properties.put(Constants.THREAD_MODEL_KEY,
                        Constants.SINGLE_THREADED_MODE);
        properties.put(Constants.WRITE_MODE_KEY, Constants.ASYNC_WRITE_MODE);

        properties.put("customconfig1", "customvalue1");
        properties.put("customconfig2", "customvalue2");

        SinkConfig sinkConfig = new PropertiesBasedSinkConfig(properties, 1);

        Assert.assertEquals(
                        DateTimeFormatter.ofPattern("dd:MM:yyyy hh:mm:ss")
                                        .toString(),
                        sinkConfig.getTimeFormat().toString());

        Assert.assertArrayEquals(
                        new LogLevel[] { LogLevel.INFO, LogLevel.ERROR,
                                        LogLevel.DEBUG },
                        sinkConfig.getLogLevels().toArray(new LogLevel[0]));
        Assert.assertEquals("file", sinkConfig.getSinkType());
        Assert.assertFalse(sinkConfig.isThreadSafe());
        Assert.assertTrue(sinkConfig.isAsyncMode());

        Assert.assertEquals("customvalue1",
                        sinkConfig.getStringProperty("customconfig1"));
        Assert.assertEquals("customvalue2",
                        sinkConfig.getStringProperty("customconfig2"));
    }

    @Test
    public void testValidPropertiesBasedSinkConfigSingleThreadedSyncWrite() {
        Properties properties = new Properties();
        properties.put(Constants.TS_FORMAT_KEY, "dd:MM:yyyy hh:mm:ss");
        properties.put(Constants.LOG_LEVEL_KEY, "INFO,ERROR,DEBUG");
        properties.put(Constants.SINK_TYPE_KEY, "FILE");
        properties.put(Constants.THREAD_MODEL_KEY,
                        Constants.SINGLE_THREADED_MODE);
        properties.put(Constants.WRITE_MODE_KEY, Constants.SYNC_WRITE_MODE);

        properties.put("customconfig1", "customvalue1");
        properties.put("customconfig2", "customvalue2");

        SinkConfig sinkConfig = new PropertiesBasedSinkConfig(properties, 1);

        Assert.assertEquals(
                        DateTimeFormatter.ofPattern("dd:MM:yyyy hh:mm:ss")
                                        .toString(),
                        sinkConfig.getTimeFormat().toString());

        Assert.assertArrayEquals(
                        new LogLevel[] { LogLevel.INFO, LogLevel.ERROR,
                                        LogLevel.DEBUG },
                        sinkConfig.getLogLevels().toArray(new LogLevel[0]));
        Assert.assertEquals("file", sinkConfig.getSinkType());
        Assert.assertFalse(sinkConfig.isThreadSafe());
        Assert.assertFalse(sinkConfig.isAsyncMode());

        Assert.assertEquals("customvalue1",
                        sinkConfig.getStringProperty("customconfig1"));
        Assert.assertEquals("customvalue2",
                        sinkConfig.getStringProperty("customconfig2"));
    }

    @Test
    public void testValidPropertiesBasedSinkConfigThreadOptionalConfigRemoved() {
        Properties properties = new Properties();
        properties.put(Constants.TS_FORMAT_KEY, "dd:MM:yyyy hh:mm:ss");
        properties.put(Constants.LOG_LEVEL_KEY, "INFO,ERROR,DEBUG");
        properties.put(Constants.SINK_TYPE_KEY, "FILE");

        properties.put("customconfig1", "customvalue1");
        properties.put("customconfig2", "customvalue2");

        SinkConfig sinkConfig = new PropertiesBasedSinkConfig(properties, 1);

        Assert.assertEquals(
                        DateTimeFormatter.ofPattern("dd:MM:yyyy hh:mm:ss")
                                        .toString(),
                        sinkConfig.getTimeFormat().toString());

        Assert.assertArrayEquals(
                        new LogLevel[] { LogLevel.INFO, LogLevel.ERROR,
                                        LogLevel.DEBUG },
                        sinkConfig.getLogLevels().toArray(new LogLevel[0]));
        Assert.assertEquals("file", sinkConfig.getSinkType());
        Assert.assertFalse(sinkConfig.isThreadSafe());
        Assert.assertFalse(sinkConfig.isAsyncMode());

        Assert.assertEquals("customvalue1",
                        sinkConfig.getStringProperty("customconfig1"));
        Assert.assertEquals("customvalue2",
                        sinkConfig.getStringProperty("customconfig2"));
    }

    @Test(expected = InvalidConfigException.class)
    public void testInValidPropertiesBasedSinkConfigTSdoesnotExist() {
        Properties properties = new Properties();
        properties.put(Constants.LOG_LEVEL_KEY, "INFO,ERROR,DEBUG");
        properties.put(Constants.SINK_TYPE_KEY, "FILE");
        properties.put(Constants.THREAD_MODEL_KEY,
                        Constants.SINGLE_THREADED_MODE);
        properties.put(Constants.WRITE_MODE_KEY, Constants.SYNC_WRITE_MODE);

        properties.put("customconfig1", "customvalue1");
        properties.put("customconfig2", "customvalue2");

        new PropertiesBasedSinkConfig(properties, 1);

    }

    @Test(expected = InvalidConfigException.class)
    public void testInValidPropertiesBasedSinkConfigLogLeveldoesnotExist() {
        Properties properties = new Properties();
        properties.put(Constants.TS_FORMAT_KEY, "dd:MM:yyyy hh:mm:ss");

        properties.put(Constants.SINK_TYPE_KEY, "FILE");
        properties.put(Constants.THREAD_MODEL_KEY,
                        Constants.SINGLE_THREADED_MODE);
        properties.put(Constants.WRITE_MODE_KEY, Constants.SYNC_WRITE_MODE);

        properties.put("customconfig1", "customvalue1");
        properties.put("customconfig2", "customvalue2");

        new PropertiesBasedSinkConfig(properties, 1);

    }

    @Test(expected = InvalidConfigException.class)
    public void testInValidPropertiesBasedSinkConfigSinkTypedoesnotExist() {
        Properties properties = new Properties();
        properties.put(Constants.TS_FORMAT_KEY, "dd:MM:yyyy hh:mm:ss");
        properties.put(Constants.LOG_LEVEL_KEY, "INFO,ERROR,DEBUG");

        properties.put(Constants.THREAD_MODEL_KEY,
                        Constants.SINGLE_THREADED_MODE);
        properties.put(Constants.WRITE_MODE_KEY, Constants.SYNC_WRITE_MODE);

        properties.put("customconfig1", "customvalue1");
        properties.put("customconfig2", "customvalue2");

        new PropertiesBasedSinkConfig(properties, 1);

    }

    @Test(expected = InvalidConfigException.class)
    public void testInValidPropertiesBasedSinkConfigInvalidThreadMode() {
        Properties properties = new Properties();
        properties.put(Constants.TS_FORMAT_KEY, "dd:MM:yyyy hh:mm:ss");
        properties.put(Constants.LOG_LEVEL_KEY, "INFO,ERROR,DEBUG");

        properties.put(Constants.SINK_TYPE_KEY, "FILE");
        properties.put(Constants.THREAD_MODEL_KEY, "invalidThreadMode");
        properties.put(Constants.WRITE_MODE_KEY, Constants.SYNC_WRITE_MODE);

        properties.put("customconfig1", "customvalue1");
        properties.put("customconfig2", "customvalue2");

        new PropertiesBasedSinkConfig(properties, 1);

    }

    @Test(expected = InvalidConfigException.class)
    public void testInValidPropertiesBasedSinkConfigInvalidSyncMode() {
        Properties properties = new Properties();
        properties.put(Constants.TS_FORMAT_KEY, "dd:MM:yyyy hh:mm:ss");
        properties.put(Constants.LOG_LEVEL_KEY, "INFO,ERROR,DEBUG");

        properties.put(Constants.SINK_TYPE_KEY, "FILE");
        properties.put(Constants.THREAD_MODEL_KEY,
                        Constants.MULTI_THREADED_MODE);
        properties.put(Constants.WRITE_MODE_KEY, "invalidSinkMode");

        properties.put("customconfig1", "customvalue1");
        properties.put("customconfig2", "customvalue2");

        new PropertiesBasedSinkConfig(properties, 1);

    }

}

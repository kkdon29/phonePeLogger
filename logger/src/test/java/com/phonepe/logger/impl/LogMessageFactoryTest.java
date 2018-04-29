package com.phonepe.logger.impl;

import java.time.ZonedDateTime;

import org.junit.Assert;
import org.junit.Test;

import com.phonepe.logger.LogLevel;
import com.phonepe.logger.LogMessage;

public class LogMessageFactoryTest {

    @Test
    public void testCreateLogMessage() {
        LogMessageFactory logMessageFactory = new LogMessageFactory();
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        LogMessage logMessage = logMessageFactory.createLogMessage(
                        LogLevel.INFO, zonedDateTime, "test", "test message");
        Assert.assertEquals(LogLevel.INFO, logMessage.getLogLevel());
        Assert.assertEquals(zonedDateTime, logMessage.getDate());
        Assert.assertEquals("test", logMessage.getNamespace());
        Assert.assertEquals("test message", logMessage.getMessage());
    }

}

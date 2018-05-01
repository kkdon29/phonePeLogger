package com.phonepe.logger.util;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.phonepe.logger.LogMessage;
import com.phonepe.logger.LogType;

public class UtilsTest {

    @Test
    public void testCheckNullOrEmptyNullString() {
        Assert.assertTrue(Utils.checkNullOrEmpty(null));
    }

    @Test
    public void testCheckNullOrEmptyEmptyStringWithSpaces() {
        Assert.assertTrue(Utils.checkNullOrEmpty("     "));
    }

    @Test
    public void testCheckNullOrEmptyValidString() {
        Assert.assertFalse(Utils.checkNullOrEmpty("aaa"));
    }

    @Test
    public void testIsCloseSinkMessage() {
        LogMessage logMessage = Mockito.mock(LogMessage.class);
        Mockito.doReturn(LogType.FRAMEWORK_LOG).when(logMessage).getLogType();
        Mockito.doReturn(Constants.CLOSE_SINK_MESSAGE).when(logMessage)
                        .getMessage();

        Assert.assertTrue(Utils.isCloseSinkMessage(logMessage));
    }

    @Test
    public void testIsCloseSinkMessageUserLog() {
        LogMessage logMessage = Mockito.mock(LogMessage.class);
        Mockito.doReturn(LogType.USER_LOG).when(logMessage).getLogType();
        Mockito.doReturn(Constants.CLOSE_SINK_MESSAGE).when(logMessage)
                        .getMessage();

        Assert.assertFalse(Utils.isCloseSinkMessage(logMessage));
    }

    @Test
    public void testIsCloseSinkMessageWrongMessage() {
        LogMessage logMessage = Mockito.mock(LogMessage.class);
        Mockito.doReturn(LogType.FRAMEWORK_LOG).when(logMessage).getLogType();
        Mockito.doReturn("unsupported").when(logMessage).getMessage();

        Assert.assertFalse(Utils.isCloseSinkMessage(logMessage));
    }

}

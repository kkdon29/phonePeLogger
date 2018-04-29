package com.phonepe.logger.outputstreamsink;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.phonepe.logger.LogLevel;
import com.phonepe.logger.LogMessage;
import com.phonepe.logger.impl.LogMessageFactory;
import com.phonepe.logger.outputstreamsink.exception.UnsupportedLogTypeException;
import com.phonepe.logger.sink.config.SinkConfig;

@RunWith(MockitoJUnitRunner.class)
public class BlockingQueueLogReaderWriterTest {
    @Mock
    private SinkConfig        sinkConfig;
    private LogMessageFactory logMessageFactory = new LogMessageFactory();

    @Test
    public void testLogUserLog() throws UnsupportedLogTypeException,
                    IOException, InterruptedException {
        Mockito.doReturn(false).when(this.sinkConfig).isThreadSafe();

        BlockingQueueLogReaderWriter blockingQueueLogReaderWriter = new BlockingQueueLogReaderWriter(
                        this.sinkConfig);
        ZonedDateTime time1 = ZonedDateTime.now();
        blockingQueueLogReaderWriter.log(
                        this.logMessageFactory.createLogMessage(LogLevel.INFO,
                                        time1, "test", "This is test1"));
        ZonedDateTime time2 = ZonedDateTime.now();

        blockingQueueLogReaderWriter.log(
                        this.logMessageFactory.createLogMessage(LogLevel.DEBUG,
                                        time2, "test", "This is test2"));

        LogMessage writtenMsg1 = blockingQueueLogReaderWriter.getNextLog(1,
                        TimeUnit.SECONDS);
        LogMessage writtenMsg2 = blockingQueueLogReaderWriter.getNextLog(1,
                        TimeUnit.SECONDS);

        Assert.assertEquals(
                        this.logMessageFactory.createLogMessage(LogLevel.INFO,
                                        time1, "test", "This is test1"),
                        writtenMsg1);
        Assert.assertEquals(
                        this.logMessageFactory.createLogMessage(LogLevel.DEBUG,
                                        time2, "test", "This is test2"),
                        writtenMsg2);

        LogMessage writtenMsg3 = blockingQueueLogReaderWriter.getNextLog(1,
                        TimeUnit.SECONDS);
        Assert.assertNull(writtenMsg3);

    }

    @Test
    public void testLogUserLogThreadSafe() throws UnsupportedLogTypeException,
                    IOException, InterruptedException {
        // Just verify if the behavior doesn't change when its thread safe
        Mockito.doReturn(true).when(this.sinkConfig).isThreadSafe();

        BlockingQueueLogReaderWriter blockingQueueLogReaderWriter = new BlockingQueueLogReaderWriter(
                        this.sinkConfig);
        ZonedDateTime time1 = ZonedDateTime.now();
        blockingQueueLogReaderWriter.log(
                        this.logMessageFactory.createLogMessage(LogLevel.INFO,
                                        time1, "test", "This is test1"));
        ZonedDateTime time2 = ZonedDateTime.now();

        blockingQueueLogReaderWriter.log(
                        this.logMessageFactory.createLogMessage(LogLevel.DEBUG,
                                        time2, "test", "This is test2"));

        LogMessage writtenMsg1 = blockingQueueLogReaderWriter.getNextLog(1,
                        TimeUnit.SECONDS);
        LogMessage writtenMsg2 = blockingQueueLogReaderWriter.getNextLog(1,
                        TimeUnit.SECONDS);

        Assert.assertEquals(
                        this.logMessageFactory.createLogMessage(LogLevel.INFO,
                                        time1, "test", "This is test1"),
                        writtenMsg1);
        Assert.assertEquals(
                        this.logMessageFactory.createLogMessage(LogLevel.DEBUG,
                                        time2, "test", "This is test2"),
                        writtenMsg2);
        LogMessage writtenMsg3 = blockingQueueLogReaderWriter.getNextLog(1,
                        TimeUnit.SECONDS);
        Assert.assertNull(writtenMsg3);

    }

    @Test
    public void testLogCloseLog() throws UnsupportedLogTypeException,
                    IOException, InterruptedException {
        Mockito.doReturn(false).when(this.sinkConfig).isThreadSafe();

        BlockingQueueLogReaderWriter blockingQueueLogReaderWriter = new BlockingQueueLogReaderWriter(
                        this.sinkConfig);
        blockingQueueLogReaderWriter
                        .log(this.logMessageFactory.sinkCloseLogMessage());

        LogMessage writtenMsg = blockingQueueLogReaderWriter.getNextLog(1,
                        TimeUnit.SECONDS);
        Assert.assertEquals(this.logMessageFactory.sinkCloseLogMessage(),
                        writtenMsg);
        writtenMsg = blockingQueueLogReaderWriter.getNextLog(1,
                        TimeUnit.SECONDS);
        Assert.assertNull(writtenMsg);
    }

    @Test
    public void testLogCloseLogRepetedly() throws UnsupportedLogTypeException,
                    IOException, InterruptedException {
        Mockito.doReturn(false).when(this.sinkConfig).isThreadSafe();

        BlockingQueueLogReaderWriter blockingQueueLogReaderWriter = new BlockingQueueLogReaderWriter(
                        this.sinkConfig);
        blockingQueueLogReaderWriter
                        .log(this.logMessageFactory.sinkCloseLogMessage());
        blockingQueueLogReaderWriter
                        .log(this.logMessageFactory.sinkCloseLogMessage());
        blockingQueueLogReaderWriter
                        .log(this.logMessageFactory.sinkCloseLogMessage());

        LogMessage writtenMsg = blockingQueueLogReaderWriter.getNextLog(1,
                        TimeUnit.SECONDS);
        Assert.assertEquals(this.logMessageFactory.sinkCloseLogMessage(),
                        writtenMsg);
        writtenMsg = blockingQueueLogReaderWriter.getNextLog(1,
                        TimeUnit.SECONDS);
        Assert.assertNull(writtenMsg);
    }

    @Test
    public void testLogUserLogOnClosedLog() throws UnsupportedLogTypeException,
                    IOException, InterruptedException {
        Mockito.doReturn(false).when(this.sinkConfig).isThreadSafe();

        BlockingQueueLogReaderWriter blockingQueueLogReaderWriter = new BlockingQueueLogReaderWriter(
                        this.sinkConfig);
        blockingQueueLogReaderWriter
                        .log(this.logMessageFactory.sinkCloseLogMessage());

        ZonedDateTime time1 = ZonedDateTime.now();
        blockingQueueLogReaderWriter.log(
                        this.logMessageFactory.createLogMessage(LogLevel.INFO,
                                        time1, "test", "This is test1"));
        LogMessage writtenMsg = blockingQueueLogReaderWriter.getNextLog(1,
                        TimeUnit.SECONDS);
        Assert.assertEquals(this.logMessageFactory.sinkCloseLogMessage(),
                        writtenMsg);
        writtenMsg = blockingQueueLogReaderWriter.getNextLog(1,
                        TimeUnit.SECONDS);
        Assert.assertNull(writtenMsg);

    }

    @Test
    public void testLogUnsupportedFrameworkLog()
                    throws UnsupportedLogTypeException, IOException,
                    InterruptedException {
        Mockito.doReturn(false).when(this.sinkConfig).isThreadSafe();

        BlockingQueueLogReaderWriter blockingQueueLogReaderWriter = new BlockingQueueLogReaderWriter(
                        this.sinkConfig);
        LogMessage logMessage = Mockito
                        .spy(this.logMessageFactory.sinkCloseLogMessage());
        Mockito.doReturn("Unsupported").when(logMessage).getMessage();
        blockingQueueLogReaderWriter.log(logMessage);
        LogMessage writtenMsg = blockingQueueLogReaderWriter.getNextLog(1,
                        TimeUnit.SECONDS);
        Assert.assertNull(writtenMsg);

    }
}

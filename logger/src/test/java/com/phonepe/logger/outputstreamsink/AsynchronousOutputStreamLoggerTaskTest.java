package com.phonepe.logger.outputstreamsink;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.phonepe.logger.LogLevel;
import com.phonepe.logger.LogMessage;
import com.phonepe.logger.LogReader;
import com.phonepe.logger.LogWriter;
import com.phonepe.logger.impl.LogMessageFactory;
import com.phonepe.logger.outputstreamsink.exception.UnsupportedLogTypeException;
import com.phonepe.logger.sink.config.SinkConfig;

@RunWith(MockitoJUnitRunner.class)
public class AsynchronousOutputStreamLoggerTaskTest {

    @Mock
    private LogWriter         logWriter;
    @Mock
    private LogReader         logReader;
    @Mock
    public SinkConfig         sinkConfig;
    private LogMessageFactory logMessageFactory = new LogMessageFactory();

    @Test
    public void testNormalRun() throws InterruptedException,
                    UnsupportedLogTypeException, IOException {
        AsynchronousOutputStreamLoggerTask asynchronousOutputStreamLoggerTask = new AsynchronousOutputStreamLoggerTask(
                        this.sinkConfig, this.logReader, this.logWriter,
                        this.logMessageFactory);
        ZonedDateTime time1 = ZonedDateTime.now();
        LogMessage logMessage1 = this.logMessageFactory.createLogMessage(
                        LogLevel.INFO, time1, "test", "This is test1");
        ZonedDateTime time2 = ZonedDateTime.now();
        LogMessage logMessage2 = this.logMessageFactory.createLogMessage(
                        LogLevel.INFO, time2, "test", "This is test2");

        LogMessage logMessage3 = this.logMessageFactory.sinkCloseLogMessage();
        Mockito.doReturn(logMessage1, logMessage2, logMessage3)
                        .when(this.logReader)
                        .getNextLog(ArgumentMatchers.anyLong(),
                                        ArgumentMatchers.any(TimeUnit.class));
        asynchronousOutputStreamLoggerTask.run();
        List<LogMessage> expectedMessages = new ArrayList<>();
        expectedMessages.add(logMessage1);
        expectedMessages.add(logMessage2);
        expectedMessages.add(logMessage3);

        ArgumentCaptor<LogMessage> argumentCaptor = ArgumentCaptor
                        .forClass(LogMessage.class);
        Mockito.verify(this.logWriter, Mockito.times(3))
                        .log(argumentCaptor.capture());
        List<LogMessage> capturedMessages = argumentCaptor.getAllValues();
        Assert.assertEquals(expectedMessages, capturedMessages);
    }

    @Test
    public void testRunWithNullLog() throws InterruptedException,
                    UnsupportedLogTypeException, IOException {
        AsynchronousOutputStreamLoggerTask asynchronousOutputStreamLoggerTask = new AsynchronousOutputStreamLoggerTask(
                        this.sinkConfig, this.logReader, this.logWriter,
                        this.logMessageFactory);
        ZonedDateTime time1 = ZonedDateTime.now();
        LogMessage logMessage1 = this.logMessageFactory.createLogMessage(
                        LogLevel.INFO, time1, "test", "This is test1");
        ZonedDateTime time2 = ZonedDateTime.now();
        LogMessage logMessage2 = this.logMessageFactory.createLogMessage(
                        LogLevel.INFO, time2, "test", "This is test2");

        LogMessage logMessage3 = this.logMessageFactory.sinkCloseLogMessage();
        Mockito.doReturn(logMessage1, logMessage2, null, logMessage3)
                        .when(this.logReader)
                        .getNextLog(ArgumentMatchers.anyLong(),
                                        ArgumentMatchers.any(TimeUnit.class));
        asynchronousOutputStreamLoggerTask.run();
        List<LogMessage> expectedMessages = new ArrayList<>();
        expectedMessages.add(logMessage1);
        expectedMessages.add(logMessage2);
        expectedMessages.add(logMessage3);

        ArgumentCaptor<LogMessage> argumentCaptor = ArgumentCaptor
                        .forClass(LogMessage.class);
        Mockito.verify(this.logWriter, Mockito.times(3))
                        .log(argumentCaptor.capture());
        List<LogMessage> capturedMessages = argumentCaptor.getAllValues();
        Assert.assertEquals(expectedMessages, capturedMessages);
    }

    @Test
    public void testRunWithInterruptedException() throws InterruptedException,
                    UnsupportedLogTypeException, IOException {
        AsynchronousOutputStreamLoggerTask asynchronousOutputStreamLoggerTask = new AsynchronousOutputStreamLoggerTask(
                        this.sinkConfig, this.logReader, this.logWriter,
                        this.logMessageFactory);
        ZonedDateTime time1 = ZonedDateTime.now();
        LogMessage logMessage1 = this.logMessageFactory.createLogMessage(
                        LogLevel.INFO, time1, "test", "This is test1");
        ZonedDateTime time2 = ZonedDateTime.now();
        LogMessage logMessage2 = this.logMessageFactory.createLogMessage(
                        LogLevel.INFO, time2, "test", "This is test2");

        LogMessage logMessage3 = this.logMessageFactory.sinkCloseLogMessage();
        Mockito.doThrow(InterruptedException.class)
                        .doReturn(logMessage1, logMessage2, null, logMessage3)
                        .when(this.logReader)
                        .getNextLog(ArgumentMatchers.anyLong(),
                                        ArgumentMatchers.any(TimeUnit.class));
        asynchronousOutputStreamLoggerTask.run();
        List<LogMessage> expectedMessages = new ArrayList<>();
        expectedMessages.add(logMessage3);

        ArgumentCaptor<LogMessage> argumentCaptor = ArgumentCaptor
                        .forClass(LogMessage.class);
        Mockito.verify(this.logWriter, Mockito.times(1))
                        .log(argumentCaptor.capture());
        List<LogMessage> capturedMessages = argumentCaptor.getAllValues();
        Assert.assertEquals(expectedMessages, capturedMessages);
    }

    @Test
    public void testRunWithIOExceptionatClose() throws InterruptedException,
                    UnsupportedLogTypeException, IOException {
        AsynchronousOutputStreamLoggerTask asynchronousOutputStreamLoggerTask = new AsynchronousOutputStreamLoggerTask(
                        this.sinkConfig, this.logReader, this.logWriter,
                        this.logMessageFactory);
        ZonedDateTime time1 = ZonedDateTime.now();
        LogMessage logMessage1 = this.logMessageFactory.createLogMessage(
                        LogLevel.INFO, time1, "test", "This is test1");
        ZonedDateTime time2 = ZonedDateTime.now();
        LogMessage logMessage2 = this.logMessageFactory.createLogMessage(
                        LogLevel.INFO, time2, "test", "This is test2");

        LogMessage logMessage3 = this.logMessageFactory.sinkCloseLogMessage();
        Mockito.doReturn(logMessage1, logMessage2, logMessage3)
                        .when(this.logReader)
                        .getNextLog(ArgumentMatchers.anyLong(),
                                        ArgumentMatchers.any(TimeUnit.class));
        Mockito.doThrow(IOException.class).when(this.logWriter)
                        .log(ArgumentMatchers.eq(logMessage3));

        asynchronousOutputStreamLoggerTask.run();
        // Just make sure all get passed to writer and run method quits
        // gracefully
        List<LogMessage> expectedMessages = new ArrayList<>();
        expectedMessages.add(logMessage1);
        expectedMessages.add(logMessage2);
        expectedMessages.add(logMessage3);

        ArgumentCaptor<LogMessage> argumentCaptor = ArgumentCaptor
                        .forClass(LogMessage.class);
        Mockito.verify(this.logWriter, Mockito.times(3))
                        .log(argumentCaptor.capture());

        List<LogMessage> capturedMessages = argumentCaptor.getAllValues();
        Assert.assertEquals(expectedMessages, capturedMessages);
    }

}

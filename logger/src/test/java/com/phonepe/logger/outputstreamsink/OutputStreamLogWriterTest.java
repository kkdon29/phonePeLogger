package com.phonepe.logger.outputstreamsink;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.phonepe.logger.LogLevel;
import com.phonepe.logger.LogMessage;
import com.phonepe.logger.impl.LogMessageFactory;
import com.phonepe.logger.outputstreamsink.exception.UnsupportedLogTypeException;
import com.phonepe.logger.sink.config.SinkConfig;

@RunWith(MockitoJUnitRunner.class)
public class OutputStreamLogWriterTest {

    @Mock
    private SinkConfig        sinkConfig;
    private LogMessageFactory logMessageFactory = new LogMessageFactory();

    @Test
    public void testLogUserLog() throws UnsupportedLogTypeException,
                    IOException, InterruptedException {
        ByteArrayOutputStream ostream = new ByteArrayOutputStream();
        Mockito.doReturn(false).when(this.sinkConfig).isThreadSafe();
        Mockito.doReturn("UTF-16LE").when(this.sinkConfig)
                        .getStringProperty(ArgumentMatchers.eq(
                                        OutputStreamSinkConstants.CHARSET_PROPERTY));

        DateTimeFormatter formatter = DateTimeFormatter
                        .ofPattern("dd:MM:yyyy hh:mm:ss:SSS z");
        Mockito.doReturn(formatter).when(this.sinkConfig).getTimeFormat();

        OutputStreamLogWriter outputStreamLogWriter = new OutputStreamLogWriter(
                        ostream, this.sinkConfig);
        ZonedDateTime time1 = ZonedDateTime.now();
        outputStreamLogWriter.log(this.logMessageFactory.createLogMessage(
                        LogLevel.INFO, time1, "test", "This is test1"));
        ZonedDateTime time2 = ZonedDateTime.now();

        outputStreamLogWriter.log(this.logMessageFactory.createLogMessage(
                        LogLevel.DEBUG, time2, "test", "This is test2"));

        String writtenContent = ostream.toString("UTF-16LE");
        String formattedTime1 = formatter.format(time1);
        String formattedTime2 = formatter.format(time2);
        String expectedContent = "INFO " + formattedTime1
                        + " test This is test1\nDEBUG " + formattedTime2
                        + " test This is test2\n";
        Assert.assertEquals(expectedContent, writtenContent);
    }

    @Test
    public void testLogUserLogNoEncodingSpecified()
                    throws UnsupportedLogTypeException, IOException,
                    InterruptedException {
        ByteArrayOutputStream ostream = new ByteArrayOutputStream();
        Mockito.doReturn(false).when(this.sinkConfig).isThreadSafe();
        Mockito.doReturn(null).when(this.sinkConfig)
                        .getStringProperty(ArgumentMatchers.eq(
                                        OutputStreamSinkConstants.CHARSET_PROPERTY));

        DateTimeFormatter formatter = DateTimeFormatter
                        .ofPattern("dd:MM:yyyy hh:mm:ss:SSS z");
        Mockito.doReturn(formatter).when(this.sinkConfig).getTimeFormat();

        OutputStreamLogWriter outputStreamLogWriter = new OutputStreamLogWriter(
                        ostream, this.sinkConfig);
        ZonedDateTime time1 = ZonedDateTime.now();
        outputStreamLogWriter.log(this.logMessageFactory.createLogMessage(
                        LogLevel.INFO, time1, "test", "This is test1"));
        ZonedDateTime time2 = ZonedDateTime.now();

        outputStreamLogWriter.log(this.logMessageFactory.createLogMessage(
                        LogLevel.DEBUG, time2, "test", "This is test2"));

        String writtenContent = ostream
                        .toString(Charset.defaultCharset().name());
        String formattedTime1 = formatter.format(time1);
        String formattedTime2 = formatter.format(time2);
        String expectedContent = "INFO " + formattedTime1
                        + " test This is test1\nDEBUG " + formattedTime2
                        + " test This is test2\n";
        Assert.assertEquals(expectedContent, writtenContent);
    }

    @Test
    public void testLogUserLogDefaultEncodingSpecified()
                    throws UnsupportedLogTypeException, IOException,
                    InterruptedException {
        ByteArrayOutputStream ostream = new ByteArrayOutputStream();
        Mockito.doReturn(false).when(this.sinkConfig).isThreadSafe();
        Mockito.doReturn(OutputStreamSinkConstants.DEFAULT_CHARSET)
                        .when(this.sinkConfig)
                        .getStringProperty(ArgumentMatchers.eq(
                                        OutputStreamSinkConstants.CHARSET_PROPERTY));

        DateTimeFormatter formatter = DateTimeFormatter
                        .ofPattern("dd:MM:yyyy hh:mm:ss:SSS z");
        Mockito.doReturn(formatter).when(this.sinkConfig).getTimeFormat();

        OutputStreamLogWriter outputStreamLogWriter = new OutputStreamLogWriter(
                        ostream, this.sinkConfig);
        ZonedDateTime time1 = ZonedDateTime.now();
        outputStreamLogWriter.log(this.logMessageFactory.createLogMessage(
                        LogLevel.INFO, time1, "test", "This is test1"));
        ZonedDateTime time2 = ZonedDateTime.now();

        outputStreamLogWriter.log(this.logMessageFactory.createLogMessage(
                        LogLevel.DEBUG, time2, "test", "This is test2"));

        String writtenContent = ostream
                        .toString(Charset.defaultCharset().name());
        String formattedTime1 = formatter.format(time1);
        String formattedTime2 = formatter.format(time2);
        String expectedContent = "INFO " + formattedTime1
                        + " test This is test1\nDEBUG " + formattedTime2
                        + " test This is test2\n";
        Assert.assertEquals(expectedContent, writtenContent);
    }

    @Test
    public void testLogUserLogThreadSafe() throws UnsupportedLogTypeException,
                    IOException, InterruptedException {
        // Just verify if the behavior doesn't change when its thread safe
        ByteArrayOutputStream ostream = new ByteArrayOutputStream();
        Mockito.doReturn(true).when(this.sinkConfig).isThreadSafe();
        DateTimeFormatter formatter = DateTimeFormatter
                        .ofPattern("dd:MM:yyyy hh:mm:ss:SSS z");
        Mockito.doReturn(OutputStreamSinkConstants.DEFAULT_CHARSET)
                        .when(this.sinkConfig)
                        .getStringProperty(ArgumentMatchers.eq(
                                        OutputStreamSinkConstants.CHARSET_PROPERTY));

        Mockito.doReturn(formatter).when(this.sinkConfig).getTimeFormat();

        OutputStreamLogWriter outputStreamLogWriter = new OutputStreamLogWriter(
                        ostream, this.sinkConfig);
        ZonedDateTime time1 = ZonedDateTime.now();
        outputStreamLogWriter.log(this.logMessageFactory.createLogMessage(
                        LogLevel.INFO, time1, "test", "This is test1"));
        ZonedDateTime time2 = ZonedDateTime.now();

        outputStreamLogWriter.log(this.logMessageFactory.createLogMessage(
                        LogLevel.DEBUG, time2, "test", "This is test2"));

        String writtenContent = ostream
                        .toString(Charset.defaultCharset().name());
        String formattedTime1 = formatter.format(time1);
        String formattedTime2 = formatter.format(time2);
        String expectedContent = "INFO " + formattedTime1
                        + " test This is test1\nDEBUG " + formattedTime2
                        + " test This is test2\n";
        Assert.assertEquals(expectedContent, writtenContent);
    }

    @Test
    public void testLogCloseLog() throws UnsupportedLogTypeException,
                    IOException, InterruptedException {
        OutputStream ostream = Mockito.mock(OutputStream.class);
        Mockito.doReturn(false).when(this.sinkConfig).isThreadSafe();

        OutputStreamLogWriter outputStreamLogWriter = new OutputStreamLogWriter(
                        ostream, this.sinkConfig);
        outputStreamLogWriter.log(this.logMessageFactory.sinkCloseLogMessage());
        Mockito.verify(ostream, Mockito.times(1)).close();
    }

    @Test
    public void testLogCloseLogRepetedly() throws UnsupportedLogTypeException,
                    IOException, InterruptedException {
        OutputStream ostream = Mockito.mock(OutputStream.class);
        Mockito.doReturn(false).when(this.sinkConfig).isThreadSafe();

        OutputStreamLogWriter outputStreamLogWriter = new OutputStreamLogWriter(
                        ostream, this.sinkConfig);
        outputStreamLogWriter.log(this.logMessageFactory.sinkCloseLogMessage());
        outputStreamLogWriter.log(this.logMessageFactory.sinkCloseLogMessage());
        outputStreamLogWriter.log(this.logMessageFactory.sinkCloseLogMessage());

        Mockito.verify(ostream, Mockito.times(1)).close();
    }

    @Test
    public void testLogUserLogOnClosedLog() throws UnsupportedLogTypeException,
                    IOException, InterruptedException {
        OutputStream ostream = Mockito.mock(OutputStream.class);
        Mockito.doReturn(false).when(this.sinkConfig).isThreadSafe();
        DateTimeFormatter formatter = DateTimeFormatter
                        .ofPattern("dd:MM:yyyy hh:mm:ss:SSS z");
        Mockito.doReturn(formatter).when(this.sinkConfig).getTimeFormat();

        OutputStreamLogWriter outputStreamLogWriter = new OutputStreamLogWriter(
                        ostream, this.sinkConfig);
        outputStreamLogWriter.log(this.logMessageFactory.sinkCloseLogMessage());

        ZonedDateTime time1 = ZonedDateTime.now();
        outputStreamLogWriter.log(this.logMessageFactory.createLogMessage(
                        LogLevel.INFO, time1, "test", "This is test1"));
        Mockito.verify(ostream, Mockito.times(0)).write(ArgumentMatchers.any());
    }

    @Test
    public void testLogUnsupportedFrameworkLog()
                    throws UnsupportedLogTypeException, IOException,
                    InterruptedException {
        OutputStream ostream = Mockito.mock(OutputStream.class);
        Mockito.doReturn(false).when(this.sinkConfig).isThreadSafe();

        OutputStreamLogWriter outputStreamLogWriter = new OutputStreamLogWriter(
                        ostream, this.sinkConfig);
        LogMessage logMessage = Mockito
                        .spy(this.logMessageFactory.sinkCloseLogMessage());
        Mockito.doReturn("Unsupported").when(logMessage).getMessage();
        outputStreamLogWriter.log(logMessage);
        Mockito.verifyZeroInteractions(ostream);
    }

}

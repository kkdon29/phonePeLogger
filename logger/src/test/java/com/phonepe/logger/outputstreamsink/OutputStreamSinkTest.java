package com.phonepe.logger.outputstreamsink;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.phonepe.logger.LogLevel;
import com.phonepe.logger.LogMessage;
import com.phonepe.logger.LogWriter;
import com.phonepe.logger.impl.LogMessageFactory;
import com.phonepe.logger.outputstreamsink.exception.UnsupportedLogTypeException;
import com.phonepe.logger.sink.config.SinkConfig;

@RunWith(MockitoJUnitRunner.class)
public class OutputStreamSinkTest {

    private OutputStreamSink      outputStreamSink;
    private ByteArrayOutputStream outputStream;
    private LogMessageFactory     logMessageFactory;
    @Mock
    private SinkConfig            sinkConfig;

    @Before
    public void setUp() {
        this.outputStream = new ByteArrayOutputStream();
        this.logMessageFactory = new LogMessageFactory();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testLog() throws UnsupportedEncodingException {
        Mockito.doReturn(false).when(this.sinkConfig).isAsyncMode();
        Mockito.doReturn(true).when(this.sinkConfig).isThreadSafe();
        Mockito.doReturn("UTF-8").when(this.sinkConfig)
                        .getStringProperty(ArgumentMatchers.eq(
                                        OutputStreamSinkConstants.CHARSET_PROPERTY));

        DateTimeFormatter formatter = DateTimeFormatter
                        .ofPattern("dd:MM:yyyy hh:mm:ss:SSS z");
        Mockito.doReturn(formatter).when(this.sinkConfig).getTimeFormat();
        this.outputStreamSink = new OutputStreamSink(this.outputStream,
                        this.sinkConfig, this.logMessageFactory,
                        new OutputStreamLogWriterFactory());
        ZonedDateTime time1 = ZonedDateTime.now();
        this.outputStreamSink.log(this.logMessageFactory.createLogMessage(
                        LogLevel.INFO, time1, "test", "This is test1"));

        ZonedDateTime time2 = ZonedDateTime.now();
        this.outputStreamSink.log(this.logMessageFactory.createLogMessage(
                        LogLevel.INFO, time2, "test", "This is test2"));

        String writtenContent = this.outputStream.toString("UTF-8");
        String formattedTime1 = formatter.format(time1);
        String formattedTime2 = formatter.format(time2);
        String expectedContent = "INFO " + formattedTime1
                        + " test This is test1\nINFO " + formattedTime2
                        + " test This is test2\n";
        Assert.assertEquals(expectedContent, writtenContent);

    }

    @Test
    public void testLogWriteException()
                    throws UnsupportedLogTypeException, IOException {
        OutputStreamLogWriterFactory outputStreamLogWriterFactory = new OutputStreamLogWriterFactory();
        outputStreamLogWriterFactory = Mockito
                        .spy(outputStreamLogWriterFactory);
        LogWriter logWriter = Mockito.mock(LogWriter.class);
        Mockito.doReturn(logWriter).when(outputStreamLogWriterFactory)
                        .createLogWriter(
                                        ArgumentMatchers.any(
                                                        OutputStream.class),
                                        ArgumentMatchers.any(SinkConfig.class),
                                        ArgumentMatchers.any(
                                                        LogMessageFactory.class));
        Mockito.doThrow(IOException.class).doNothing().when(logWriter)
                        .log(ArgumentMatchers.any(LogMessage.class));
        this.outputStreamSink = new OutputStreamSink(this.outputStream,
                        this.sinkConfig, this.logMessageFactory,
                        outputStreamLogWriterFactory);

        ZonedDateTime time1 = ZonedDateTime.now();
        this.outputStreamSink.log(this.logMessageFactory.createLogMessage(
                        LogLevel.INFO, time1, "test", "This is test1"));

        Mockito.verify(logWriter, Mockito.times(1)).log(ArgumentMatchers
                        .eq(this.logMessageFactory.sinkCloseLogMessage()));
    }

    @Test
    public void testCloseWriteException()
                    throws UnsupportedLogTypeException, IOException {
        OutputStreamLogWriterFactory outputStreamLogWriterFactory = new OutputStreamLogWriterFactory();
        outputStreamLogWriterFactory = Mockito
                        .spy(outputStreamLogWriterFactory);
        LogWriter logWriter = Mockito.mock(LogWriter.class);
        Mockito.doReturn(logWriter).when(outputStreamLogWriterFactory)
                        .createLogWriter(
                                        ArgumentMatchers.any(
                                                        OutputStream.class),
                                        ArgumentMatchers.any(SinkConfig.class),
                                        ArgumentMatchers.any(
                                                        LogMessageFactory.class));
        Mockito.doThrow(IOException.class).when(logWriter)
                        .log(ArgumentMatchers.any(LogMessage.class));
        this.outputStreamSink = new OutputStreamSink(this.outputStream,
                        this.sinkConfig, this.logMessageFactory,
                        outputStreamLogWriterFactory);
        // Just make sure close terminates gracefully.
        this.outputStreamSink.close();

    }

}

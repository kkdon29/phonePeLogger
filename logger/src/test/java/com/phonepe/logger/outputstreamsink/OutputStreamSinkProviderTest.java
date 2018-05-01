package com.phonepe.logger.outputstreamsink;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
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
import com.phonepe.logger.impl.LogMessageFactory;
import com.phonepe.logger.outputstreamsink.exception.StreamOpenFailedException;
import com.phonepe.logger.outputstreamsink.exception.UnsupportedStreamException;
import com.phonepe.logger.sink.Sink;
import com.phonepe.logger.sink.config.SinkConfig;
import com.phonepe.logger.sink.config.exception.InvalidConfigException;

@RunWith(MockitoJUnitRunner.class)
public class OutputStreamSinkProviderTest {
    @Mock
    private SinkConfig        sinkConfig;
    private LogMessageFactory logMessageFactory = new LogMessageFactory();
    private DateTimeFormatter format;
    private PrintStream       currentConsoleStream;
    private Sink              sink;

    @Before
    public void setUp() {
        new File("test.log").delete();
        this.format = DateTimeFormatter
                        .ofPattern("dd:MM:yyyy hh:mm:ss:SSSSSSSSS z");
        Mockito.doReturn(this.format).when(this.sinkConfig).getTimeFormat();
        Mockito.doReturn(OutputStreamSinkConstants.DEFAULT_CHARSET)
                        .when(this.sinkConfig)
                        .getStringProperty(ArgumentMatchers.eq(
                                        OutputStreamSinkConstants.CHARSET_PROPERTY));
        Mockito.doReturn(false).when(this.sinkConfig).isThreadSafe();
        this.currentConsoleStream = System.out;
        this.sink = null;

    }

    @After
    public void tearDown() {
        System.setOut(this.currentConsoleStream);
        if (this.sink != null) {
            this.sink.close();
        }
    }

    @Test
    public void testConsoleLog() throws UnsupportedEncodingException {
        Mockito.doReturn(false).when(this.sinkConfig).isAsyncMode();

        OutputStreamSinkProvider outputStreamSinkProvider = new OutputStreamSinkProvider();
        Mockito.doReturn(OutputStreamSinkConstants.CONSOLE_STREAM)
                        .when(this.sinkConfig)
                        .getStringProperty(ArgumentMatchers.eq(
                                        OutputStreamSinkConstants.FILE_LOCATION_PROPERTY));

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteArrayOutputStream);

        System.setOut(printStream);
        this.sink = outputStreamSinkProvider.createSink(this.sinkConfig,
                        this.logMessageFactory);
        ZonedDateTime time1 = ZonedDateTime.now();
        this.sink.log(this.logMessageFactory.createLogMessage(LogLevel.INFO,
                        time1, "test", "This is test1"));
        ZonedDateTime time2 = ZonedDateTime.now();

        this.sink.log(this.logMessageFactory.createLogMessage(LogLevel.DEBUG,
                        time2, "test", "This is test2"));

        String writtenContent = byteArrayOutputStream
                        .toString(Charset.defaultCharset().name());

        String formattedTime1 = this.format.format(time1);
        String formattedTime2 = this.format.format(time2);
        String expectedContent = "INFO " + formattedTime1
                        + " test This is test1\nDEBUG " + formattedTime2
                        + " test This is test2\n";
        Assert.assertEquals(expectedContent, writtenContent);

    }

    @Test
    public void testFileLog() throws FileNotFoundException, IOException {
        Mockito.doReturn(false).when(this.sinkConfig).isAsyncMode();

        OutputStreamSinkProvider outputStreamSinkProvider = new OutputStreamSinkProvider();

        Mockito.doReturn(OutputStreamSinkConstants.FILE_STREAM
                        + OutputStreamSinkConstants.STREAM_TYPE_SPLIT_DELIMITER
                        + "test.log").when(this.sinkConfig)
                        .getStringProperty(ArgumentMatchers.eq(
                                        OutputStreamSinkConstants.FILE_LOCATION_PROPERTY));

        this.sink = outputStreamSinkProvider.createSink(this.sinkConfig,
                        this.logMessageFactory);
        ZonedDateTime time1 = ZonedDateTime.now();
        this.sink.log(this.logMessageFactory.createLogMessage(LogLevel.INFO,
                        time1, "test", "This is test1"));
        ZonedDateTime time2 = ZonedDateTime.now();

        this.sink.log(this.logMessageFactory.createLogMessage(LogLevel.DEBUG,
                        time2, "test", "This is test2"));
        try (FileInputStream fileInputStream = new FileInputStream(
                        "test.log")) {
            String writtenContent = new String(fileInputStream.readAllBytes(),
                            Charset.defaultCharset());

            String formattedTime1 = this.format.format(time1);
            String formattedTime2 = this.format.format(time2);
            String expectedContent = "INFO " + formattedTime1
                            + " test This is test1\nDEBUG " + formattedTime2
                            + " test This is test2\n";
            Assert.assertEquals(expectedContent, writtenContent);
        }
    }

    @Test
    public void testFileLogCloseReopenSink()
                    throws FileNotFoundException, IOException {
        Mockito.doReturn(false).when(this.sinkConfig).isAsyncMode();

        OutputStreamSinkProvider outputStreamSinkProvider = new OutputStreamSinkProvider();

        Mockito.doReturn(OutputStreamSinkConstants.FILE_STREAM
                        + OutputStreamSinkConstants.STREAM_TYPE_SPLIT_DELIMITER
                        + "test.log").when(this.sinkConfig)
                        .getStringProperty(ArgumentMatchers.eq(
                                        OutputStreamSinkConstants.FILE_LOCATION_PROPERTY));

        this.sink = outputStreamSinkProvider.createSink(this.sinkConfig,
                        this.logMessageFactory);
        ZonedDateTime time1 = ZonedDateTime.now();
        this.sink.log(this.logMessageFactory.createLogMessage(LogLevel.INFO,
                        time1, "test", "This is test1"));
        ZonedDateTime time2 = ZonedDateTime.now();
        this.sink.log(this.logMessageFactory.createLogMessage(LogLevel.DEBUG,
                        time2, "test", "This is test2"));
        this.sink.close();
        this.sink = outputStreamSinkProvider.createSink(this.sinkConfig,
                        this.logMessageFactory);

        ZonedDateTime time3 = ZonedDateTime.now();
        this.sink.log(this.logMessageFactory.createLogMessage(LogLevel.INFO,
                        time3, "test", "This is test3"));

        ZonedDateTime time4 = ZonedDateTime.now();

        this.sink.log(this.logMessageFactory.createLogMessage(LogLevel.DEBUG,
                        time4, "test", "This is test4"));
        this.sink.close();
        try (FileInputStream fileInputStream = new FileInputStream(
                        "test.log")) {
            String writtenContent = new String(fileInputStream.readAllBytes(),
                            Charset.defaultCharset());

            String formattedTime1 = this.format.format(time1);
            String formattedTime2 = this.format.format(time2);
            String formattedTime3 = this.format.format(time3);
            String formattedTime4 = this.format.format(time4);

            String expectedContent = "INFO " + formattedTime1
                            + " test This is test1\nDEBUG " + formattedTime2
                            + " test This is test2\nINFO " + formattedTime3
                            + " test This is test3\nDEBUG " + formattedTime4
                            + " test This is test4\n";
            Assert.assertEquals(expectedContent, writtenContent);
        }
    }

    @Test
    public void testFileLogAsync() throws FileNotFoundException, IOException,
                    InterruptedException {
        Mockito.doReturn(true).when(this.sinkConfig).isAsyncMode();

        OutputStreamSinkProvider outputStreamSinkProvider = new OutputStreamSinkProvider();

        Mockito.doReturn(OutputStreamSinkConstants.FILE_STREAM
                        + OutputStreamSinkConstants.STREAM_TYPE_SPLIT_DELIMITER
                        + "test.log").when(this.sinkConfig)
                        .getStringProperty(ArgumentMatchers.eq(
                                        OutputStreamSinkConstants.FILE_LOCATION_PROPERTY));

        this.sink = outputStreamSinkProvider.createSink(this.sinkConfig,
                        this.logMessageFactory);
        ZonedDateTime time1 = ZonedDateTime.now();
        this.sink.log(this.logMessageFactory.createLogMessage(LogLevel.INFO,
                        time1, "test", "This is test1"));
        ZonedDateTime time2 = ZonedDateTime.now();
        this.sink.log(this.logMessageFactory.createLogMessage(LogLevel.DEBUG,
                        time2, "test", "This is test2"));

        ZonedDateTime time3 = ZonedDateTime.now();
        this.sink.log(this.logMessageFactory.createLogMessage(LogLevel.INFO,
                        time3, "test", "This is test3"));

        ZonedDateTime time4 = ZonedDateTime.now();

        this.sink.log(this.logMessageFactory.createLogMessage(LogLevel.DEBUG,
                        time4, "test", "This is test4"));
        this.sink.close();
        Thread.sleep(500);
        try (FileInputStream fileInputStream = new FileInputStream(
                        "test.log")) {
            String writtenContent = new String(fileInputStream.readAllBytes(),
                            Charset.defaultCharset());

            String formattedTime1 = this.format.format(time1);
            String formattedTime2 = this.format.format(time2);
            String formattedTime3 = this.format.format(time3);
            String formattedTime4 = this.format.format(time4);

            String expectedContent = "INFO " + formattedTime1
                            + " test This is test1\nDEBUG " + formattedTime2
                            + " test This is test2\nINFO " + formattedTime3
                            + " test This is test3\nDEBUG " + formattedTime4
                            + " test This is test4\n";
            Assert.assertEquals(expectedContent, writtenContent);
        }
    }

    @Test(expected = InvalidConfigException.class)
    public void testLogLocationAbscent() throws FileNotFoundException,
                    IOException, InterruptedException {

        OutputStreamSinkProvider outputStreamSinkProvider = new OutputStreamSinkProvider();

        Mockito.doReturn(null).when(this.sinkConfig)
                        .getStringProperty(ArgumentMatchers.eq(
                                        OutputStreamSinkConstants.FILE_LOCATION_PROPERTY));

        this.sink = outputStreamSinkProvider.createSink(this.sinkConfig,
                        this.logMessageFactory);
    }

    @Test(expected = StreamOpenFailedException.class)
    public void testFileInaccessible() throws FileNotFoundException,
                    IOException, InterruptedException {

        OutputStreamSinkProvider outputStreamSinkProvider = new OutputStreamSinkProvider();

        Mockito.doReturn(OutputStreamSinkConstants.FILE_STREAM
                        + OutputStreamSinkConstants.STREAM_TYPE_SPLIT_DELIMITER
                        + "src\test").when(this.sinkConfig)
                        .getStringProperty(ArgumentMatchers.eq(
                                        OutputStreamSinkConstants.FILE_LOCATION_PROPERTY));

        this.sink = outputStreamSinkProvider.createSink(this.sinkConfig,
                        this.logMessageFactory);
    }

    @Test(expected = UnsupportedStreamException.class)
    public void testInvalidStreamType() throws FileNotFoundException,
                    IOException, InterruptedException {

        OutputStreamSinkProvider outputStreamSinkProvider = new OutputStreamSinkProvider();

        Mockito.doReturn("test"
                        + OutputStreamSinkConstants.STREAM_TYPE_SPLIT_DELIMITER
                        + "test.log").when(this.sinkConfig)
                        .getStringProperty(ArgumentMatchers.eq(
                                        OutputStreamSinkConstants.FILE_LOCATION_PROPERTY));

        this.sink = outputStreamSinkProvider.createSink(this.sinkConfig,
                        this.logMessageFactory);
    }

}

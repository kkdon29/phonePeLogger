package com.phonepe.logger;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.nio.charset.Charset;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.phonepe.logger.util.Constants;

public class LoggerTest {

    private Logger      logger;
    private PrintStream curSysout;

    @Before
    public void setUp() throws NoSuchFieldException, SecurityException,
                    IllegalArgumentException, IllegalAccessException {
        new File("test.log").delete();
        System.setProperty(Constants.CONFIG_FILE_LOCATION_KEY,
                        "src/test/resources/SinkConfigs.properties");
        System.setProperty(Constants.SINK_PROVIDER_REGISTRY_FILE_KEY,
                        "src/test/resources/SinkRegistry.properties");
        this.curSysout = System.out;
    }

    @After
    public void tearDown() throws NoSuchFieldException, SecurityException,
                    IllegalArgumentException, IllegalAccessException {
        if (this.logger != null) {
            this.logger.close();
        }
        this.logger = null;
        System.setOut(this.curSysout);
    }

    @Test
    public void testLoggerSingleton() {
        this.logger = Logger.getLogger();

        Logger logger1 = Logger.getLogger();
        Assert.assertSame(this.logger, logger1);
    }

    @Test
    public void testLog() throws FileNotFoundException, IOException,
                    InterruptedException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteArrayOutputStream);
        System.setOut(printStream);
        this.logger = Logger.getLogger();

        this.logger.log(LogLevel.INFO, LoggerTest.class.getName(),
                        TestUtils.prepareMessageForLog(1, 1));
        // Adding a sleep in order to let Asynchronous logger finish logging
        Thread.sleep(500);
        try (BufferedReader reader = new BufferedReader(
                        new FileReader("test.log"))) {
            String logMessage = reader.readLine();
            ParsedLogMessage parsedLogMessage = TestUtils.parseLog(logMessage);
            Assert.assertEquals(LogLevel.INFO, parsedLogMessage.getLogLevel());
            Assert.assertEquals(LoggerTest.class.getName(),
                            parsedLogMessage.getNamespace());
            Assert.assertEquals(1, parsedLogMessage.getLogId());
            Assert.assertEquals(1, parsedLogMessage.getThreadId());
        }
        String log = byteArrayOutputStream
                        .toString(Charset.defaultCharset().name());
        try (BufferedReader reader = new BufferedReader(
                        new StringReader(log))) {
            String logMessage = reader.readLine();
            ParsedLogMessage parsedLogMessage = TestUtils.parseLog(logMessage);
            Assert.assertEquals(LogLevel.INFO, parsedLogMessage.getLogLevel());
            Assert.assertEquals(LoggerTest.class.getName(),
                            parsedLogMessage.getNamespace());
            Assert.assertEquals(1, parsedLogMessage.getLogId());
            Assert.assertEquals(1, parsedLogMessage.getThreadId());

        }
    }
}

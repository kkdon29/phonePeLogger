package com.phonepe.logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.phonepe.logger.util.Constants;

public class LoggerStressTest {

    private final int      numThreads    = 100;
    private final int      logsPerThread = 1000;
    private LogWorker      logWorkers[];
    private CountDownLatch countDownLatch;

    @Before
    public void setUp() throws Exception {
        this.countDownLatch = new CountDownLatch(this.numThreads);
        new File("test-sync.log").delete();
        new File("test-async.log").delete();

        System.setProperty(Constants.CONFIG_FILE_LOCATION_KEY,
                        "src/test/resources/StressTestSinkConfigs.properties");
        System.setProperty(Constants.SINK_PROVIDER_REGISTRY_FILE_KEY,
                        "src/test/resources/StressTestSinkRegistry.properties");

        this.logWorkers = new LogWorker[this.numThreads];
        for (int i = 0; i < this.numThreads; i++) {
            this.logWorkers[i] = new LogWorker(i);
        }
    }

    @After
    public void tearDown() throws Exception {
    }

    private class LogWorker implements Runnable {
        private int threadId;

        public LogWorker(int threadId) {
            this.threadId = threadId;
        }

        @Override
        public void run() {
            LoggerStressTest.this.countDownLatch.countDown();
            try {
                LoggerStressTest.this.countDownLatch.await();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
            Logger logger = Logger.getLogger();
            for (int i = 0; i < LoggerStressTest.this.logsPerThread; i++) {
                logger.log(LogLevel.INFO, LoggerStressTest.class.getName(),
                                TestUtils.prepareMessageForLog(i,
                                                this.threadId));
            }
        }
    }

    @Test
    public void test() throws InterruptedException, FileNotFoundException,
                    IOException {
        Thread threads[] = new Thread[this.numThreads];
        for (int i = 0; i < this.numThreads; i++) {
            threads[i] = new Thread(this.logWorkers[i]);
            threads[i].start();
        }
        for (int i = 0; i < this.numThreads; i++) {
            threads[i].join();
        }
        Logger.getLogger().close();
        Thread.sleep(1000);
        int threadLogCount[] = new int[this.numThreads];
        Arrays.fill(threadLogCount, 0);
        try (BufferedReader reader = new BufferedReader(
                        new FileReader("test-sync.log"))) {
            while (true) {
                String log = reader.readLine();
                if (log == null) {
                    break;
                }
                ParsedLogMessage parsedLogMessage = TestUtils.parseLog(log);
                Assert.assertEquals(LogLevel.INFO,
                                parsedLogMessage.getLogLevel());
                Assert.assertEquals(LoggerStressTest.class.getName(),
                                parsedLogMessage.getNamespace());
                Assert.assertEquals(
                                threadLogCount[parsedLogMessage.getThreadId()],
                                parsedLogMessage.getLogId());
                threadLogCount[parsedLogMessage.getThreadId()]++;
            }
        }
        for (int i = 0; i < this.numThreads; i++) {
            Assert.assertEquals(this.logsPerThread, threadLogCount[i]);
        }
        Arrays.fill(threadLogCount, 0);

        try (BufferedReader reader = new BufferedReader(
                        new FileReader("test-async.log"))) {
            while (true) {
                String log = reader.readLine();
                if (log == null) {
                    break;
                }
                ParsedLogMessage parsedLogMessage = TestUtils.parseLog(log);
                Assert.assertEquals(LogLevel.INFO,
                                parsedLogMessage.getLogLevel());
                Assert.assertEquals(LoggerStressTest.class.getName(),
                                parsedLogMessage.getNamespace());
                Assert.assertEquals(
                                threadLogCount[parsedLogMessage.getThreadId()],
                                parsedLogMessage.getLogId());
                threadLogCount[parsedLogMessage.getThreadId()]++;
            }
        }
        for (int i = 0; i < this.numThreads; i++) {
            Assert.assertEquals(this.logsPerThread, threadLogCount[i]);
        }
    }

}

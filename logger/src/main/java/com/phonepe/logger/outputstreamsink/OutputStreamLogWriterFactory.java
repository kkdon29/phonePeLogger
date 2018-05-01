package com.phonepe.logger.outputstreamsink;

import java.io.OutputStream;

import com.phonepe.logger.LogType;
import com.phonepe.logger.LogWriter;
import com.phonepe.logger.impl.LogMessageFactory;
import com.phonepe.logger.sink.config.SinkConfig;

/**
 * Factory class for creating appropriate {@link LogWriter} for specified
 * {@link OutputStream} and {@link SinkConfig}
 *
 * @author ASUS
 */
public class OutputStreamLogWriterFactory {
    private LogWriter createAsyncLogWriter(OutputStream oStream,
                    SinkConfig sinkConfig,
                    LogMessageFactory logMessageFactory) {
        BlockingQueueLogReaderWriter blockingQueueLogReaderWriter = new BlockingQueueLogReaderWriter(
                        sinkConfig);
        LogWriter logWriter = blockingQueueLogReaderWriter;
        AsynchronousOutputStreamLoggerTask asynchronousOutputStreamLoggerTask = new AsynchronousOutputStreamLoggerTask(
                        sinkConfig, blockingQueueLogReaderWriter,
                        this.createSyncLogWriter(oStream, sinkConfig),
                        logMessageFactory);
        Thread asyncThread = new Thread(asynchronousOutputStreamLoggerTask);
        asyncThread.start();
        return logWriter;

    }

    private LogWriter createSyncLogWriter(OutputStream oStream,
                    SinkConfig sinkConfig) {
        return new OutputStreamLogWriter(oStream, sinkConfig);
    }

    /**
     * Creates appropriate {@link LogWriter}. In case
     * {@link SinkConfig#isAsyncMode()} is set to <code>true</code>, returns
     * {@link BlockingQueueLogReaderWriter} and starts thread to read messages
     * from {@link BlockingQueueLogReaderWriter} and persist them to
     * {@link OutputStreamLogWriter}.
     * In case {@link SinkConfig#isAsyncMode()} is <code>false</code> simply
     * returns {@link OutputStreamLogWriter}
     *
     * @param oStream
     *            {@link OutputStream} to be used for persistance
     * @param sinkConfig
     *            {@link SinkConfig} needed for this {@link LogWriter}
     * @param logMessageFactory
     *            {@link LogMessageFactory} instance to create required
     *            {@link LogType#FRAMEWORK_LOG} messages
     * @return Appropriate {@link LogWriter}
     */
    public LogWriter createLogWriter(OutputStream oStream,
                    SinkConfig sinkConfig,
                    LogMessageFactory logMessageFactory) {
        if (sinkConfig.isAsyncMode()) {
            return this.createAsyncLogWriter(oStream, sinkConfig,
                            logMessageFactory);
        }
        return this.createSyncLogWriter(oStream, sinkConfig);
    }
}

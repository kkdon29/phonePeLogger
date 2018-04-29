package com.phonepe.logger.outputstreamsink;

import java.io.OutputStream;

import com.phonepe.logger.LogWriter;
import com.phonepe.logger.impl.LogMessageFactory;
import com.phonepe.logger.sink.config.SinkConfig;

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

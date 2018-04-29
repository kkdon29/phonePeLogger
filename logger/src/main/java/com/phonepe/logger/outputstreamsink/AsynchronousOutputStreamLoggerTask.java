package com.phonepe.logger.outputstreamsink;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.phonepe.logger.LogMessage;
import com.phonepe.logger.LogReader;
import com.phonepe.logger.LogWriter;
import com.phonepe.logger.impl.LogMessageFactory;
import com.phonepe.logger.sink.config.SinkConfig;
import com.phonepe.logger.util.Utils;

public class AsynchronousOutputStreamLoggerTask implements Runnable {
    private LogReader         logReader;
    private LogWriter         logWriter;
    private static final int  BUFFER_POLL_TIMEOUT_SEC = 2;
    private LogMessageFactory logMessageFactory;

    public AsynchronousOutputStreamLoggerTask(SinkConfig sinkConfig,
                    LogReader logReader, LogWriter logWriter,
                    LogMessageFactory logMessageFactory) {
        this.logReader = logReader;
        this.logWriter = logWriter;
        this.logMessageFactory = logMessageFactory;
    }

    @Override
    public void run() {
        boolean isClosed = false;
        try {

            while (!isClosed) {
                LogMessage logMessage = this.logReader.getNextLog(
                                AsynchronousOutputStreamLoggerTask.BUFFER_POLL_TIMEOUT_SEC,
                                TimeUnit.SECONDS);
                if (logMessage == null) {
                    continue;
                }
                if (Utils.isCloseSinkMessage(logMessage)) {
                    isClosed = true;
                }
                else {
                    this.logWriter.log(logMessage);
                }
            }
        }
        catch (InterruptedException | IOException e) {
            System.out.println("Error writing log message");
            e.printStackTrace();
        }
        finally {
            try {
                this.logWriter.log(
                                this.logMessageFactory.sinkCloseLogMessage());

            }
            catch (IOException e) {
                System.out.println("Error closing log writer");
                e.printStackTrace();
            }
        }

    }

}

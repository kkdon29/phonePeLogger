package com.phonepe.logger.outputstreamsink;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.phonepe.logger.LogMessage;
import com.phonepe.logger.LogReader;
import com.phonepe.logger.LogType;
import com.phonepe.logger.LogWriter;
import com.phonepe.logger.impl.LogMessageFactory;
import com.phonepe.logger.sink.Sink;
import com.phonepe.logger.sink.config.SinkConfig;
import com.phonepe.logger.util.Utils;

/**
 * Task which keeps logging {@link LogMessage}s delivered by {@link LogReader}
 * corresponding to this {@link OutputStreamSink}
 *
 * @author Kaustubh Khasnis
 */
public class AsynchronousOutputStreamLoggerTask implements Runnable {
    private LogReader         logReader;
    private LogWriter         logWriter;
    private LogMessageFactory logMessageFactory;

    /**
     * Constructor for this {@link AsynchronousOutputStreamLoggerTask}
     *
     * @param sinkConfig
     *            {@link SinkConfig} for this {@link Sink}
     * @param logReader
     *            {@link LogReader} to read {@link LogMessage}s from
     *            intermediate buffer/log store
     * @param logWriter
     *            {@link LogWriter} to write {@link LogMessage}s to desired
     *            destination
     * @param logMessageFactory
     *            {@link LogMessageFactory} to help create any
     *            {@link LogType#FRAMEWORK_LOG} type {@link LogMessage}s
     *            required
     */
    public AsynchronousOutputStreamLoggerTask(SinkConfig sinkConfig,
                    LogReader logReader, LogWriter logWriter,
                    LogMessageFactory logMessageFactory) {
        this.logReader = logReader;
        this.logWriter = logWriter;
        this.logMessageFactory = logMessageFactory;
    }

    /**
     * Run method which waits max
     * {@link OutputStreamSinkConstants#BUFFER_POLL_TIMEOUT_SEC} seconds in a
     * loop to read next {@link LogMessage} from provided {@link LogReader}.
     * The message is immediately pushed to desired {@link LogWriter} unless it
     * indicates Close {@link LogMessage}. In that case, another Close
     * {@link LogMessage} is sent to {@link LogWriter}. Any failure in writing
     * to {@link LogWriter} or reading from {@link LogReader} results in close
     * {@link LogMessage} being pushed to {@link LogWriter}
     */
    @Override
    public void run() {
        boolean isClosed = false;
        try {

            while (!isClosed) {
                LogMessage logMessage = this.logReader.getNextLog(
                                OutputStreamSinkConstants.BUFFER_POLL_TIMEOUT_SEC,
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

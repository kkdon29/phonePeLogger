package com.phonepe.logger.outputstreamsink;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.phonepe.logger.LogMessage;
import com.phonepe.logger.LogReader;
import com.phonepe.logger.LogWriter;
import com.phonepe.logger.outputstreamsink.exception.UnsupportedLogTypeException;
import com.phonepe.logger.sink.config.SinkConfig;
import com.phonepe.logger.util.NoOPLock;
import com.phonepe.logger.util.Utils;

/**
 * A {@link LogWriter} and {@link LogReader} using unbound {@link BlockingQueue}
 * as log store. It serves as intermediate buffer for asynchronous
 * {@link OutputStreamSink}.
 *
 * @author Kaustubh Khasnis
 */
public class BlockingQueueLogReaderWriter implements LogWriter , LogReader {
    private LinkedBlockingQueue<LogMessage> messageQueue;
    private volatile boolean                isClosed = false;
    private final Lock                      lock;

    public BlockingQueueLogReaderWriter(SinkConfig sinkConfig) {
        if (sinkConfig.isThreadSafe()) {
            this.lock = new ReentrantLock();
        }
        else {
            this.lock = new NoOPLock();
        }
        this.messageQueue = new LinkedBlockingQueue<>();
    }

    @Override
    public void log(LogMessage logMessage)
                    throws IOException, UnsupportedLogTypeException {
        switch (logMessage.getLogType()) {
            case USER_LOG:
                try {
                    this.lock.lock();
                    if (!this.isClosed) {
                        this.messageQueue.offer(logMessage);
                    }
                }
                finally {
                    this.lock.unlock();
                }
                break;
            case FRAMEWORK_LOG:
                try {
                    if (Utils.isCloseSinkMessage(logMessage)) {
                        this.lock.lock();

                        if (!this.isClosed) {
                            this.messageQueue.offer(logMessage);
                            this.isClosed = true;
                        }
                    }
                }
                finally {
                    this.lock.unlock();
                }
                break;
            default:
                throw new UnsupportedLogTypeException(logMessage.getLogType());
        }
    }

    @Override
    public LogMessage getNextLog(long timeOut, TimeUnit unit)
                    throws InterruptedException {
        return this.messageQueue.poll(timeOut, unit);
    }

}

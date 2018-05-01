package com.phonepe.logger.outputstreamsink;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.phonepe.logger.LogMessage;
import com.phonepe.logger.LogWriter;
import com.phonepe.logger.outputstreamsink.exception.UnsupportedLogTypeException;
import com.phonepe.logger.sink.config.SinkConfig;
import com.phonepe.logger.util.NoOPLock;
import com.phonepe.logger.util.Utils;

/**
 * {@link LogWriter} to publish messages to specified {@link OutputStream}
 *
 * @author Kaustubh Khasnis
 */
public class OutputStreamLogWriter implements LogWriter {
    private final Lock       lock;
    private OutputStream     outputStream;
    private volatile boolean isClosed = false;
    private SinkConfig       sinkConfig;
    private Charset          charset;

    /**
     * Creates an {@link OutputStream} based {@link LogWriter}. It also passes
     * {@link SinkConfig} so that the thread safety choice of user can be obeyed
     * Further, it uses {@link OutputStreamSinkConstants#CHARSET_PROPERTY} in
     * {@link SinkConfig} to determine {@link Charset} used to encode serialized
     * {@link LogMessage}s into bytes. Default to be used is
     * {@link OutputStreamSinkConstants#DEFAULT_CHARSET}. When it is set to
     * "default", JVM default {@link Charset} is used.
     *
     * @param outputStream
     *            {@link OutputStream} to push {@link LogMessage}s to
     * @param sinkConfig
     *            {@link SinkConfig} to be used for pushing {@link LogMessage}s
     */
    public OutputStreamLogWriter(OutputStream outputStream,
                    SinkConfig sinkConfig) {
        this.outputStream = outputStream;
        this.sinkConfig = sinkConfig;
        if (sinkConfig.isThreadSafe()) {
            this.lock = new ReentrantLock();
        }
        else {
            this.lock = new NoOPLock();
        }
        String charset = sinkConfig.getStringProperty(
                        OutputStreamSinkConstants.CHARSET_PROPERTY);
        if (Utils.checkNullOrEmpty(charset) || charset.equalsIgnoreCase(
                        OutputStreamSinkConstants.DEFAULT_CHARSET)) {
            this.charset = Charset.defaultCharset();
        }
        else {
            this.charset = Charset.forName(charset);
        }

    }

    private String serializeLog(LogMessage logMessage) {
        StringBuilder sb = new StringBuilder();
        sb.append(logMessage.getLogLevel()).append(" ").append(this.sinkConfig
                        .getTimeFormat().format(logMessage.getDate()));
        sb.append(" ").append(logMessage.getNamespace()).append(" ")
                        .append(logMessage.getMessage()).append('\n');
        return sb.toString();
    }

    @Override
    public void log(LogMessage logMessage)
                    throws IOException, UnsupportedLogTypeException {

        switch (logMessage.getLogType()) {
            case USER_LOG:
                String message = this.serializeLog(logMessage);
                byte bytes[] = message.getBytes(this.charset);
                try {
                    this.lock.lock();
                    if (!this.isClosed) {
                        this.outputStream.write(bytes);
                        this.outputStream.flush();
                    }
                }
                finally {
                    this.lock.unlock();
                }
                break;
            case FRAMEWORK_LOG:
                if (Utils.isCloseSinkMessage(logMessage)) {
                    try {
                        this.lock.lock();
                        if (!this.isClosed) {
                            this.outputStream.close();
                            this.isClosed = true;
                        }
                    }
                    finally {
                        this.lock.unlock();
                    }
                }
                break;
            default:
                throw new UnsupportedLogTypeException(logMessage.getLogType());

        }
    }
}

package com.phonepe.logger.outputstreamsink;

import java.io.IOException;
import java.io.OutputStream;

import com.phonepe.logger.LogMessage;
import com.phonepe.logger.LogWriter;
import com.phonepe.logger.impl.LogMessageFactory;
import com.phonepe.logger.sink.Sink;
import com.phonepe.logger.sink.config.SinkConfig;

public class OutputStreamSink implements Sink {

    protected SinkConfig      sinkConfig;
    private LogWriter         logWriter;
    private LogMessageFactory logMessageFactory;

    public OutputStreamSink(OutputStream outputStream, SinkConfig sinkConfig,
                    LogMessageFactory logMessageFactory,
                    OutputStreamLogWriterFactory outputStreamLogWriterFactory) {
        this.logMessageFactory = logMessageFactory;
        this.logWriter = outputStreamLogWriterFactory.createLogWriter(
                        outputStream, sinkConfig, logMessageFactory);
    }

    @Override
    public void log(LogMessage logMessage) {
        try {
            this.logWriter.log(logMessage);
        }
        catch (IOException e) {
            e.printStackTrace();
            this.close();
        }
    }

    @Override
    public void close() {
        try {
            this.logWriter.log(this.logMessageFactory.sinkCloseLogMessage());
        }
        catch (IOException e) {
            System.out.println("Error while closing sink");
        }
    }

}

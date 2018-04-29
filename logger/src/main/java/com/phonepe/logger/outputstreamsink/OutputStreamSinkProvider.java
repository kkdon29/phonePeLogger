package com.phonepe.logger.outputstreamsink;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.phonepe.logger.impl.LogMessageFactory;
import com.phonepe.logger.outputstreamsink.exception.StreamOpenFailedException;
import com.phonepe.logger.outputstreamsink.exception.UnsupportedStreamException;
import com.phonepe.logger.sink.Sink;
import com.phonepe.logger.sink.SinkProvider;
import com.phonepe.logger.sink.config.SinkConfig;
import com.phonepe.logger.sink.config.exception.InvalidConfigException;
import com.phonepe.logger.util.Utils;

public class OutputStreamSinkProvider implements SinkProvider {

    @Override
    public Sink createSink(SinkConfig config,
                    LogMessageFactory logMessageFactory)
                    throws InvalidConfigException, UnsupportedStreamException,
                    StreamOpenFailedException {
        String fileLocation = config.getStringProperty(
                        OutputStreamSinkConstants.FILE_LOCATION_PROPERTY);
        if (Utils.checkNullOrEmpty(fileLocation)) {
            throw new InvalidConfigException(
                            OutputStreamSinkConstants.FILE_LOCATION_PROPERTY
                                            + " not available in " + config);
        }
        String fileLocationSplit[] = fileLocation.split(
                        OutputStreamSinkConstants.STREAM_TYPE_SPLIT_DELIMITER);
        String streamType = fileLocationSplit[0];
        switch (streamType) {
            case OutputStreamSinkConstants.CONSOLE_STREAM:
                return new OutputStreamSink(System.out, config,
                                logMessageFactory,
                                new OutputStreamLogWriterFactory());
            case OutputStreamSinkConstants.FILE_STREAM:
                try {

                    return new OutputStreamSink(
                                    new FileOutputStream(fileLocationSplit[1],
                                                    true),
                                    config, logMessageFactory,
                                    new OutputStreamLogWriterFactory());
                }
                catch (FileNotFoundException ex) {
                    throw new StreamOpenFailedException(ex);
                }
            default:
                throw new UnsupportedStreamException(streamType);
        }

    }

}

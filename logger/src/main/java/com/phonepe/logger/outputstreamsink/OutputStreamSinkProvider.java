package com.phonepe.logger.outputstreamsink;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.phonepe.logger.impl.LogMessageFactory;
import com.phonepe.logger.outputstreamsink.exception.StreamOpenFailedException;
import com.phonepe.logger.outputstreamsink.exception.UnsupportedStreamException;
import com.phonepe.logger.sink.Sink;
import com.phonepe.logger.sink.SinkProvider;
import com.phonepe.logger.sink.config.SinkConfig;
import com.phonepe.logger.sink.config.exception.InvalidConfigException;
import com.phonepe.logger.util.Utils;

/**
 * {@link SinkProvider} implementation to create {@link Sink} instances for
 * {@link OutputStreamSink}
 *
 * @author Kaustubh Khasnis
 */
public class OutputStreamSinkProvider implements SinkProvider {

    /**
     * Creates {@link OutputStreamSink} using specified {@link SinkConfig}.
     * {@link OutputStreamSinkConstants#FILE_LOCATION_PROPERTY} in
     * {@link SinkConfig} determines type and location of the
     * {@link OutputStreamSink}. The format if value of this key is "type" then
     * {@link OutputStreamSinkConstants#STREAM_TYPE_SPLIT_DELIMITER} and then
     * location if needed
     * When type is {@link OutputStreamSinkConstants#CONSOLE_STREAM}, it prints
     * to
     * {@link System#out}. Location not needed
     * When its {@link OutputStreamSinkConstants#FILE_STREAM}, location is
     * nothing but file path. These are the only 2 right now supported.
     *
     * @throws InvalidConfigException
     *             when {@link OutputStreamSinkConstants#FILE_LOCATION_PROPERTY}
     *             is abscent in {@link SinkConfig}
     * @throws UnsupportedStreamException
     *             when {@link OutputStreamSinkConstants#FILE_LOCATION_PROPERTY}
     *             points to an unsupported stream type.
     * @throws StreamOpenFailedException
     *             When {@link OutputStream} could not be created/opened
     */
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

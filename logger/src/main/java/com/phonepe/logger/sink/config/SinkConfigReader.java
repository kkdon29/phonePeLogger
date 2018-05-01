package com.phonepe.logger.sink.config;

import java.util.List;

/**
 * Interface which can help read multiple {@link SinkConfig} from given
 * {@link SinkConfig} store such as properties file
 *
 * @author Kaustubh Khasnis
 */
public interface SinkConfigReader {

    /**
     * @return {@link List} of {@link SinkConfig} to be read
     */
    List<SinkConfig> getConfigs();

}
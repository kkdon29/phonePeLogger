package com.phonepe.logger.util;

/**
 * Framework level constants class to store various constants required by
 * different classes
 *
 * @author Kaustubh Khasnis
 */
public class Constants {
    public static final String CONFIG_FILE_LOCATION_KEY        = "sinks.config";
    public static final String TS_FORMAT_KEY                   = "ts_format";
    public static final String LOG_LEVEL_KEY                   = "log_level";
    public static final String SINK_TYPE_KEY                   = "sink_type";
    public static final String THREAD_MODEL_KEY                = "thread_model";
    public static final String WRITE_MODE_KEY                  = "write_mode";
    public static final String SINGLE_THREADED_MODE            = "SINGLE";
    public static final String MULTI_THREADED_MODE             = "MULTI";
    public static final String SYNC_WRITE_MODE                 = "SYNC";
    public static final String ASYNC_WRITE_MODE                = "ASYNC";
    public static final String SINK_PROVIDER_REGISTRY_FILE_KEY = "sinkproviders.config";
    public static final String CLOSE_SINK_MESSAGE              = "CLOSE";
    public static final String DEFAULT_CONFIG_FILE_LOCATION    = "sinkConfig.properties";
    public static final String DEFAULT_SINK_PROVIDER_CONFIG    = "sinkProviderConfig.properties";
    public static final String DEFAULT_THREAD_MODE             = Constants.SINGLE_THREADED_MODE;
    public static final String DEFAULT_WRITE_MODE              = Constants.SYNC_WRITE_MODE;
}

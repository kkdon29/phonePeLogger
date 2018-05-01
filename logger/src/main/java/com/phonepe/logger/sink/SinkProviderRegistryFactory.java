package com.phonepe.logger.sink;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;

import com.phonepe.logger.sink.exception.SinkProviderAlreadyRegisteredException;
import com.phonepe.logger.sink.exception.SinkProviderRegistryConfigException;
import com.phonepe.logger.util.Constants;

/**
 * A registry class for creating {@link SinkProvider}s.It reads
 * {@link SinkProvider} configurations from registry file pointed by
 * {@link Constants#SINK_PROVIDER_REGISTRY_FILE_KEY}. The file is expected to be
 * in properties file format. It needs to have mapping between sink types and
 * {@link SinkProvider} implementations.
 *
 * @author Kaustubh Khasnis
 */
public class SinkProviderRegistryFactory {
    /**
     * Creates {@link SinkProviderRegistry} with help of given parameters and
     * property file.
     *
     * @param loggerProperties
     *            Framework level configuration {@link Properties} used by
     *            various classes to get configurations.
     * @param sinkLoader
     *            {@link DynamicSinkProviderLoader} instance which can load
     *            various {@link SinkProvider} implementations at run time.
     * @return
     *         {@link SinkProviderRegistry} instance
     * @throws SinkProviderRegistryConfigException
     *             in case of invalid config
     * @throws SinkProviderAlreadyRegisteredException
     *             when there are duplicate sinkTypes in given registry file
     */
    public SinkProviderRegistry createSinkProviderRegistry(
                    Properties loggerProperties,
                    DynamicSinkProviderLoader sinkLoader)
                    throws SinkProviderRegistryConfigException,
                    SinkProviderAlreadyRegisteredException {
        String sinkRegistryFileLocation = loggerProperties
                        .getProperty(Constants.SINK_PROVIDER_REGISTRY_FILE_KEY);
        if (sinkRegistryFileLocation == null) {
            throw new SinkProviderRegistryConfigException(
                            Constants.SINK_PROVIDER_REGISTRY_FILE_KEY
                                            + " not provided");
        }
        return this.populateSinkProviderRegistry(sinkRegistryFileLocation,
                        sinkLoader);
    }

    /*
     * Creates and populates {@link SinkProviderRegistry} with all the sink type
     * and {@link SinkProvider} mappings by reading file pointed by
     * sinkRegistryFileLocation
     */
    private SinkProviderRegistry populateSinkProviderRegistry(
                    String sinkRegistryFileLocation,
                    DynamicSinkProviderLoader sinkLoader) {
        SinkProviderRegistry sinkProviderRegistry = new SinkProviderRegistry();
        try (BufferedReader br = new BufferedReader(
                        new FileReader(sinkRegistryFileLocation))) {
            Properties registry = new Properties() {
                @Override
                public synchronized Object put(Object key, Object value) {
                    if (this.get(key) != null) {
                        throw new SinkProviderAlreadyRegisteredException(
                                        (String) key);
                    }
                    return super.put(key, value);
                }
            };
            registry.load(br);
            for (Entry<Object, Object> entry : registry.entrySet()) {
                String sinkType = entry.getKey().toString().toLowerCase();
                String providerClass = entry.getValue().toString();
                SinkProvider sinkProvider = sinkLoader
                                .loadSinkProviderbyClassName(providerClass);
                sinkProviderRegistry.registerSinkProvider(sinkType,
                                sinkProvider);
            }
            return sinkProviderRegistry;
        }
        catch (IOException ex) {
            throw new SinkProviderRegistryConfigException(
                            "Error reading sink provider registry file", ex);
        }

    }

}

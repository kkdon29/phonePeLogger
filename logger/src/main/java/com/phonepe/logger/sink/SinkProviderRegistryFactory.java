package com.phonepe.logger.sink;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;

import com.phonepe.logger.sink.exception.SinkProviderAlreadyRegisteredException;
import com.phonepe.logger.sink.exception.SinkProviderRegistryConfigException;
import com.phonepe.logger.util.Constants;

public class SinkProviderRegistryFactory {
    public SinkProviderRegistry createSinkProviderRegistry(
                    Properties loggerProperties,
                    DynamicSinkProviderLoader sinkLoader) {
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

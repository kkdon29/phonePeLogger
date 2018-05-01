package com.phonepe.logger.sink;

import java.util.HashMap;
import java.util.Map;

import com.phonepe.logger.sink.config.SinkConfig;

/**
 * Registry containing mapping between sink types(specified in
 * {@link SinkConfig} and {@link SinkProvider}s
 *
 * @author Kaustubh Khasnis
 */
class SinkProviderRegistry {
    private Map<String, SinkProvider> sinkProviders;

    public SinkProviderRegistry() {
        this.sinkProviders = new HashMap<>();
    }

    /**
     * Registers the {@link SinkProvider} for given sinkType
     * 
     * @param sinkType
     *            sinkType (to be used in {@link SinkConfig}
     * @param provider
     *            {@link SinkProvider} to map to
     */
    public void registerSinkProvider(String sinkType, SinkProvider provider) {
        this.sinkProviders.put(sinkType, provider);
    }

    /**
     * Returns the {@link SinkProvider} for this sinkType
     * 
     * @param sinkType
     *            Sink Type that you need {@link SinkProvider} for
     * @return mapped {@link SinkProvider}
     */
    public SinkProvider getProvider(String sinkType) {
        return this.sinkProviders.get(sinkType);
    }
}

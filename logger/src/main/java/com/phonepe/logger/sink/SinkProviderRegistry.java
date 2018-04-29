package com.phonepe.logger.sink;

import java.util.HashMap;
import java.util.Map;

class SinkProviderRegistry {
    private Map<String, SinkProvider> sinkProviders;

    public SinkProviderRegistry() {
        this.sinkProviders = new HashMap<>();
    }

    public void registerSinkProvider(String sinkType, SinkProvider provider) {
        this.sinkProviders.put(sinkType, provider);
    }

    public SinkProvider getProvider(String sinkType) {
        return this.sinkProviders.get(sinkType);
    }
}

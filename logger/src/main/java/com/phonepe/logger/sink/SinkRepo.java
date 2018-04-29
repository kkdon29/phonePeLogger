package com.phonepe.logger.sink;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.phonepe.logger.LogLevel;

import exception.InvalidArgumentException;

class SinkRepo {
    private Map<LogLevel, List<Sink>> levelSinkMapping;

    public SinkRepo() {
        this.levelSinkMapping = new HashMap<>();
    }

    public void registerSink(LogLevel level, Sink sink)
                    throws InvalidArgumentException {
        this.levelSinkMapping.putIfAbsent(level, new ArrayList<>());
        this.levelSinkMapping.get(level).add(sink);
    }

    @SuppressWarnings("unchecked")
    public List<Sink> getSinksForLogLevel(LogLevel logLevel) {
        return Collections.unmodifiableList(this.levelSinkMapping
                        .getOrDefault(logLevel, Collections.EMPTY_LIST));
    }

    public void closeSinks() {
        for (List<Sink> sinks : this.levelSinkMapping.values()) {
            for (Sink sink : sinks) {
                sink.close();
            }
        }
    }
}

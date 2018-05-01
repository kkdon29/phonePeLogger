package com.phonepe.logger.sink;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.phonepe.logger.LogLevel;

/**
 * Repository to map all {@link LogLevel}s to the {@link Sink} instances
 * supporting them
 *
 * @author Kaustubh Khasnis
 */
class SinkRepo {
    private Map<LogLevel, List<Sink>> levelSinkMapping;

    public SinkRepo() {
        this.levelSinkMapping = new HashMap<>();
    }

    /**
     * Registers the {@link Sink} against given {@link LogLevel}
     *
     * @param level
     *            {@link LogLevel} to register against
     * @param sink
     *            {@link Sink} to be registered
     */
    public void registerSink(LogLevel level, Sink sink) {
        this.levelSinkMapping.putIfAbsent(level, new ArrayList<>());
        this.levelSinkMapping.get(level).add(sink);
    }

    /**
     * Returns all {@link Sink} instances supporting this {@link LogLevel}
     *
     * @param logLevel
     *            {@link LogLevel} for which the {@link Sink}s are needed
     * @return Immutable {@link List} of {@link Sink}s supporting this
     *         {@link LogLevel}
     */
    @SuppressWarnings("unchecked")
    public List<Sink> getSinksForLogLevel(LogLevel logLevel) {
        return Collections.unmodifiableList(this.levelSinkMapping
                        .getOrDefault(logLevel, Collections.EMPTY_LIST));
    }

    /**
     * Close all sinks in this repository
     */
    public void closeSinks() {
        for (List<Sink> sinks : this.levelSinkMapping.values()) {
            for (Sink sink : sinks) {
                sink.close();
            }
        }
    }
}

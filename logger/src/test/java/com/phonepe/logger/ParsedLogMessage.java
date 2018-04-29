package com.phonepe.logger;

import java.time.temporal.TemporalAccessor;

public class ParsedLogMessage {
    private LogLevel         logLevel;
    private TemporalAccessor time;
    private String           namespace;
    private int              threadId;
    private int              logId;

    public LogLevel getLogLevel() {
        return this.logLevel;
    }

    public void setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
    }

    public TemporalAccessor getTime() {
        return this.time;
    }

    public void setTime(TemporalAccessor time) {
        this.time = time;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public int getThreadId() {
        return this.threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }

    public int getLogId() {
        return this.logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }
}

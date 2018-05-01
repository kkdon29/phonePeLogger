package com.phonepe.logger.impl;

import java.io.Serializable;
import java.time.ZonedDateTime;

import com.phonepe.logger.LogLevel;
import com.phonepe.logger.LogMessage;
import com.phonepe.logger.LogType;

/**
 * Implementation of {@link LogMessage} interface with {@link Object#hashCode()}
 * and {@link Object#equals(Object)} overridden
 *
 * @author Kaustubh Khasnis
 */
class LogMessageImpl implements LogMessage , Serializable {
    private static final long serialVersionUID = -7540940587517246569L;
    private LogLevel          logLevel;
    private ZonedDateTime     date;
    private String            namespace;
    private String            message;
    private LogType           logType          = LogType.USER_LOG;

    @Override
    public LogLevel getLogLevel() {
        return this.logLevel;
    }

    public void setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
    }

    @Override
    public ZonedDateTime getDate() {
        return this.date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    @Override
    public String getNamespace() {
        return this.namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public LogType getLogType() {
        return this.logType;
    }

    public void setLogType(LogType logType) {
        this.logType = logType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result)
                        + ((this.date == null) ? 0 : this.date.hashCode());
        result = (prime * result) + ((this.logLevel == null) ? 0
                        : this.logLevel.hashCode());
        result = (prime * result) + ((this.logType == null) ? 0
                        : this.logType.hashCode());
        result = (prime * result) + ((this.message == null) ? 0
                        : this.message.hashCode());
        result = (prime * result) + ((this.namespace == null) ? 0
                        : this.namespace.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        LogMessageImpl other = (LogMessageImpl) obj;
        if (this.date == null) {
            if (other.date != null) {
                return false;
            }
        }
        else if (!this.date.equals(other.date)) {
            return false;
        }
        if (this.logLevel != other.logLevel) {
            return false;
        }
        if (this.logType != other.logType) {
            return false;
        }
        if (this.message == null) {
            if (other.message != null) {
                return false;
            }
        }
        else if (!this.message.equals(other.message)) {
            return false;
        }
        if (this.namespace == null) {
            if (other.namespace != null) {
                return false;
            }
        }
        else if (!this.namespace.equals(other.namespace)) {
            return false;
        }
        return true;
    }

}

package com.phonepe.logger.util;

import com.phonepe.logger.LogMessage;
import com.phonepe.logger.LogType;

/**
 * Class containing utility functions to be used by framework. This is interface
 * because we do not want anyone to instantiate this.
 *
 * @author Kaustubh Khasnis
 */
public interface Utils {
    /**
     * Checks if given {@link String} is null or empty
     *
     * @param string
     *            {@link String} to be checked
     * @return true if its null or empty, false if its not. It trims the
     *         {@link String} before checking if its empty
     */
    public static boolean checkNullOrEmpty(String string) {
        if ((string == null) || string.trim().isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * Check if given {@link LogMessage} is a sink close message. i.e. it is of
     * {@link LogType#FRAMEWORK_LOG} and its {@link LogMessage#getMessage()}
     * returns {@link Constants#CLOSE_SINK_MESSAGE}
     *
     * @param logMessage
     *            {@link LogMessage} to be checked
     * @return true if its close message , false if its not.
     */
    public static boolean isCloseSinkMessage(LogMessage logMessage) {
        if ((logMessage.getLogType() == LogType.FRAMEWORK_LOG)
                        && logMessage.getMessage().equalsIgnoreCase(
                                        Constants.CLOSE_SINK_MESSAGE)) {
            return true;
        }
        return false;
    }
}

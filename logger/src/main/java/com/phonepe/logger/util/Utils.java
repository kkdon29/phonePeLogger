package com.phonepe.logger.util;

import com.phonepe.logger.LogMessage;
import com.phonepe.logger.LogType;

public class Utils {
    public static boolean checkNullOrEmpty(String string) {
        if ((string == null) || string.trim().isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean isCloseSinkMessage(LogMessage logMessage) {
        if ((logMessage.getLogType() == LogType.FRAMEWORK_LOG)
                        && logMessage.getMessage().equalsIgnoreCase(
                                        Constants.CLOSE_SINK_MESSAGE)) {
            return true;
        }
        return false;
    }
}

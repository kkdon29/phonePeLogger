package com.phonepe.logger;

import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestUtils {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter
                    .ofPattern("dd:MM:yy-HH:mm:ss:SSSS-z");;

    public static ParsedLogMessage parseLog(String logMessage) {
        String tokens[] = logMessage.split(" ");
        ParsedLogMessage parsedLogMessage = new ParsedLogMessage();
        parsedLogMessage.setLogLevel(LogLevel.valueOf(tokens[0]));
        parsedLogMessage.setTime(TestUtils.dateTimeFormatter.parse(tokens[1]));
        parsedLogMessage.setNamespace(tokens[2]);

        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(tokens[3]);
        int threadId = 0;
        while (matcher.find()) {
            threadId = Integer.parseInt(matcher.group());
        }
        parsedLogMessage.setThreadId(threadId);

        matcher = pattern.matcher(tokens[4]);
        int logId = 0;
        while (matcher.find()) {
            logId = Integer.parseInt(matcher.group());
        }
        parsedLogMessage.setLogId(logId);

        return parsedLogMessage;
    }

    public static String prepareMessageForLog(int logId, int threadId) {
        return "thread" + threadId + " test" + logId;
    }
}

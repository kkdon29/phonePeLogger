package com.phonepe.logger;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public interface LogReader {

    LogMessage getNextLog(long timeOut, TimeUnit unit)
                    throws InterruptedException, IOException;

}
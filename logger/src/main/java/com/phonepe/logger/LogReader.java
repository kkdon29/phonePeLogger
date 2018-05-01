package com.phonepe.logger;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.phonepe.logger.sink.Sink;

/**
 * Interface that can facilitate chaining of more than one {@link LogWriter}s.
 * e.g. You can use it in asynchronous {@link Sink} implementations where first
 * {@link LogWriter} will write to a buffer, then the {@link LogReader} can read
 * from buffer in another thread and write to underlying persistence.
 * {@link LogReader} is expected to maintain some kind of state/cursor
 * internally so that it does not provide duplicate log messages. Each
 * getNextLog must read next log message at the cursor and advance the cursor.
 * 
 * @author Kaustubh Khasnis
 */
public interface LogReader {

    /**
     * Retrieve next log message from underlying log store e.g. a buffer.
     *
     * @param timeOut
     *            Time for which to block on read.
     * @param unit
     *            {@link TimeUnit} for the timeOut
     * @return {@link LogMessage} retrieved from store. <code>null</code> in
     *         case no {@link LogMessage} available
     * @throws InterruptedException
     *             In case {@link LogReader} was interrupted while blocking for
     *             next {@link LogMessage}
     * @throws IOException
     *             In case there was an error reading from log store
     */
    LogMessage getNextLog(long timeOut, TimeUnit unit)
                    throws InterruptedException, IOException;

}
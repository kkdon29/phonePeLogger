package com.phonepe.logger.sink;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.phonepe.logger.LogLevel;

@RunWith(MockitoJUnitRunner.class)
public class SinkRepoTest {

    @Mock
    private Sink sink;

    @Test
    public void testRegisterSingleSink() {
        SinkRepo sinkRepo = new SinkRepo();
        sinkRepo.registerSink(LogLevel.INFO, this.sink);
        List<Sink> sinks = sinkRepo.getSinksForLogLevel(LogLevel.INFO);
        Assert.assertTrue(
                        "Invalid number of sinks registered for log level Info",
                        sinks.size() == 1);
        for (Sink sink : sinks) {
            Assert.assertEquals(this.sink, sink);
        }
    }

    @Test
    public void testRegisterMultipleSinksSameLevel() {
        SinkRepo sinkRepo = new SinkRepo();
        sinkRepo.registerSink(LogLevel.INFO, this.sink);
        sinkRepo.registerSink(LogLevel.INFO, this.sink);
        sinkRepo.registerSink(LogLevel.INFO, this.sink);

        List<Sink> sinks = sinkRepo.getSinksForLogLevel(LogLevel.INFO);
        Assert.assertTrue(
                        "Invalid number of sinks registered for log level Info",
                        sinks.size() == 3);
        for (Sink sink : sinks) {
            Assert.assertEquals(this.sink, sink);
        }
    }

    @Test
    public void testRegisterMultipleSinksDifferentLevel() {
        SinkRepo sinkRepo = new SinkRepo();
        sinkRepo.registerSink(LogLevel.INFO, this.sink);
        sinkRepo.registerSink(LogLevel.INFO, this.sink);
        sinkRepo.registerSink(LogLevel.DEBUG, this.sink);
        sinkRepo.registerSink(LogLevel.DEBUG, this.sink);

        List<Sink> sinks = sinkRepo.getSinksForLogLevel(LogLevel.INFO);
        Assert.assertTrue(
                        "Invalid number of sinks registered for log level Info",
                        sinks.size() == 2);
        for (Sink sink : sinks) {
            Assert.assertEquals(this.sink, sink);
        }
        sinks = sinkRepo.getSinksForLogLevel(LogLevel.DEBUG);
        Assert.assertTrue(
                        "Invalid number of sinks registered for log level Debug",
                        sinks.size() == 2);
        for (Sink sink : sinks) {
            Assert.assertEquals(this.sink, sink);
        }
    }

    @Test
    public void testGetSinksForUnregisteredLevel() {
        SinkRepo sinkRepo = new SinkRepo();
        List<Sink> sinks = sinkRepo.getSinksForLogLevel(LogLevel.INFO);
        Assert.assertTrue(
                        "Invalid number of sinks registered for log level Info",
                        sinks.size() == 0);

    }

    @Test
    public void testCloseSinks() {
        SinkRepo sinkRepo = new SinkRepo();
        sinkRepo.registerSink(LogLevel.INFO, this.sink);
        sinkRepo.registerSink(LogLevel.INFO, this.sink);
        sinkRepo.registerSink(LogLevel.DEBUG, this.sink);
        sinkRepo.registerSink(LogLevel.DEBUG, this.sink);

        sinkRepo.closeSinks();
        Mockito.verify(this.sink, Mockito.times(4)).close();

    }

}

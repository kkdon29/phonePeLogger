package com.phonepe.logger.sink;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.phonepe.logger.LogLevel;
import com.phonepe.logger.impl.LogMessageFactory;
import com.phonepe.logger.sink.config.SinkConfig;
import com.phonepe.logger.sink.config.SinkConfigReader;
import com.phonepe.logger.sink.config.impl.SinkConfigReaderProvider;

@RunWith(MockitoJUnitRunner.class)
public class SinkRepoFactoryTest {

    private SinkProviderRegistry sinkProviderRegistry = new SinkProviderRegistry();

    private SinkConfigReaderProvider sinkConfigReaderProvider = new SinkConfigReaderProvider();
    private LogMessageFactory        logMessageFactory        = new LogMessageFactory();
    @Mock
    private SinkConfigReader         sinkConfigReader;

    @Mock
    private SinkConfig sinkConfig;

    @Mock
    private SinkProvider sinkProvider;

    @Mock
    private Sink sink;

    @Before
    public void setUp() throws Exception {
        this.sinkProviderRegistry = Mockito.spy(this.sinkProviderRegistry);
        this.sinkConfigReaderProvider = Mockito
                        .spy(this.sinkConfigReaderProvider);
        Mockito.doReturn(this.sinkConfigReader)
                        .when(this.sinkConfigReaderProvider)
                        .createSinkConfigReader(
                                        ArgumentMatchers.any(Properties.class));

        Mockito.doReturn(Arrays.asList(this.sinkConfig, this.sinkConfig))
                        .when(this.sinkConfigReader).getConfigs();
        Mockito.doReturn(Arrays.asList(LogLevel.INFO)).when(this.sinkConfig)
                        .getLogLevels();
        Mockito.doReturn(this.sinkProvider).when(this.sinkProviderRegistry)
                        .getProvider(ArgumentMatchers.any());
        Mockito.doReturn(this.sink).when(this.sinkProvider).createSink(
                        ArgumentMatchers.any(SinkConfig.class),
                        ArgumentMatchers.any(LogMessageFactory.class));
    }

    @Test
    public void testCreateSinkRepo() {
        SinkRepoFactory sinkRepoFactory = new SinkRepoFactory();
        SinkRepo sinkRepo = sinkRepoFactory.createSinkRepo(new Properties(),
                        this.sinkProviderRegistry,
                        this.sinkConfigReaderProvider, this.logMessageFactory);
        List<Sink> sinks = sinkRepo.getSinksForLogLevel(LogLevel.INFO);
        Assert.assertTrue("Invalid number of sinks registered",
                        sinks.size() == 2);
        for (Sink sink : sinks) {
            Assert.assertEquals(this.sink, sink);
        }
    }

}

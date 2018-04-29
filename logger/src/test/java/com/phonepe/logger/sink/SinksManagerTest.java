package com.phonepe.logger.sink;

import java.util.Arrays;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.phonepe.logger.LogLevel;
import com.phonepe.logger.LogMessage;
import com.phonepe.logger.impl.LogMessageFactory;
import com.phonepe.logger.sink.config.impl.SinkConfigReaderProvider;

@RunWith(MockitoJUnitRunner.class)
public class SinksManagerTest {

    @Mock
    private SinkProviderRegistry        sinkProviderRegistry;
    @Mock
    private SinkRepo                    sinkRepo;
    @Mock
    private SinkProviderRegistryFactory sinkProviderRegistryFactory;
    @Mock
    private SinkRepoFactory             sinkRepoFactory;
    @Mock
    private SinkConfigReaderProvider    sinkConfigReaderProvider;
    @Mock
    private DynamicSinkProviderLoader   dynamicSinkProviderLoader;
    @Mock
    private LogMessageFactory           logMessageFactory;
    @Mock
    private LogMessage                  logMessage;

    @Mock
    private Sink sink;

    @Before
    public void setUp() throws Exception {
        Mockito.doReturn(this.sinkProviderRegistry)
                        .when(this.sinkProviderRegistryFactory)
                        .createSinkProviderRegistry(
                                        ArgumentMatchers.any(Properties.class),
                                        ArgumentMatchers.any(
                                                        DynamicSinkProviderLoader.class));
        Mockito.doReturn(this.sinkRepo).when(this.sinkRepoFactory)
                        .createSinkRepo(ArgumentMatchers.any(Properties.class),
                                        ArgumentMatchers.any(
                                                        SinkProviderRegistry.class),
                                        ArgumentMatchers.any(
                                                        SinkConfigReaderProvider.class),
                                        ArgumentMatchers.any(
                                                        LogMessageFactory.class));
        Mockito.doReturn(Arrays.asList(this.sink, this.sink))
                        .when(this.sinkRepo).getSinksForLogLevel(
                                        ArgumentMatchers.eq(LogLevel.INFO));
        Mockito.doReturn(LogLevel.INFO).when(this.logMessage).getLogLevel();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testLog() {
        SinksManager sinksManager = new SinksManager(new Properties(),
                        this.sinkProviderRegistryFactory, this.sinkRepoFactory,
                        this.sinkConfigReaderProvider,
                        this.dynamicSinkProviderLoader, this.logMessageFactory);
        sinksManager.log(this.logMessage);
        Mockito.verify(this.sink, Mockito.times(2))
                        .log(ArgumentMatchers.eq(this.logMessage));
    }

    @Test
    public void testClose() {
        SinksManager sinksManager = new SinksManager(new Properties(),
                        this.sinkProviderRegistryFactory, this.sinkRepoFactory,
                        this.sinkConfigReaderProvider,
                        this.dynamicSinkProviderLoader, this.logMessageFactory);
        sinksManager.close();
        Mockito.verify(this.sinkRepo, Mockito.times(1)).closeSinks();
    }

}

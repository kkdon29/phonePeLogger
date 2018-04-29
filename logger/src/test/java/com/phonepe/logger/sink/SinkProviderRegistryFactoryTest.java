package com.phonepe.logger.sink;

import java.util.Properties;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import com.phonepe.logger.sink.exception.SinkProviderAlreadyRegisteredException;
import com.phonepe.logger.sink.exception.SinkProviderRegistryConfigException;
import com.phonepe.logger.util.Constants;

@RunWith(MockitoJUnitRunner.class)
public class SinkProviderRegistryFactoryTest {

    @Spy
    private DynamicSinkProviderLoader dynamicSinkProviderLoader = new DynamicSinkProviderLoader();

    @Mock
    private SinkProvider sinkProvider;

    @Before
    public void setUp() {
        Mockito.doReturn(this.sinkProvider).when(this.dynamicSinkProviderLoader)
                        .loadSinkProviderbyClassName(
                                        ArgumentMatchers.anyString());
    }

    @Test
    public void testCreateSinkProviderRegistry() {
        SinkProviderRegistryFactory sinkProviderRegistryFactory = new SinkProviderRegistryFactory();
        Properties properties = new Properties();
        properties.put(Constants.SINK_PROVIDER_REGISTRY_FILE_KEY,
                        "src/test/resources/validSinkRegistry.properties");
        SinkProviderRegistry sinkProviderRegistry = sinkProviderRegistryFactory
                        .createSinkProviderRegistry(properties,
                                        this.dynamicSinkProviderLoader);
        SinkProvider sinkProvider = sinkProviderRegistry.getProvider("file");
        Assert.assertEquals(this.sinkProvider, sinkProvider);
        sinkProvider = sinkProviderRegistry.getProvider("console");
        Assert.assertEquals(this.sinkProvider, sinkProvider);
    }

    @Test(expected = SinkProviderRegistryConfigException.class)
    public void testCreateSinkProviderRegistryNullKey() {
        SinkProviderRegistryFactory sinkProviderRegistryFactory = new SinkProviderRegistryFactory();
        Properties properties = new Properties();
        sinkProviderRegistryFactory.createSinkProviderRegistry(properties,
                        this.dynamicSinkProviderLoader);
    }

    @Test(expected = SinkProviderRegistryConfigException.class)
    public void testCreateSinkProviderRegistryInvalidFileLocation() {
        SinkProviderRegistryFactory sinkProviderRegistryFactory = new SinkProviderRegistryFactory();
        Properties properties = new Properties();
        properties.put(Constants.SINK_PROVIDER_REGISTRY_FILE_KEY,
                        "src/test/resources/nonExistantSinkRegistry.properties");

        sinkProviderRegistryFactory.createSinkProviderRegistry(properties,
                        this.dynamicSinkProviderLoader);
    }

    @Test(expected = SinkProviderAlreadyRegisteredException.class)
    public void testCreateSinkProviderDuplicateSinksRegistry() {
        SinkProviderRegistryFactory sinkProviderRegistryFactory = new SinkProviderRegistryFactory();
        Properties properties = new Properties();
        properties.put(Constants.SINK_PROVIDER_REGISTRY_FILE_KEY,
                        "src/test/resources/duplicateSinksSinkRegistry.properties");
        sinkProviderRegistryFactory.createSinkProviderRegistry(properties,
                        this.dynamicSinkProviderLoader);

    }

}

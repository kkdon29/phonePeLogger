package com.phonepe.logger.sink;

import org.junit.Assert;
import org.junit.Test;

import com.phonepe.logger.sink.exception.SinkLoaderException;

public class DynamicSinkProviderLoaderTest {

    @Test
    public void testLoadSinkbyClassName() {
        DynamicSinkProviderLoader sinkLoader = new DynamicSinkProviderLoader();
        SinkProvider sinkProvider = sinkLoader.loadSinkProviderbyClassName(
                        SinkProviderImpl.class.getName());
        Assert.assertEquals(sinkProvider.getClass(), SinkProviderImpl.class);
    }

    @Test(expected = SinkLoaderException.class)
    public void testLoadSinkbyClassNameNonExistentClass() {
        DynamicSinkProviderLoader sinkLoader = new DynamicSinkProviderLoader();
        SinkProvider sink = sinkLoader
                        .loadSinkProviderbyClassName("class.doesnotExist");
        Assert.assertEquals(sink.getClass(), SinkProviderImpl.class);
    }

    @Test(expected = SinkLoaderException.class)
    public void testLoadSinkbyClassNameInvalidClass() {
        DynamicSinkProviderLoader sinkLoader = new DynamicSinkProviderLoader();
        SinkProvider sink = sinkLoader.loadSinkProviderbyClassName(
                        DynamicSinkProviderLoaderTest.class.getCanonicalName());
        Assert.assertEquals(sink.getClass(), SinkProviderImpl.class);
    }
}

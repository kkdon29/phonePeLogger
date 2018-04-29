package com.phonepe.logger.sink;

import java.lang.reflect.InvocationTargetException;

import com.phonepe.logger.sink.exception.SinkLoaderException;

public class DynamicSinkProviderLoader {
    public SinkProvider loadSinkProviderbyClassName(String className) {
        try {
            @SuppressWarnings("unchecked")
            Class<SinkProvider> sinkClass = (Class<SinkProvider>) Class
                            .forName(className);
            return sinkClass.getDeclaredConstructor().newInstance();
        }
        catch (ClassNotFoundException | InstantiationException
                        | IllegalAccessException | IllegalArgumentException
                        | InvocationTargetException | NoSuchMethodException
                        | SecurityException | ClassCastException ex) {
            throw new SinkLoaderException(className, ex);
        }
    }
}

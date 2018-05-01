package com.phonepe.logger.sink;

import java.lang.reflect.InvocationTargetException;

import com.phonepe.logger.sink.exception.SinkProviderLoaderException;

/**
 * Dynamic loader class to load {@link SinkProvider}s at run time provided in
 * {@link SinkProviderRegistry} using java reflection api.
 *
 * @author Kaustubh Khasnis
 */
public class DynamicSinkProviderLoader {
    /**
     * Create {@link SinkProvider} using reflection api using specified
     * className
     *
     * @param className
     *            Name of the class to be loaded
     * @return {@link SinkProvider} created
     * @throws SinkProviderLoaderException
     *             in case something goes wrong during loading
     */
    public SinkProvider loadSinkProviderbyClassName(String className)
                    throws SinkProviderLoaderException {
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
            throw new SinkProviderLoaderException(className, ex);
        }
    }
}

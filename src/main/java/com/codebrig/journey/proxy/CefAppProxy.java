package com.codebrig.journey.proxy;

import org.joor.Reflect;

/**
 * Journey local proxy for CefApp.
 * <p>
 * Javadoc taken from: https://bitbucket.org/chromiumembedded/java-cef
 *
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 * @version 0.2.17
 * @since 0.2.0
 */
@SuppressWarnings("unused")
public interface CefAppProxy extends Reflect.ProxyObject {

    Reflect.ProxyArgumentsConverter PROXY_ARGUMENTS_CONVERTER = (methodName, args) -> {
    };

    Reflect.ProxyValueConverter PROXY_VALUE_CONVERTER = (methodName, returnValue) -> {
        if ("createClient".equals(methodName)) {
            return Reflect.on(returnValue).as(CefClientProxy.class);
        }
        return returnValue;
    };

    /**
     * To shutdown the system, it's important to call the dispose
     * method. Calling this method closes all client instances with
     * and all browser instances each client owns. After that the
     * message loop is terminated and CEF is shutdown.
     */
    void dispose();

    /**
     * Creates a new client instance and returns it to the caller.
     * One client instance is responsible for one to many browser
     * instances
     *
     * @return a new client instance
     */
    CefClientProxy createClient();

    /**
     * Clear all registered scheme handler factories. Returns false on error. This
     * function may be called on any thread in the browser process.
     */
    boolean clearSchemeHandlerFactories();

    /**
     * Perform a single message loop iteration. Used on all platforms except
     * Windows with windowed rendering.
     */
    void doMessageLoopWork(long delayMs);
}

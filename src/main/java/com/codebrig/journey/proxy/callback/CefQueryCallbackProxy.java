package com.codebrig.journey.proxy.callback;

import org.joor.Reflect;

/**
 * Journey local proxy for CefQueryCallback.
 * <p>
 * Javadoc taken from: https://bitbucket.org/chromiumembedded/java-cef
 *
 * @author <a href="mailto:dhruvit.raithatha@gmail.com">Dhruvit Raithatha</a>
 * @version 0.4.0
 * @since 0.4.0
 */
public interface CefQueryCallbackProxy extends Reflect.ProxyObject {

    Reflect.ProxyArgumentsConverter PROXY_ARGUMENTS_CONVERTER = (methodName, args) -> {
    };

    Reflect.ProxyValueConverter PROXY_VALUE_CONVERTER = (methodName, returnValue) -> returnValue;

    /**
     * Notify the associated JavaScript onSuccess callback that the query has
     * completed successfully.
     *
     * @param response Response passed to JavaScript.
     */
    void success(String response);

    /**
     * Notify the associated JavaScript onFailure callback that the query has
     * failed.
     *
     * @param errorCode    Error code passed to JavaScript.
     * @param errorMessage Error message passed to JavaScript.
     */
    void failure(int errorCode, String errorMessage);
}

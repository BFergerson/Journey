package com.codebrig.journey.proxy.callback;

import org.joor.Reflect;

/**
 * Journey local proxy for CefQueryCallbackProxy.
 * <p>
 * Javadoc taken from: https://bitbucket.org/chromiumembedded/java-cef
 *
 * @author <a href="mailto:dhruvit.raithatha@gmail.com">Dhruvit Raithatha</a>
 * @version 0.3.4
 * @since 0.3.4
 */
public interface CefQueryCallbackProxy extends Reflect.ProxyObject {
    Reflect.ProxyArgumentsConverter PROXY_ARGUMENTS_CONVERTER = (methodName, args) -> {};

    Reflect.ProxyValueConverter PROXY_VALUE_CONVERTER = (methodName, returnValue) -> returnValue;
    /**
     * Notify the associated JavaScript onSuccess callback that the query has
     * completed successfully.
     * @param response Response passed to JavaScript.
     */
    public void success(String response);

    /**
     * Notify the associated JavaScript onFailure callback that the query has
     * failed.
     * @param error_code Error code passed to JavaScript.
     * @param error_message Error message passed to JavaScript.
     */
    public void failure(int error_code, String error_message);
}

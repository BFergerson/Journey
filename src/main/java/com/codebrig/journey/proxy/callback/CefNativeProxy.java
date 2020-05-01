package com.codebrig.journey.proxy.callback;

import org.joor.Reflect;

/**
 * Journey local proxy for CefNative.
 * <p>
 * Javadoc taken from: https://bitbucket.org/chromiumembedded/java-cef
 *
 * @author <a href="mailto:dhruvit.raithatha@gmail.com">Dhruvit Raithatha</a>
 * @version 0.4.1
 * @since 0.4.0
 */
public interface CefNativeProxy extends Reflect.ProxyObject {

    Reflect.ProxyArgumentsConverter PROXY_ARGUMENTS_CONVERTER = (methodName, args) -> {
    };

    Reflect.ProxyValueConverter PROXY_VALUE_CONVERTER = (methodName, returnValue) -> returnValue;

    /**
     * Method is called by the native code to store a reference
     * to an implemented native JNI counterpart.
     *
     * @param identifer The name of the interface class (e.g. CefFocusHandler).
     * @param nativeRef The reference to the native code.
     */
    void setNativeRef(String identifer, long nativeRef);

    /**
     * Method is called by the native code to get the reference
     * to an previous stored identifier.
     *
     * @param identifer The name of the interface class (e.g. CefFocusHandler).
     * @return The stored reference value of the native code.
     */
    long getNativeRef(String identifer);
}

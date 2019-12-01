package com.codebrig.journey.proxy.misc;

import org.joor.Reflect;

/**
 * Journey local proxy for BoolRef.
 * <p>
 * Javadoc taken from: https://bitbucket.org/chromiumembedded/java-cef
 *
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 * @version 0.4.0
 * @since 0.4.0
 */
public interface BoolRefProxy {

    Reflect.ProxyArgumentsConverter PROXY_ARGUMENTS_CONVERTER = (methodName, args) -> {
    };

    Reflect.ProxyValueConverter PROXY_VALUE_CONVERTER = (methodName, returnValue) -> returnValue;

    void set(boolean value);

    boolean get();
}

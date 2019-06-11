package com.codebrig.journey.proxy;

import org.joor.Reflect;

/**
 * Journey local proxy for CefApp.
 *
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 * @version 0.2.14
 * @since 0.2.0
 */
@SuppressWarnings("unused")
public interface CefAppProxy extends Reflect.ProxyObject {

    Reflect.ProxyValueConverter PROXY_VALUE_CONVERTER = (name, o) -> {
        if ("createClient".equals(name)) {
            return Reflect.on(o).as(CefClientProxy.class);
        }
        return o;
    };

    void dispose();

    CefClientProxy createClient();
}

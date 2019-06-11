package com.codebrig.journey.proxy;

import org.joor.Reflect;

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

package com.codebrig.journey.proxy.handler;

import java.util.HashMap;

/**
 * Journey local proxy for CefNativeDefault.
 * <p>
 * Javadoc taken from: https://bitbucket.org/chromiumembedded/java-cef
 *
 * @author <a href="mailto:dhruvit.raithatha@gmail.com">Dhruvit Raithatha</a>
 * @version 0.3.4
 * @since 0.3.4
 */
public class CefNativeDefault {
    private HashMap<String, Long> hmap = new HashMap<String, Long>();

    public void setNativeRef(String identifer, long nativeRef) {
        hmap.put(identifer, nativeRef);
    }

    public long getNativeRef(String identifer) {
        if (hmap.containsKey(identifer)) {
            return hmap.get(identifer).longValue();
        }
        return 0;
    }
}
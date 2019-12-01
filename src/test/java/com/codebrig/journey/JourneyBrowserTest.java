package com.codebrig.journey;

import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;

public class JourneyBrowserTest {

    @Test
    public void createBrowser() {
        JourneyBrowserView browser = new JourneyBrowserView("https://google.com");
        assertNotNull(browser.getCefBrowser());
    }
}

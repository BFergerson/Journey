package com.codebrig.journey;

import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.CefSettings;
import org.cef.browser.CefBrowser;

import javax.swing.*;
import java.awt.*;

/**
 * Wraps CefApp/CefClient/CefBrowser and extends JComponent for easy of implementation.
 *
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 * @version 0.1.1
 * @since 0.1.1
 */
public class JourneyBrowserView extends JComponent {

    public static final String ABOUT_BLANK = "about:blank";
    public static final CefSettings DEFAULT_SETTINGS = new CefSettings();

    static {
        DEFAULT_SETTINGS.windowless_rendering_enabled = false;
        DEFAULT_SETTINGS.log_file = String.format("%s/journey-%s/debug.log",
                System.getProperty("java.io.tmpdir"), JourneyConstants.VERSION);
    }

    private static CefSettings cefSettings;
    private static CefApp cefApp;
    private CefClient cefClient;
    private CefBrowser cefBrowser;

    public JourneyBrowserView() throws Exception {
        this(new String[0], DEFAULT_SETTINGS, ABOUT_BLANK);
    }

    public JourneyBrowserView(CefSettings cefSettings, String initialUrl) throws Exception {
        this(new String[0], cefSettings, initialUrl);
    }

    public JourneyBrowserView(String initialUrl) throws Exception {
        this(new String[0], DEFAULT_SETTINGS, initialUrl);
    }

    public JourneyBrowserView(String[] args, CefSettings cefSettings, String initialUrl) throws Exception {
        JourneyBrowserView.cefSettings = cefSettings;
        JourneyLoader.setup();

        setLayout(new BorderLayout());
        if (SwingUtilities.isEventDispatchThread()) {
            if (cefApp == null) cefApp = CefApp.getInstance(args, cefSettings);
            cefClient = cefApp.createClient();
            cefBrowser = cefClient.createBrowser(initialUrl, false, false);
            add(cefBrowser.getUIComponent(), "Center");
        } else {
            SwingUtilities.invokeAndWait(() -> {
                if (cefApp == null) cefApp = CefApp.getInstance(args, cefSettings);
                cefClient = cefApp.createClient();
                cefBrowser = cefClient.createBrowser(initialUrl, false, false);
                add(cefBrowser.getUIComponent(), "Center");
            });
        }
    }

    public CefSettings getCefSettings() {
        return cefSettings;
    }

    public CefApp getCefApp() {
        return cefApp;
    }

    public CefClient getCefClient() {
        return cefClient;
    }

    public CefBrowser getBrowser() {
        return cefBrowser;
    }
}

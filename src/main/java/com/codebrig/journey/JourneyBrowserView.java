package com.codebrig.journey;

import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.CefSettings;
import org.cef.OS;
import org.cef.browser.CefBrowser;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Wraps CefApp/CefClient/CefBrowser and extends JComponent for easy of implementation.
 *
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 * @version 0.2.0
 * @since 0.1.1
 */
public class JourneyBrowserView extends JComponent {

    public static final String ABOUT_BLANK = "about:blank";
    public static final CefSettings DEFAULT_SETTINGS = new CefSettings();

    static {
        DEFAULT_SETTINGS.windowless_rendering_enabled = false;
        DEFAULT_SETTINGS.log_file = String.format("%s/journey-%s/debug.log",
                System.getProperty("java.io.tmpdir"), JourneyLoader.VERSION);
    }

    private static CefSettings cefSettings;
    private static CefApp cefApp;
    private CefClient cefClient;
    private CefBrowser cefBrowser;

    public JourneyBrowserView() throws Exception {
        this(getDefaultCEFArgs(), DEFAULT_SETTINGS, ABOUT_BLANK);
    }

    public JourneyBrowserView(CefSettings cefSettings, String initialUrl) throws Exception {
        this(getDefaultCEFArgs(), cefSettings, initialUrl);
    }

    public JourneyBrowserView(String initialUrl) throws Exception {
        this(getDefaultCEFArgs(), DEFAULT_SETTINGS, initialUrl);
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

    private static String[] getDefaultCEFArgs() {
        if (OS.isMacintosh()) {
            File frameworkDirPath = new File(JourneyLoader.nativeDir,
                    "jcef_app.app/Contents/Frameworks/Chromium Embedded Framework.framework");
            File browserSubprocessPath = new File(JourneyLoader.nativeDir,
                    "jcef_app.app/Contents/Frameworks/jcef Helper.app/Contents/MacOS/jcef Helper");
            File resourcesDirPath = new File(JourneyLoader.nativeDir,
                    "jcef_app.app/Contents/Frameworks/Chromium Embedded Framework.framework/Resources");
            return new String[]{
                    "--framework-dir-path=" + frameworkDirPath.getAbsolutePath(),
                    "--browser-subprocess-path=" + browserSubprocessPath.getAbsolutePath(),
                    "--resources-dir-path=" + resourcesDirPath.getAbsolutePath(),
                    "--disable-gpu"
            };
        } else {
            return new String[0];
        }
    }
}

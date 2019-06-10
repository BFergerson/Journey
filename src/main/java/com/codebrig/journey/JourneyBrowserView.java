package com.codebrig.journey;

import com.codebrig.journey.proxy.CefAppProxy;
import com.codebrig.journey.proxy.CefBrowserProxy;
import com.codebrig.journey.proxy.CefClientProxy;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.lang.reflect.InvocationTargetException;

import static org.joor.Reflect.on;

/**
 * Wraps CefApp/CefClient/CefBrowser and extends JComponent for easy of implementation.
 *
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 * @version 0.2.9
 * @since 0.1.1
 */
@SuppressWarnings({"JavaReflectionInvocation", "WeakerAccess"})
public class JourneyBrowserView extends JComponent {

    public static final String ABOUT_BLANK = "about:blank";
    public static final JourneySettings DEFAULT_SETTINGS;

    static {
        JourneyLoader.setup();
        DEFAULT_SETTINGS = new JourneySettings();
        DEFAULT_SETTINGS.setWindowless_rendering_enabled(false);
        DEFAULT_SETTINGS.setLog_file(new File(JourneyLoader.NATIVE_DIRECTORY, "debug.log").getAbsolutePath());
    }

    private static JourneySettings journeySettings;
    private static CefAppProxy cefApp;
    private CefClientProxy cefClient;
    private CefBrowserProxy cefBrowser;

    public JourneyBrowserView() throws InvocationTargetException, InterruptedException {
        this(getDefaultCEFArguments(), DEFAULT_SETTINGS, ABOUT_BLANK);
    }

    public JourneyBrowserView(JourneySettings journeySettings, String initialUrl)
            throws InvocationTargetException, InterruptedException {
        this(getDefaultCEFArguments(), journeySettings, initialUrl);
    }

    public JourneyBrowserView(String initialUrl) throws InvocationTargetException, InterruptedException {
        this(getDefaultCEFArguments(), DEFAULT_SETTINGS, initialUrl);
    }

    public JourneyBrowserView(String[] args, JourneySettings journeySettings, String initialUrl)
            throws InvocationTargetException, InterruptedException {
        JourneyBrowserView.journeySettings = journeySettings;

        setLayout(new BorderLayout());
        if (SwingUtilities.isEventDispatchThread()) {
            if (cefApp == null) {
                Object realCefApp = on(JourneyLoader.getJourneyClassLoader().loadClass("org.cef.CefApp"))
                        .call("getInstance", args, journeySettings.asCefSettings()).get();
                cefApp = on(realCefApp).as(CefAppProxy.class, JourneyLoader.getJourneyClassLoader());
            }
            cefClient = cefApp.createClient();
            cefBrowser = cefClient.createBrowser(initialUrl, false, false);
            add(cefBrowser.getUIComponent(), "Center");
        } else {
            SwingUtilities.invokeAndWait(() -> {
                if (cefApp == null) {
                    Object realCefApp = on(JourneyLoader.getJourneyClassLoader().loadClass("org.cef.CefApp"))
                            .call("getInstance", args, journeySettings.asCefSettings()).get();
                    cefApp = on(realCefApp).as(CefAppProxy.class, JourneyLoader.getJourneyClassLoader());
                }
                cefClient = cefApp.createClient();
                cefBrowser = cefClient.createBrowser(initialUrl, false, false);
                add(cefBrowser.getUIComponent(), "Center");
            });
        }
    }

    public JourneySettings getJourneySettings() {
        return journeySettings;
    }

    public CefAppProxy getCefApp() {
        return cefApp;
    }

    public CefClientProxy getCefClient() {
        return cefClient;
    }

    public CefBrowserProxy getBrowser() {
        return cefBrowser;
    }

    private static String[] getDefaultCEFArguments() {
        if (System.getProperty("os.name").toLowerCase().startsWith("mac")) {
            File frameworkDirPath = new File(JourneyLoader.NATIVE_DIRECTORY,
                    "jcef_app.app/Contents/Frameworks/Chromium Embedded Framework.framework");
            File browserSubprocessPath = new File(JourneyLoader.NATIVE_DIRECTORY,
                    "jcef_app.app/Contents/Frameworks/jcef Helper.app/Contents/MacOS/jcef Helper");
            File resourcesDirPath = new File(JourneyLoader.NATIVE_DIRECTORY,
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

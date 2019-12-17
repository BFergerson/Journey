package com.codebrig.journey;

import org.joor.Reflect;

/**
 * Functions as a local proxy for CefSettings.
 * Note: This doesn't use a real proxy because CefSettings exposes configuration through fields instead of methods.
 *
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 * @version 0.4.0
 * @since 0.2.0
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class JourneySettings {

    public enum LogSeverity {
        LOGSEVERITY_DEFAULT,
        LOGSEVERITY_VERBOSE,
        LOGSEVERITY_INFO,
        LOGSEVERITY_WARNING,
        LOGSEVERITY_ERROR,
        LOGSEVERITY_DISABLE;
    }

    public static Class CEF_SETTINGS_CLASS = JourneyLoader.getJourneyClassLoader().loadClass("org.cef.CefSettings");
    private Object cefSettings;

    public JourneySettings() {
        try {
            cefSettings = CEF_SETTINGS_CLASS.newInstance();
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        }
    }

    public Object asCefSettings() {
        return cefSettings;
    }

    public String getBrowserSubprocessPath() {
        return Reflect.on(cefSettings).get("browser_subprocess_path");
    }

    public void setBrowserSubprocessPath(String browser_subprocess_path) {
        Reflect.on(cefSettings).set("browser_subprocess_path", browser_subprocess_path);
    }

    public boolean isWindowlessRenderingEnabled() {
        return Reflect.on(cefSettings).get("windowless_rendering_enabled");
    }

    public void setWindowlessRenderingEnabled(boolean windowless_rendering_enabled) {
        Reflect.on(cefSettings).set("windowless_rendering_enabled", windowless_rendering_enabled);
    }

    public boolean isCommandLineArgsDisabled() {
        return Reflect.on(cefSettings).get("command_line_args_disabled");
    }

    public void setCommandLineArgsDisabled(boolean command_line_args_disabled) {
        Reflect.on(cefSettings).set("command_line_args_disabled", command_line_args_disabled);
    }

    public String getCachePath() {
        return Reflect.on(cefSettings).get("cache_path");
    }

    public void setCachePath(String cache_path) {
        Reflect.on(cefSettings).set("cache_path", cache_path);
    }

    public boolean isPersistSessionCookies() {
        return Reflect.on(cefSettings).get("persist_session_cookies");
    }

    public void setPersistSessionCookies(boolean persist_session_cookies) {
        Reflect.on(cefSettings).set("persist_session_cookies", persist_session_cookies);
    }

    public String getUserAgent() {
        return Reflect.on(cefSettings).get("user_agent");
    }

    public void setUserAgent(String user_agent) {
        Reflect.on(cefSettings).set("user_agent", user_agent);
    }

    public String getProductVersion() {
        return Reflect.on(cefSettings).get("product_version");
    }

    public void setProductVersion(String product_version) {
        Reflect.on(cefSettings).set("product_version", product_version);
    }

    public String getLocale() {
        return Reflect.on(cefSettings).get("locale");
    }

    public void setLocale(String locale) {
        Reflect.on(cefSettings).set("locale", locale);
    }

    public String getLogFile() {
        return Reflect.on(cefSettings).get("log_file");
    }

    public void setLogFile(String log_file) {
        Reflect.on(cefSettings).set("log_file", log_file);
    }

    public LogSeverity getLogSeverity() {
        Object realLogSeverity = Reflect.on(cefSettings).get("log_severity");
        if (realLogSeverity == null) {
            return null;
        } else {
            return LogSeverity.valueOf(realLogSeverity.toString());
        }
    }

    public void setLogSeverity(LogSeverity log_severity) {
        Object realLogSeverity = Reflect.on("org.cef.CefSettings$LogSeverity", JourneyLoader.getJourneyClassLoader())
                .call("valueOf", log_severity.toString());
        Reflect.on(cefSettings).set("log_severity", realLogSeverity);
    }

    public String getJavascriptFlags() {
        return Reflect.on(cefSettings).get("javascript_flags");
    }

    public void setJavascriptFlags(String javascript_flags) {
        Reflect.on(cefSettings).set("javascript_flags", javascript_flags);
    }

    public String getResourcesDirPath() {
        return Reflect.on(cefSettings).get("resources_dir_path");
    }

    public void setResourcesDirPath(String resources_dir_path) {
        Reflect.on(cefSettings).set("resources_dir_path", resources_dir_path);
    }

    public String getLocalesDirPath() {
        return Reflect.on(cefSettings).get("locales_dir_path");
    }

    public void setLocalesDirPath(String locales_dir_path) {
        Reflect.on(cefSettings).set("locales_dir_path", locales_dir_path);
    }

    public boolean isPackLoadingDisabled() {
        return Reflect.on(cefSettings).get("pack_loading_disabled");
    }

    public void setPackLoadingDisabled(boolean pack_loading_disabled) {
        Reflect.on(cefSettings).set("pack_loading_disabled", pack_loading_disabled);
    }

    public int getRemoteDebuggingPort() {
        return Reflect.on(cefSettings).get("remote_debugging_port");
    }

    public void setRemoteDebuggingPort(int remote_debugging_port) {
        Reflect.on(cefSettings).set("remote_debugging_port", remote_debugging_port);
    }

    public int getUncaughtExceptionStackSize() {
        return Reflect.on(cefSettings).get("uncaught_exception_stack_size");
    }

    public void setUncaughtExceptionStackSize(int uncaught_exception_stack_size) {
        Reflect.on(cefSettings).set("uncaught_exception_stack_size", uncaught_exception_stack_size);
    }

    public boolean isIgnoreCertificateErrors() {
        return Reflect.on(cefSettings).get("ignore_certificate_errors");
    }

    public void setIgnoreCertificateErrors(boolean ignore_certificate_errors) {
        Reflect.on(cefSettings).set("ignore_certificate_errors", ignore_certificate_errors);
    }

//    public ColorType getBackground_color() {
//        Object realColorType = Reflect.on(cefSettings).get("background_color");
//        if (realColorType == null) {
//            return null;
//        } else {
//            ColorType colorType = new ColorType(0, 0, 0, 0);
//            Reflect.on(colorType).set("color_value", colorType.color_value);
//            return colorType;
//        }
//    }
//
//    public void setBackground_color(ColorType background_color) {
//        Object realColorType = Reflect.on("org.cef.CefSettings$ColorType", JourneyLoader.getJourneyClassLoader())
//                .create().get();
//        Reflect.on(realColorType).set("color_value", background_color.color_value);
//        Reflect.on(cefSettings).set("background_color", realColorType);
//    }

//    public static class ColorType {
//        private long color_value = 0L;
//
//        private ColorType() {
//        }
//
//        public ColorType(int var2, int var3, int var4, int var5) {
//            this.color_value = (long) (var2 << 24 | var3 << 16 | var4 << 8 | var5 << 0);
//        }
//
//        public long getColor() {
//            return this.color_value;
//        }
//
//        public JourneySettings.ColorType clone() {
//            JourneySettings.ColorType var1 = new JourneySettings.ColorType();
//            var1.color_value = this.color_value;
//            return var1;
//        }
//    }
}

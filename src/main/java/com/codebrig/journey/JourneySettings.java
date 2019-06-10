package com.codebrig.journey;

public class JourneySettings {

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

    private Object getFieldValue(String fieldName) {
        try {
            return CEF_SETTINGS_CLASS.getField(fieldName).get(cefSettings);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void setFieldValue(String fieldName, Object value) {
        try {
            CEF_SETTINGS_CLASS.getField(fieldName).set(cefSettings, value);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    //
//    public String getBrowser_subprocess_path() {
//        return (String) getFieldValue("browser_subprocess_path");
//    }
//
//    public void setBrowser_subprocess_path(String browser_subprocess_path) {
//        cefSettings.browser_subprocess_path = browser_subprocess_path;
//    }
//
//    public boolean isWindowless_rendering_enabled() {
//        return cefSettings.windowless_rendering_enabled;
//    }
//
    public void setWindowless_rendering_enabled(boolean windowless_rendering_enabled) {
        setFieldValue("windowless_rendering_enabled", windowless_rendering_enabled);
    }

    //
//    public boolean isCommand_line_args_disabled() {
//        return cefSettings.command_line_args_disabled;
//    }
//
//    public void setCommand_line_args_disabled(boolean command_line_args_disabled) {
//        cefSettings.command_line_args_disabled = command_line_args_disabled;
//    }
//
//    public String getCache_path() {
//        return cefSettings.cache_path;
//    }
//
//    public void setCache_path(String cache_path) {
//        cefSettings.cache_path = cache_path;
//    }
//
//    public boolean isPersist_session_cookies() {
//        return cefSettings.persist_session_cookies;
//    }
//
//    public void setPersist_session_cookies(boolean persist_session_cookies) {
//        cefSettings.persist_session_cookies = persist_session_cookies;
//    }
//
//    public String getUser_agent() {
//        return cefSettings.user_agent;
//    }
//
//    public void setUser_agent(String user_agent) {
//        cefSettings.user_agent = user_agent;
//    }
//
//    public String getProduct_version() {
//        return cefSettings.product_version;
//    }
//
//    public void setProduct_version(String product_version) {
//        cefSettings.product_version = product_version;
//    }
//
//    public String getLocale() {
//        return cefSettings.locale;
//    }
//
//    public void setLocale(String locale) {
//        cefSettings.locale = locale;
//    }
//
//    public String getLog_file() {
//        return cefSettings.log_file;
//    }
//
    public void setLog_file(String log_file) {
        setFieldValue("log_file", log_file);
    }
//
//    public CefSettings.LogSeverity getLog_severity() {
//        return cefSettings.log_severity;
//    }
//
//    public void setLog_severity(CefSettings.LogSeverity log_severity) {
//        cefSettings.log_severity = log_severity;
//    }
//
//    public String getJavascript_flags() {
//        return cefSettings.javascript_flags;
//    }
//
//    public void setJavascript_flags(String javascript_flags) {
//        cefSettings.javascript_flags = javascript_flags;
//    }
//
//    public String getResources_dir_path() {
//        return cefSettings.resources_dir_path;
//    }
//
//    public void setResources_dir_path(String resources_dir_path) {
//        cefSettings.resources_dir_path = resources_dir_path;
//    }
//
//    public String getLocales_dir_path() {
//        return cefSettings.locales_dir_path;
//    }
//
//    public void setLocales_dir_path(String locales_dir_path) {
//        cefSettings.locales_dir_path = locales_dir_path;
//    }
//
//    public boolean isPack_loading_disabled() {
//        return cefSettings.pack_loading_disabled;
//    }
//
//    public void setPack_loading_disabled(boolean pack_loading_disabled) {
//        cefSettings.pack_loading_disabled = pack_loading_disabled;
//    }
//
//    public int getRemote_debugging_port() {
//        return cefSettings.remote_debugging_port;
//    }
//
//    public void setRemote_debugging_port(int remote_debugging_port) {
//        cefSettings.remote_debugging_port = remote_debugging_port;
//    }
//
//    public int getUncaught_exception_stack_size() {
//        return cefSettings.uncaught_exception_stack_size;
//    }
//
//    public void setUncaught_exception_stack_size(int uncaught_exception_stack_size) {
//        cefSettings.uncaught_exception_stack_size = uncaught_exception_stack_size;
//    }
//
//    public boolean isIgnore_certificate_errors() {
//        return cefSettings.ignore_certificate_errors;
//    }
//
//    public void setIgnore_certificate_errors(boolean ignore_certificate_errors) {
//        cefSettings.ignore_certificate_errors = ignore_certificate_errors;
//    }
//
//    public CefSettings.ColorType getBackground_color() {
//        return cefSettings.background_color;
//    }
//
//    public void setBackground_color(CefSettings.ColorType background_color) {
//        cefSettings.background_color = background_color;
//    }
}

package com.codebrig.journey;

import java.util.ResourceBundle;

/**
 * Holds constants for the current build.
 *
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 * @version 0.1.1
 * @since 0.1.1
 */
public class JourneyConstants {

    private static final ResourceBundle BUILD = ResourceBundle.getBundle("journey_build");
    public static final String VERSION = BUILD.getString("version");
    public static final String JCEF_VERSION = BUILD.getString("jcef_version");
    public static final String BUILD_DATE = BUILD.getString("build_date");
    public static final String MODE = BUILD.getString("mode");
}

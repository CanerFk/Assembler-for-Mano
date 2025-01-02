package com.canerfk.mano.application.util;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
public class LanguageManager {
    private static final String BUNDLE_PATH = "com/canerfk/mano/application/languages/messages";
    private static ResourceBundle bundle;
    private static Locale currentLocale;

    public static void setLanguage(String language) {
        currentLocale = new Locale(language);
        bundle = ResourceBundle.getBundle(BUNDLE_PATH, currentLocale);
    }

    public static String getString(String key) {
        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            System.err.println("Missing resource key: " + key);
            return "!" + key + "!";
        }
    }

    public static Locale getCurrentLocale() {
        return currentLocale;
    }
}
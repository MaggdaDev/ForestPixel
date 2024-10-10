package org.maggdadev.forestpixel;

import java.util.prefs.Preferences;

public class FPPreferences {
    private static FPPreferences _instance;
    private final Preferences preferences = Preferences.userNodeForPackage(FPPreferences.class);

    public FPPreferences() {
        System.out.println(preferences.absolutePath());
    }

    public static void set(String key, String value) {
        getInstance().preferences.put(key, value);
    }

    public static String get(String key, String defaultValue) {
        return getInstance().preferences.get(key, defaultValue);
    }

    public static FPPreferences getInstance() {
        if (_instance == null) {
            _instance = new FPPreferences();
        }
        return _instance;
    }
}

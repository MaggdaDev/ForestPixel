package org.maggdadev.forestpixel.io;

public class Preferences {
    private static Preferences _instance;
    private final java.util.prefs.Preferences preferences = java.util.prefs.Preferences.userNodeForPackage(Preferences.class);

    public Preferences() {
        System.out.println(preferences.absolutePath());
    }

    public static void set(PreferenceKey key, String value) {
        getInstance().preferences.put(key.name(), value);
    }

    public static String get(PreferenceKey key, String defaultValue) {
        return getInstance().preferences.get(key.name(), defaultValue);
    }

    public static Preferences getInstance() {
        if (_instance == null) {
            _instance = new Preferences();
        }
        return _instance;
    }
}

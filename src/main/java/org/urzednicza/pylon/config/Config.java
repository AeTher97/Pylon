package org.urzednicza.pylon.config;


import java.util.ResourceBundle;

public class Config {

    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("config");

    public static String get(String key) {
        return resourceBundle.getString(key);
    }
}

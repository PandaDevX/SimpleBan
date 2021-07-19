package com.redspeaks.simpleban.lib;

public class TimeUtil {

    public static long calculate(String text) {
        if(text.endsWith("s")) {
            text = text.replace("s", "");
            return Long.parseLong(text);
        }
        if(text.endsWith("m")) {
            text = text.replace("m", "");
            return Long.parseLong(text) * 60;
        }
        if(text.endsWith("h")) {
            text = text.replace("h", "");
            return Long.parseLong(text) * 3600;
        }
        if(text.endsWith("d")) {
            text = text.replace("d", "");
            return Long.parseLong(text) * 86400;
        }
        if(text.endsWith("w")) {
            text = text.replace("w", "");
            return Long.parseLong(text) * 604800;
        }
        if(text.endsWith("mt")) {
            text = text.replace("mt", "");
            return Long.parseLong(text) * 2629743;
        }
        if(text.endsWith("y")) {
            text = text.replace("y", "");
            return Long.parseLong(text) * 31556926;
        }
        return Long.parseLong(text);
    }
}

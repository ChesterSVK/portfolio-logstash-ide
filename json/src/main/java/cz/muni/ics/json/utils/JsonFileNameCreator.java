package cz.muni.ics.json.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class JsonFileNameCreator {

    private JsonFileNameCreator() {
    }

    public static String getFormattedTimeJsonName() {
        return new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()) + ".json";
    }
}

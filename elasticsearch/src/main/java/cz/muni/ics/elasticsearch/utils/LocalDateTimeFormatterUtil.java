package cz.muni.ics.elasticsearch.utils;


import org.elasticsearch.common.inject.Singleton;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Singleton
@Component
public final class LocalDateTimeFormatterUtil {

    private LocalDateTimeFormatterUtil() {
    }

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss-SSS");

    public static String getNormalizedDate(LocalDateTime dateTime) {
        return dateTime.format(formatter);
    }

    public static LocalDateTime parseNormalizedDate(String date) {
        return LocalDateTime.parse(date, formatter);
    }
}

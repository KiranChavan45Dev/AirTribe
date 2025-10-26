package com.smart.system.parking.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class TimeUtil {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Returns current time as LocalDateTime.
     */
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    /**
     * Returns current time as formatted string.
     */
    public static String nowAsString() {
        return now().format(FORMATTER);
    }

    /**
     * Converts a LocalDateTime to formatted string.
     * @param dateTime the LocalDateTime to format
     * @return formatted date-time string, or null if input is null
     */
    public static String toString(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(FORMATTER);
    }
}

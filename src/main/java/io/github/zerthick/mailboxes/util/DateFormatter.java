package io.github.zerthick.mailboxes.util;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatter {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d yyyy hh:mm:ss a z");

    public static String formatDate(ZonedDateTime dateTime) {
        return dateTime.format(formatter);
    }
}

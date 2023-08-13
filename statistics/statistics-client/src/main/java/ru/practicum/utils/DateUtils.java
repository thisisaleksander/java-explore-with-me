package ru.practicum.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final LocalDateTime START = LocalDateTime.MIN;
    public static final LocalDateTime END = LocalDateTime.MAX;

    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}

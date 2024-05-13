package com.issuetracker.domain.common;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeToStringConverter {

    public static final long SECOND = 1000;
    public static final long MINUTE = 60 * SECOND;
    public static final long HOUR = 60 * MINUTE;
    public static final ZoneId SEOUL = ZoneId.of("Asia/Seoul");

    public static String convert(LocalDateTime source, LocalDateTime now) {
        long currentTime = now.atZone(SEOUL).toInstant().toEpochMilli();
        long sourceTime = source.atZone(SEOUL).toInstant().toEpochMilli();
        long difference = Math.abs(currentTime - sourceTime);

        String message;

        if(difference / SECOND < 60) {
            message = difference / SECOND + "초 전";
        } else if (difference / MINUTE < 60) {
            message = difference / MINUTE + "분 전";
        } else if (difference / HOUR < 24) {
            message = difference / HOUR + "시간 전";
        } else {
            message = DateTimeFormatter.ofPattern("yyyy년 M월 d일 HH:mm:ss").format(source);
        }

        return message;
    }
}

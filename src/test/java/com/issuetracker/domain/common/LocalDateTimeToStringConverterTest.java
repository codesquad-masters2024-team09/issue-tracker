package com.issuetracker.domain.common;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class LocalDateTimeToStringConverterTest {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy년 M월 d일 HH:mm:ss");
    public static final LocalDateTime NOW = LocalDateTime.parse("2024년 5월 13일 12:30:00", DATE_TIME_FORMATTER);

    @ParameterizedTest(name = "현재 2024년 5월 13일 12시 30분 0초 일 때 {0}초가 지난 시간은 \"{0}초 전\"으로 변환된다")
    @ValueSource(ints = {1, 2, 58, 59})
    void convert_minus_seconds(int minusSeconds) {
        // given
        LocalDateTime now = LocalDateTime.of(2024, 5, 13, 12, 30, 0);
        LocalDateTime source = now.minusSeconds(minusSeconds);

        // when
        String convertedTimeFormat = LocalDateTimeToStringConverter.convert(source, NOW);

        // then
        assertThat(convertedTimeFormat).isEqualTo(minusSeconds + "초 전");
    }

    @ParameterizedTest(name = "현재 2024년 5월 13일 12시 30분 0초 일 때 {0}분이 지난 시간은 \"{0}분 전\"으로 변환된다")
    @ValueSource(ints = {1, 2, 58, 59})
    void convert_minus_minutes(int minusMinutes) {
        // given
        LocalDateTime now = LocalDateTime.of(2024, 5, 13, 12, 30, 0);
        LocalDateTime source = now.minusMinutes(minusMinutes);

        // when
        String convertedTimeFormat = LocalDateTimeToStringConverter.convert(source, NOW);

        // then
        assertThat(convertedTimeFormat).isEqualTo(minusMinutes + "분 전");
    }

    @ParameterizedTest(name = "현재 2024년 5월 13일 12시 30분 0초 일 때 {0}시간이 지난 시간은 \"{0}시간 전\"으로 변환된다")
    @ValueSource(ints = {1, 2, 22, 23})
    void convert_minus_hours(int minusHours) {
        // given
        LocalDateTime now = LocalDateTime.of(2024, 5, 13, 12, 30, 0);
        LocalDateTime source = now.minusHours(minusHours);

        // when
        String convertedTimeFormat = LocalDateTimeToStringConverter.convert(source, NOW);

        // then
        assertThat(convertedTimeFormat).isEqualTo(minusHours + "시간 전");
    }

    @ParameterizedTest(name = "현재 2024년 5월 13일 12시 30분 0초 일 때 {0}일이 지난 시간은 \"yyyy년 M월 d일 HH:mm:ss\"으로 변환된다")
    @ValueSource(ints = {1, 2, 3})
    void convert_minus_date(int minusDays) {
        // given
        LocalDateTime source = NOW.minusDays(minusDays);

        // when
        String convertedTimeFormat = LocalDateTimeToStringConverter.convert(source, NOW);

        // then
        assertThat(convertedTimeFormat).isEqualTo(source.format(DATE_TIME_FORMATTER));
    }
}
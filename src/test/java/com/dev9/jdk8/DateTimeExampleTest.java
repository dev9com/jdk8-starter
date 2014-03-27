package com.dev9.jdk8;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class DateTimeExampleTest {

    @Test
    public void simpleTimeMath() {
        LocalDate today = LocalDate.now();

        long startDay = today.toEpochDay();

        LocalDate future = today.plusDays(2);

        assertThat(future.toEpochDay()).isEqualTo(startDay + 2);
    }

    @Test
    public void humanReadable() {
        LocalDate date = LocalDate.now();
        System.out.printf("%s-%s-%s",
                date.getYear(), date.getMonthValue(), date.getDayOfMonth()
        );
    }

    @Test
    public void standardClockInterface()
    {
        Clock utcClock = Clock.systemUTC();

        Clock defaultZoneClock = Clock.systemDefaultZone();;

        Clock mockClock = new Clock() {
            @Override
            public ZoneId getZone() {
                return null;
            }

            @Override
            public Clock withZone(ZoneId zone) {
                return null;
            }

            @Override
            public Instant instant() {
                return null;
            }
        };

        assertThat(utcClock.getZone().getId()).isNotEqualTo(defaultZoneClock.getZone().getId());

        assertThat(utcClock.instant()).isNotNull();

        assertThat(defaultZoneClock.instant()).isNotNull();

        assertThat(mockClock.instant()).isNull();

    }

}

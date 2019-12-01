package com.example.reservation.ui.statistics;

import com.example.reservation.Reservation;

import org.junit.Test;

import java.util.Calendar;
import java.util.Locale;

import static org.junit.Assert.*;

public class StatisticsFragmentTest {

    StatisticsFragment s = new StatisticsFragment();
    @Test
    public void checkInMonth() {
        Calendar today = Calendar.getInstance(Locale.KOREA);

        Reservation r = new Reservation();

        // 2019년 11월이 이번달인지 체크
        r.setYear(2019);
        r.setMonth(11);
        assertFalse(s.checkInMonth(r));

        // 2019년 12월이 이번달인지 체크
        r.setYear(2019);
        r.setMonth(12);
        assertTrue(s.checkInMonth(r));


        // 2019년 5월이 이번달인지 체크
        r.setYear(2019);
        r.setMonth(5);
        assertFalse(s.checkInMonth(r));

        // 2020년 1월이 이번달인지 체크
        r.setYear(2020);
        r.setMonth(1);
        assertFalse(s.checkInMonth(r));
    }
}
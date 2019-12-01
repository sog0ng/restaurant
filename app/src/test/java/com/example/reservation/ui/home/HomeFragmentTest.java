package com.example.reservation.ui.home;

import com.example.reservation.Reservation;

import org.junit.Test;

import java.util.Calendar;
import java.util.Locale;

import static org.junit.Assert.*;

public class HomeFragmentTest {

    HomeFragment h = new HomeFragment();
    @Test
    public void checkToday() {

        Calendar today = Calendar.getInstance(Locale.KOREA);


        // 2019년 11월 5일에 대해 테스트
        Reservation r = new Reservation();
        r.setYear(2019);
        r.setMonth(11);
        r.setDay(5);

        assertFalse(h.checkToday(r));

        // 오늘에 대해 테스트
        r.setYear(today.get(Calendar.YEAR));
        r.setMonth(today.get(Calendar.MONTH+1));
        r.setDay(today.get(Calendar.DATE));

        assertFalse(h.checkToday(r));

        // 2018년 5월 6일에 대해 테스트
        r.setYear(2018);
        r.setMonth(5);
        r.setDay(6);

        assertFalse(h.checkToday(r));


        // 2017년 3월 9일에 대해 테스트
        r.setYear(2017);
        r.setMonth(3);
        r.setDay(9);

        assertFalse(h.checkToday(r));

        // 2020년 3월 13일에 대해 테스트
        r.setYear(2020);
        r.setMonth(3);
        r.setDay(13);

        assertFalse(h.checkToday(r));

    }
}
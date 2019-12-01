package com.example.reservation.item;

import org.junit.Test;

import java.util.Calendar;
import java.util.Locale;

import static org.junit.Assert.*;

public class CustomerListViewAdapterTest {
    @Test
    public void countDday() {
        CustomerListViewAdapter customerListViewAdapter = new CustomerListViewAdapter();
        Calendar today = Calendar.getInstance(Locale.KOREA);

        // 현재 날짜와 비교
        int res = customerListViewAdapter.countDday(today.get(Calendar.YEAR), (today.get(Calendar.MONTH) + 1), today.get(Calendar.DATE));
        assertEquals(0, res);

        // 1일 뒤와 비교
        res = customerListViewAdapter.countDday(today.get(Calendar.YEAR), (today.get(Calendar.MONTH) + 1), today.get(Calendar.DATE) + 1);
        assertEquals(1, res);

        // 1일 전과 비교
        res = customerListViewAdapter.countDday(today.get(Calendar.YEAR), (today.get(Calendar.MONTH) + 1), today.get(Calendar.DATE) - 1);
        assertEquals(-1, res);

        // 5일 후와 비교
        res = customerListViewAdapter.countDday(today.get(Calendar.YEAR), (today.get(Calendar.MONTH) + 1), today.get(Calendar.DATE) + 5);
        assertEquals(5, res);
    }
}
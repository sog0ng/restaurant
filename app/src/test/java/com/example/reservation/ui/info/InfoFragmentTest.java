package com.example.reservation.ui.info;

import org.junit.Test;

import static org.junit.Assert.*;

public class InfoFragmentTest {

    InfoFragment infofragment = new InfoFragment();
    @Test
    public void confirmPassword() {
        //비밀번호 조건은 8자리~16자리
        assertFalse(infofragment.ConfirmPassword("asdf"));
        assertFalse(infofragment.ConfirmPassword("asdf123"));
        assertTrue(infofragment.ConfirmPassword("qwer1234"));
        assertTrue(infofragment.ConfirmPassword("123465789"));
    }

    @Test
    public void confirmPhonenum() {
        assertTrue(infofragment.ConfirmPhonenum("01012345678"));
        assertFalse(infofragment.ConfirmPhonenum("fdsafda"));   // 문자인 경우
        assertFalse(infofragment.ConfirmPhonenum("01012345678901234")); // 자리수가 11자리보다 많은 경우
        assertFalse(infofragment.ConfirmPhonenum("0101234"));   // 자리수가 11자리보다 적은 경우
    }
}
package com.example.reservation;

import org.junit.Test;

import static org.junit.Assert.*;

public class SignUpActivityTest {

    SignUpActivity a = new SignUpActivity();
    @Test
    public void confirmPassword() {
        //비밀번호 조건은 8자리~16자리
        assertFalse(a.ConfirmPassword("asdf"));
        assertFalse(a.ConfirmPassword("asdf123"));
        assertTrue(a.ConfirmPassword("qwer1234"));
        assertTrue(a.ConfirmPassword("123465789"));
    }

    @Test
    public void confirmPhonenum() {
        assertTrue(a.ConfirmPhonenum("01012345678"));
        assertFalse(a.ConfirmPhonenum("fdsafda"));   // 문자인 경우
        assertFalse(a.ConfirmPhonenum("01012345678901234")); // 자리수가 11자리보다 많은 경우
        assertFalse(a.ConfirmPhonenum("0101234"));   // 자리수가 11자리보다 적은 경우
    }
}
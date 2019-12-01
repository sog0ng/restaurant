package com.example.reservation;

import com.google.firebase.database.DataSnapshot;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MainActivityTest {

    MainActivity mainActivity = new MainActivity();

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        User u = mock(User.class);
        assertTrue(u != null);

        when(u.getId1()).thenReturn("admin");
        when(u.getPassword()).thenReturn("pw");

        assertTrue(mainActivity.userLoginCheck("admin", "pw", u));

        when(u.getId1()).thenReturn("qwer");
        when(u.getPassword()).thenReturn("qwer1234");

        assertTrue(mainActivity.userLoginCheck("qwer", "qwer1234", u));

        when(u.getId1()).thenReturn("asdf");
        when(u.getPassword()).thenReturn("12345678");

        assertTrue(mainActivity.userLoginCheck("asdf", "12345678", u));

        assertFalse(mainActivity.userLoginCheck("qqqqqq", "qqqqqqqqq", u));

        assertFalse(mainActivity.userLoginCheck("qwerty", "qwerty", u));
        /*
        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
        assertNotNull(a.getKeyCheck(childSnapshot));}*/
    }

}
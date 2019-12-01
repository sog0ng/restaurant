package com.example.reservation;

import com.google.firebase.database.DataSnapshot;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MainActivityTest {

    MainActivity a = new MainActivity();

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        assertNotNull(a.getMyRef1());
        /*
        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
        assertNotNull(a.getKeyCheck(childSnapshot));}*/
    }

}
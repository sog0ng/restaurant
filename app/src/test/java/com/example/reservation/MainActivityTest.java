package com.example.reservation;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MainActivityTest {
    FirebaseDatabase database;
    DatabaseReference myRef1;
    DataSnapshot dataSnapshot;
    DataSnapshot childSnapshot;

    MainActivity a = new MainActivity();
    User u = new User();
    String password;

    @Before
    public void setUp() {
        initDatabase();
        //u.setPassword("20000000");
        //password = u.getPassword();
    }

    @Test
    public void userLoginCheck() {

        assertFalse(a.userLoginCheck("ooo", password, u));
        assertTrue(a.userLoginCheck("test", "11111111", u));
        assertTrue(a.userLoginCheck("mimi", "11111111", u));
        assertFalse(a.userLoginCheck("aa", "11111111", u));
        assertFalse(a.userLoginCheck("check", "12345678", u));

    }

    @Test
    public void getValueCheck() {
        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
            assertNotNull(a.getValueCheck(childSnapshot));
        }
    }

    //    @Test
//    public void test() {
//        assertNotNull(a.getMyRef1());
//        /*
//        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
//        assertNotNull(a.getKeyCheck(childSnapshot));}*/
//    }
    private void initDatabase() {
        database = FirebaseDatabase.getInstance();
        myRef1 = database.getReference("User_info/");
        //childSnapshot = dataSnapshot.getChildren();

    }
}

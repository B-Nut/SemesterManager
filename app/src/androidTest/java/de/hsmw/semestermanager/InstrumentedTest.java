package de.hsmw.semestermanager;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class InstrumentedTest {
    @Test
    public void test() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        DatabaseHandler dh = new DatabaseHandler(appContext);
        DatabaseInterface di = new DatabaseInterface(dh.getWritableDatabase());
        int foobar = (int) di.insertDataPlans("Test", "2016.05.01", "2016.12.30");

        assertEquals(1, foobar);
    }
}

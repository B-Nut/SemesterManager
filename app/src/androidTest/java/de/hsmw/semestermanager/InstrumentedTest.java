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
        String name = "Test";
        String startDate = "2016-05-01";
        String endDate = "2016-12-30";
        Context appContext = InstrumentationRegistry.getTargetContext();
        DatabaseHandler dh = new DatabaseHandler(appContext);
        DatabaseInterface di = new DatabaseInterface(dh.getWritableDatabase());
        int foobar = (int) di.insertDataPlans(name, startDate, endDate);
        Plan plan = di.getDataByIDPlans(foobar);
        assertEquals(name, plan.getName());
        assertEquals(startDate, plan.getStartDate().toString());
        assertEquals(endDate, plan.getEndDate().toString());
    }
}

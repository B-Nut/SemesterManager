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
    public void semesterplanTest() throws Exception {
        // Context of the app under test.
        String name = "SemesterplanTest";
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

    @Test
    public void modulTest() throws Exception {
        // Context of the app under test.
        String name = "ModulTest";
        int planID = 1;
        Context appContext = InstrumentationRegistry.getTargetContext();
        DatabaseHandler dh = new DatabaseHandler(appContext);
        DatabaseInterface di = new DatabaseInterface(dh.getWritableDatabase());
        int foobar = (int) di.insertDataModules(name, planID);
        Module module = di.getDataByIDModules(foobar);
        assertEquals(name, module.getName());
        assertEquals(planID, module.getPlanID());
    }

    @Test
    public void terminTest() throws Exception {
        // Context of the app under test.
        String name = "TerminTest";
        String startDate = "2016-05-01";
        String endDate = "2016-12-30";
        String wiederholungsStart = startDate;
        String wiederholungsEnde = endDate;
        String startTime = "15:45:00";
        String endTime = "16:45:00";
        String ort = "testOrt";
        String typ = "Vorlesung";
        int prioritaet = 0;
        int planID = 1;
        int modulID = 1;
        int istGanztagsTermin = 0;
        String dozent = "Schubert";
        int periode = 0;
        Context appContext = InstrumentationRegistry.getTargetContext();
        DatabaseHandler dh = new DatabaseHandler(appContext);
        DatabaseInterface di = new DatabaseInterface(dh.getWritableDatabase());
        int foobar = (int) di.insertDataEntries( name, startDate, endDate, wiederholungsStart, wiederholungsEnde, startTime, endTime, ort, typ, prioritaet, planID, modulID, istGanztagsTermin, dozent, periode);
        Entry entry = di.getDataByIDEntries(foobar);
        assertEquals(name, entry.getName());
        assertEquals(startDate, entry.getStartDate().toString());
        assertEquals(endDate, entry.getEndDate().toString());
        assertEquals(wiederholungsStart, entry.getWiederholungsStart().toString());
        assertEquals(wiederholungsEnde, entry.getWiederholungsEnde().toString());
        assertEquals(startTime, entry.getStartTime().toString());
        assertEquals(endTime, entry.getEndTime().toString());
        assertEquals(ort, entry.getOrt());
        assertEquals(typ, entry.getTyp());
        assertEquals(prioritaet, entry.getPrioritaet());
        assertEquals(planID, entry.getPlanID());
        assertEquals(modulID, entry.getModulID());
        assertEquals(istGanztagsTermin, entry.getIstGanztagsTermin());
        assertEquals(dozent, entry.getDozent());
        assertEquals(periode, entry.getPeriode());
    }
}

package de.hsmw.semestermanager;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.sql.Date;
import java.sql.Time;

import static org.junit.Assert.*;

/**
 * Testklasse, bei der alle Tests erfolgreich sind, wenn sie mit einer leeren Datenbank der reihe
 * nach ausgeführt werden.
 */
@RunWith(AndroidJUnit4.class)
public class InstrumentedTest {
    @Test
    public void semesterplanTest() throws Exception {
        String name = "SemesterplanTest";
        String startDate = "2017-01-01";
        String endDate = "2017-06-30";
        Context appContext = InstrumentationRegistry.getTargetContext();
        DatabaseInterface di = DatabaseInterface.getInstance(appContext);
        int foobar = (int) di.insertDataPlans(name, startDate, endDate);
        Plan plan = di.getPlanByID(foobar);
        assertEquals(name, plan.getName());
        assertEquals(startDate, plan.getStartDate().toString());
        assertEquals(endDate, plan.getEndDate().toString());
    }

    @Test
    public void getAllPlansTest() {
        int anzP = 1;//zu erwartende Anzahl an Semesterplänen
        int bar;//tatsächliche Anzahl an Semesterplänen
        Context appContext = InstrumentationRegistry.getTargetContext();
        DatabaseInterface di = DatabaseInterface.getInstance(appContext);
        if (di.getAllPlans() == null) {
            bar = 0;
        } else {
            bar = di.getAllPlans().length;
        }
        assertEquals(anzP, bar);
    }

    @Test
    public void modulTest() throws Exception {
        String name = "ModulTest";
        int planID = 1;
        Context appContext = InstrumentationRegistry.getTargetContext();
        DatabaseInterface di = DatabaseInterface.getInstance(appContext);
        int foobar = (int) di.insertDataModules(name, planID);
        Module module = di.getModuleByID(foobar);
        assertEquals(name, module.getName());
        assertEquals(planID, module.getPlanID());
    }

    @Test
    public void getAllModulesTest() {
        int anzM = 1;//zu erwartende Anzahl an Modulen
        int bar;//tatsächliche Anzahl an Modulen
        Context appContext = InstrumentationRegistry.getTargetContext();
        DatabaseInterface di = DatabaseInterface.getInstance(appContext);
        if (di.getAllModules() == null) {
            bar = 0;
        } else {
            bar = di.getAllModules().length;
        }
        assertEquals(anzM, bar);
    }

    @Test
    public void getModulesByPlanIDTest() {
        int id = 1;
        int anzM = 1;//zu erwartende Anzahl an Modulen
        int bar;//tatsächliche Anzahl an Modulen
        Context appContext = InstrumentationRegistry.getTargetContext();
        DatabaseInterface di = DatabaseInterface.getInstance(appContext);
        if (di.getModulesByPlanID(id) == null) {
            bar = 0;
        } else {
            bar = di.getModulesByPlanID(id).length;
        }
        assertEquals(anzM, bar);

    }

    @Test
    public void terminTest() throws Exception {
        String name = "TestTermin";
        String startDate = "2017-01-02";
        String wiederholungsEnde = "2017-06-29";
        String startTime = "15:45:00";
        String endTime = "20:45:00";
        String ort = "testOrt";
        String typ = "Vorlesung";
        int prioritaet = 0;
        int planID = 1;
        int modulID = 1;
        int istGanztagsTermin = 0;
        String dozent = "Schubert";
        int periode = 7;
        int isExeption = 0;
        int exceptionContextID = 0;
        String exceptionTargetDay = "0001-01-01";
        int delete = 0;
        Context appContext = InstrumentationRegistry.getTargetContext();
        DatabaseInterface di = DatabaseInterface.getInstance(appContext);
        int foobar = (int) di.insertDataTermine(name, startDate, wiederholungsEnde, startTime, endTime, ort, typ, prioritaet, planID, modulID, istGanztagsTermin, dozent, periode, isExeption, exceptionContextID, exceptionTargetDay, delete);
        Termin termin = di.getTerminByID(foobar);
        assertEquals(name, termin.getName());
        assertEquals(startDate, termin.getStartDate().toString());
        assertEquals(wiederholungsEnde, termin.getWiederholungsEnde().toString());
        assertEquals(startTime, termin.getStartTime().toString());
        assertEquals(endTime, termin.getEndTime().toString());
        assertEquals(ort, termin.getOrt());
        assertEquals(typ, termin.getTyp());
        assertEquals(prioritaet, termin.getPrioritaet());
        assertEquals(planID, termin.getPlanID());
        assertEquals(modulID, termin.getModulID());
        assertEquals(istGanztagsTermin, termin.getIsGanztagsTermin());
        assertEquals(dozent, termin.getDozent());
        assertEquals(periode, termin.getPeriode());
    }

    @Test//dient nur zum hinzufügen von weiteren Terminen für spätere Tests
    public void addTermin() throws Exception {
        String name = "TerminExceptionTest";
        String startDate = "2017-01-10";
        String wiederholungsEnde = "2017-06-30";
        String startTime = "12:00:00";
        String endTime = "14:00:00";
        String ort = "testOrt";
        String typ = "Vorlesung";
        int prioritaet = 0;
        int planID = 1;
        int modulID = 1;
        int istGanztagsTermin = 0;
        String dozent = "Schubert";
        int periode = 7;
        int isExeption = 1;
        int exceptionContextID = 1;
        String exceptionTargetDay = "2017-01-23";
        int delete = 0;
        Context appContext = InstrumentationRegistry.getTargetContext();
        DatabaseInterface di = DatabaseInterface.getInstance(appContext);
        di.insertDataTermine(name, startDate, wiederholungsEnde, startTime, endTime, ort, typ, prioritaet, planID, modulID, istGanztagsTermin, dozent, periode, isExeption, exceptionContextID, exceptionTargetDay, delete);
        name = "Termin2Test";
        startDate = "2017-01-3";
        wiederholungsEnde = "2017-06-30";
        startTime = "15:00:00";
        endTime = "18:00:00";
        ort = "testOrt";
        typ = "Vorlesung";
        prioritaet = 0;
        planID = 1;
        modulID = 0;
        istGanztagsTermin = 0;
        dozent = "Schubert";
        periode = 7;
        isExeption = 0;
        exceptionContextID = 0;
        exceptionTargetDay = "0001-01-01";
        delete = 0;
        di.insertDataTermine(name, startDate, wiederholungsEnde, startTime, endTime, ort, typ, prioritaet, planID, modulID, istGanztagsTermin, dozent, periode, isExeption, exceptionContextID, exceptionTargetDay, delete);
    }

    @Test
    public void getAllTermineTest() {
        int anzT = 3;//zu erwartende Anzahl an Terminen
        int bar;//tatsächliche Anzahl an Terminen
        Context appContext = InstrumentationRegistry.getTargetContext();
        DatabaseInterface di = DatabaseInterface.getInstance(appContext);
        if (di.getAllTermine() == null) {
            bar = 0;
        } else {
            bar = di.getAllTermine().length;
        }
        assertEquals(anzT, bar);
    }

    @Test
    public void getTermineByPlanIDTest() {
        int id = 1;
        int anzT = 3;//zu erwartende Anzahl an Terminen
        int bar;//tatsächliche Anzahl an Terminen
        Context appContext = InstrumentationRegistry.getTargetContext();
        DatabaseInterface di = DatabaseInterface.getInstance(appContext);
        if (di.getTermineByPlanID(id) == null) {
            bar = 0;
        } else {
            bar = di.getTermineByPlanID(id).length;
        }
        assertEquals(anzT, bar);
    }

    @Test
    public void getTermineByModulIDTest() {
        int id = 1;
        int anzT = 2;//zu erwartende Anzahl an Terminen
        int bar;//tatsächliche Anzahl an Terminen
        Context appContext = InstrumentationRegistry.getTargetContext();
        DatabaseInterface di = DatabaseInterface.getInstance(appContext);
        if (di.getTermineByModulID(id) == null) {
            bar = 0;
        } else {
            bar = di.getTermineByModulID(id).length;
        }
        assertEquals(anzT, bar);
    }

    @Test
    public void getTermineByPlanIDButNotModulIDTest() {
        int id = 1;
        int anzT = 1;//zu erwartende Anzahl an Terminen
        int bar;//tatsächliche Anzahl an Terminen
        Context appContext = InstrumentationRegistry.getTargetContext();
        DatabaseInterface di = DatabaseInterface.getInstance(appContext);
        if (di.getTermineByPlanIDButNotModulID(id) == null) {
            bar = 0;
        } else {
            bar = di.getTermineByPlanIDButNotModulID(id).length;
        }
        assertEquals(anzT, bar);
    }

    @Test
    public void getTerminByDateTest() throws Exception {
        String date = "2017-01-10";//Datum der Termine
        int planOrModuleID = 1;//ID des zugehörigen Semesterplans/Moduls
        boolean isPlan = true;//true = Termine gehören zu einem Semesterplan, false = Termine gehören zu einem Modul
        int anzTermine = 2;//erwartete Anzahl an Terminen
        Context appContext = InstrumentationRegistry.getTargetContext();
        DatabaseInterface di = DatabaseInterface.getInstance(appContext);
        Termin[] foobar = di.getTermineByDate(date, planOrModuleID, isPlan);
        assertEquals(anzTermine, foobar.length);
    }


    @Test
    public void getCountExceptionsByIDTest() {
        int id = 1;
        long anzE = 1;//zu erwartende Anzahl an Exceptions
        Context appContext = InstrumentationRegistry.getTargetContext();
        DatabaseInterface di = DatabaseInterface.getInstance(appContext);
        assertEquals(anzE, di.getCountExceptionsByID(id));
    }

    @Test
    public void getExceptionsByWiederholungsIDTest() {
        int id = 1;
        int anzE = 1;//zu erwartende Anzahl an Exceptions
        int bar;//tatsächliche Anzahl an Exceptions
        Context appContext = InstrumentationRegistry.getTargetContext();
        DatabaseInterface di = DatabaseInterface.getInstance(appContext);
        if (null == di.getExceptionsByWiederholungsID(id)) {
            bar = 0;
        } else {
            bar = di.getExceptionsByWiederholungsID(id).length;
        }
        assertEquals(anzE, bar);

    }

    @Test
    public void getDurationSumUntilTest() throws Exception {
        String duration = "22:00";//zu erwartende Dauer
        String bar;//tatsächliche Dauer
        Date date = Date.valueOf("2017-02-01");//Datum der Termine
        Context appContext = InstrumentationRegistry.getTargetContext();
        DatabaseInterface di = DatabaseInterface.getInstance(appContext);
        Termin t = di.getTerminByID(1);
        long foo = t.getDurationSumUntil(date, appContext);
        bar = Helper.formatLong2HourString(foo);
        assertEquals(duration, bar);
    }

    @Test
    public void updateDataPlans() {
        int id = 1;
        String name = "SemesterplanUpdateTest";
        String startDate = "2017-03-01";
        String endDate = "2017-08-28";
        Context appContext = InstrumentationRegistry.getTargetContext();
        DatabaseInterface di = DatabaseInterface.getInstance(appContext);
        di.updateDataPlans(id, name, startDate, endDate);
        Plan plan = di.getPlanByID(id);
        assertEquals(name, plan.getName());
        assertEquals(startDate, plan.getStartDate().toString());
        assertEquals(endDate, plan.getEndDate().toString());
    }

    @Test
    public void updateDataModulesTest() {
        int id = 1;
        String name = "ModulUpdateTest";
        int planID = 1;
        Context appContext = InstrumentationRegistry.getTargetContext();
        DatabaseInterface di = DatabaseInterface.getInstance(appContext);
        int foobar = (int) di.updateDataModules(id, name, planID);
        Module module = di.getModuleByID(foobar);
        assertEquals(name, module.getName());
        assertEquals(planID, module.getPlanID());
    }

    @Test
    public void updateDataTermineTest() {
        int id = 1;
        String name = "TestUpdateTermin";
        String startDate = "2017-01-02";
        String wiederholungsEnde = "2017-11-01";
        String startTime = "15:45:00";
        String endTime = "20:45:00";
        String ort = "testOrt";
        String typ = "Vorlesung";
        int prioritaet = 0;
        int planID = 1;
        int modulID = 1;
        int istGanztagsTermin = 0;
        String dozent = "Schubert";
        int periode = 7;
        int isExeption = 0;
        int exceptionContextID = 0;
        String exceptionTargetDay = "0001-01-01";
        int delete = 0;
        Context appContext = InstrumentationRegistry.getTargetContext();
        DatabaseInterface di = DatabaseInterface.getInstance(appContext);
        int foobar = (int) di.updateDataTermine(id, name, startDate, wiederholungsEnde, startTime, endTime, ort, typ, prioritaet, planID, modulID, istGanztagsTermin, dozent, periode, isExeption, exceptionContextID, exceptionTargetDay, delete);
        Termin termin = di.getTerminByID(foobar);
        assertEquals(name, termin.getName());
        assertEquals(startDate, termin.getStartDate().toString());
        assertEquals(wiederholungsEnde, termin.getWiederholungsEnde().toString());
        assertEquals(startTime, termin.getStartTime().toString());
        assertEquals(endTime, termin.getEndTime().toString());
        assertEquals(ort, termin.getOrt());
        assertEquals(typ, termin.getTyp());
        assertEquals(prioritaet, termin.getPrioritaet());
        assertEquals(planID, termin.getPlanID());
        assertEquals(modulID, termin.getModulID());
        assertEquals(istGanztagsTermin, termin.getIsGanztagsTermin());
        assertEquals(dozent, termin.getDozent());
        assertEquals(periode, termin.getPeriode());
    }

    @Test
    public void deletePlanByIDTest() {
        int id = 1;
        Context appContext = InstrumentationRegistry.getTargetContext();
        DatabaseInterface di = DatabaseInterface.getInstance(appContext);

        di.deletePlanByID(id);
        assertEquals(null, di.getPlanByID(id));
    }

    @Test
    public void deleteModulByIDTest() {
        int id = 1;
        Context appContext = InstrumentationRegistry.getTargetContext();
        DatabaseInterface di = DatabaseInterface.getInstance(appContext);
        di.deleteModulByID(id);
        assertEquals(null, di.getModuleByID(id));
    }

    @Test
    public void deleteTerminByIDTest() {
        int id = 1;
        Context appContext = InstrumentationRegistry.getTargetContext();
        DatabaseInterface di = DatabaseInterface.getInstance(appContext);
        di.deleteTerminByID(id);
        assertEquals(null, di.getTerminByID(id));
    }
}

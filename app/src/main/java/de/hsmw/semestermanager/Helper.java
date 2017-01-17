package de.hsmw.semestermanager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Hilfsklasse mit - Erfahrungsgemäß - Hilfreichen Funktionen, die aber eigentlich woanders stehen sollten.
 * Created by Benjamin on 06.12.2016.
 */

public class Helper {
    /**
     * Erzeugt einen DatePicker, der nach der Auswahl das Ergebnis in die übergebene TextView schreibt.
     *
     * @param inputDate TextView, in das die Auswahl gespeichert werden soll.
     * @param title     String der als Titel für den Picker dient.
     */
    public static void pickDate(final Context context, final TextView inputDate, final String title) {
        inputDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mCurrentDate = Calendar.getInstance();
                int day = mCurrentDate.get(Calendar.DAY_OF_MONTH);
                int month = mCurrentDate.get(Calendar.MONTH);
                int year = mCurrentDate.get(Calendar.YEAR);
                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(context, 0, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        inputDate.setText(String.format("%02d", dayOfMonth) + "." + String.format("%02d", month + 1) + "." + year);
                    }
                }, year, month, day);
                mDatePicker.setTitle(title);
                mDatePicker.show();
            }
        });
    }

    /**
     * Erzeugt einen TimePicker, der nach der Auswahl das Ergebnis in die übergebene TextView schreibt.
     *
     * @param inputTime TextView, in das die Auswahl gespeichert werden soll.
     * @param title     String der als Titel für den Picker dient.
     */
    public static void pickTime(final Context context, final TextView inputTime, final String title) {
        inputTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        inputTime.setText(String.format("%02d", selectedHour) + ":" + String.format("%02d", selectedMinute));
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle(title);
                mTimePicker.show();

            }
        });
    }

    /**
     * @param germanDate Einen DateString im Format DD.MM.YYYY
     * @return Einen DateString im Format YYYY-MM-DD zur sicheren Verwendung von java.sql.Date.valueOf().
     * @throws IllegalArgumentException Wenn der gegebene String nicht zu einem gültigen SQL-String formatiert werden kann.
     */
    public static String dateToSQL(String germanDate) throws IllegalArgumentException {

        final SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        try {
            return df.parse(germanDate).toString();
        } catch (ParseException pe) {
            throw new IllegalArgumentException(pe.getMessage());
        }
    }

    /**
     * @param sqlDate Einen DateString im Format YYYY-MM-DD
     * @return Einen DateString im Format DD.MM.YYYY
     * @throws IllegalArgumentException Wenn der gegebene String nicht zu einem gültigen String formatiert werden kann.
     */
    public static String sqlToGermanDate(String sqlDate) throws IllegalArgumentException {
        final SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        try {
            return df.format(java.sql.Date.valueOf(sqlDate));
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /**
     * Quasi ein MiniSpinnerAdapter...
     */
    public static void fillSpinner(Context context, DatabaseObject[] inputArray, Spinner spinner) {
        List<String> inputNames = new ArrayList();
        for (DatabaseObject inputObject : inputArray) {
            inputNames.add(inputObject.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, inputNames);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    /**
     * Quasi ein MiniSpinnerAdapter...
     */
    public static void fillSpinner(Context context, String[] inputArray, Spinner spinner) {
        ArrayAdapter<String> adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, inputArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    /**
     * Hier als Ausnahme ist eine TimeFormat-Instanz nicht hilfreich gewesen.
     *
     * @param l Zeit in ms
     * @return Zeit im Format HH:MM wobei die Stunden zwischen einer und vielen Stellen variiert, die Minuten aber immer mit 2 Stellen angegeben ist.
     */
    public static String formatLong2HourString(long l) {
        String returnString = "";
        long hours = (l / 1000 / 60 / 60);
        long minutes = (l / 1000 / 60) - (hours * 60);
        DecimalFormat df = new DecimalFormat("00");
        returnString = hours + ":" + df.format(minutes);
        return returnString;
    }

    //Brauchen wir vielleicht noch. :)
    public static void showMessage(final Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Benjamin on 06.12.2016.
 */

public class Helper {
    public static void pickDate(final Context context, final TextView inputDate, final String title){
        inputDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mCurrentDate = Calendar.getInstance();
                int day = mCurrentDate.get(Calendar.DAY_OF_MONTH);
                int month = mCurrentDate.get(Calendar.MONTH);
                int year = mCurrentDate.get(Calendar.YEAR);
                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(context, 0, new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        inputDate.setText(String.format("%02d",dayOfMonth) + "." + String.format("%02d",month+1) + "." + year);
                    }
                }, year, month, day);
                mDatePicker.setTitle(title);
                mDatePicker.show();
            }
        });
    }
    public static void pickTime(final Context context, final TextView inputTime, final String title){
        inputTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        inputTime.setText( String.format("%02d", selectedHour) + ":" + String.format("%02d",selectedMinute));
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle(title);
                mTimePicker.show();

            }
        });
    }
    public static String dateToSQL(String germanDate){
        String[] values = germanDate.split("[.]");
        return values[2] + "-" + values[1] + "-" + values[0];
    }
    public static String sqlToGermanDate(String sqlDate){
        String[] vaiues = sqlDate.split("-");
        return vaiues[2] + "." + vaiues[1] + "." + vaiues[0];
    }

    //Brauchen wir vielleicht noch. :)
    public void showMessage(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
    public static void fillSpinner(Context context, DatabaseObject[] inputArray, Spinner spinner) {
        List<String> inputNames = new ArrayList();
        for (DatabaseObject inputObject : inputArray) {
            inputNames.add(inputObject.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, inputNames);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
    public static void fillSpinner(Context context, String[] inputArray, Spinner spinner) {
        ArrayAdapter<String> adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, inputArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}


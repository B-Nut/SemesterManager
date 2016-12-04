package de.hsmw.semestermanager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import java.util.Calendar;

public class SemesterplanInput extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semesterplan_input);

        pickDate((TextView) findViewById(R.id.new_semester_date_start));
        pickDate((TextView) findViewById(R.id.new_semester_date_end));

        findViewById(R.id.new_semester_submit).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DatabaseHandler dh = new DatabaseHandler(SemesterplanInput.this);
                DatabaseInterface di = new DatabaseInterface(dh.getWritableDatabase());
                String name = ((EditText) findViewById(R.id.new_semester_name)).getText().toString();
                String startDate = dateToSQL(((TextView) findViewById(R.id.new_semester_date_start)).getText().toString());
                String endDate = dateToSQL(((TextView) findViewById(R.id.new_semester_date_end)).getText().toString());
                di.insertDataPlans(name,startDate,endDate);

                finish();
            }
        });
    }
    private void pickDate(final TextView inputDate){
        inputDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mCurrentDate = Calendar.getInstance();
                int day = mCurrentDate.get(Calendar.DAY_OF_MONTH);
                int month = mCurrentDate.get(Calendar.MONTH);
                int year = mCurrentDate.get(Calendar.YEAR);
                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(SemesterplanInput.this, 0, new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        inputDate.setText(String.format("%02d",dayOfMonth) + "." + String.format("%02d",month+1) + "." + year);
                    }
                }, year, month, day);
                mDatePicker.setTitle("Select Date");
                mDatePicker.show();
            }
        });
    }
    private String dateToSQL(String germanDate){
        String[] values = germanDate.split("[.]");
        return values[2] + "-" + values[1] + "-" + values[0];
    }

    private void pickTime(final TextView inputTime){
        inputTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(SemesterplanInput.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        inputTime.setText( String.format("%02d", selectedHour) + ":" + String.format("%02d",selectedMinute));
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
    }
}

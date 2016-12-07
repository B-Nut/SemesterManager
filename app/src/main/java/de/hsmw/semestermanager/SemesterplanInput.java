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

        Helper.pickDate(this, (TextView) findViewById(R.id.new_semester_date_start), "Wähle Semesterbeginn");
        Helper.pickDate(this, (TextView) findViewById(R.id.new_semester_date_end), "Wähle Semesterende");

        findViewById(R.id.new_semester_submit).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DatabaseHandler dh = new DatabaseHandler(SemesterplanInput.this);
                DatabaseInterface di = new DatabaseInterface(dh.getWritableDatabase());
                String name = ((EditText) findViewById(R.id.new_semester_name)).getText().toString();
                String startDate = Helper.dateToSQL(((TextView) findViewById(R.id.new_semester_date_start)).getText().toString());
                String endDate = Helper.dateToSQL(((TextView) findViewById(R.id.new_semester_date_end)).getText().toString());
                di.insertDataPlans(name,startDate,endDate);

                finish();
            }
        });
    }
}

package de.hsmw.semestermanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SemesterplanInput extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semesterplan_input);

        Helper.pickDate(this, (TextView) findViewById(R.id.new_semester_date_start), "Wähle Semesterbeginn");
        Helper.pickDate(this, (TextView) findViewById(R.id.new_semester_date_end), "Wähle Semesterende");

        findViewById(R.id.new_semester_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseInterface di = DatabaseInterface.getInstance(SemesterplanInput.this);
                String name = ((EditText) findViewById(R.id.new_semester_name)).getText().toString();
                String startDate = Helper.dateToSQL(((TextView) findViewById(R.id.new_semester_date_start)).getText().toString());
                String endDate = Helper.dateToSQL(((TextView) findViewById(R.id.new_semester_date_end)).getText().toString());
                di.insertDataPlans(name, startDate, endDate);

                finish();
            }
        });
    }
}

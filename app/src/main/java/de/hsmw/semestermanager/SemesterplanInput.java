package de.hsmw.semestermanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SemesterplanInput extends AppCompatActivity {

    EditText inputName;
    TextView inputStartDate;
    TextView inputEndDate;
    Button b;
    DatabaseInterface di;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semesterplan_input);
        b = (Button) findViewById(R.id.new_semester_submit);
        inputName = (EditText) findViewById(R.id.new_semester_name);
        inputStartDate = (TextView) findViewById(R.id.new_semester_date_start);
        inputEndDate = (TextView) findViewById(R.id.new_semester_date_end);

        Helper.pickDate(this, inputStartDate, "Wähle Semesterbeginn");
        Helper.pickDate(this, inputEndDate, "Wähle Semesterende");

        int editID = -1;
        if(getIntent().getExtras() != null) {
            editID = getIntent().getExtras().getInt("ID", -1);
        }
        if (editID == -1) {
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseInterface di = DatabaseInterface.getInstance(SemesterplanInput.this);
                    di.insertDataPlans(inputName.getText().toString(), Helper.dateToSQL(inputStartDate.getText().toString()), Helper.dateToSQL(inputEndDate.getText().toString()));
                    finish();
                }
            });
        } else {
            di = DatabaseInterface.getInstance(this);
            final Plan editPlan = di.getPlanByID(editID);

            inputName.setText(editPlan.getName());
            inputStartDate.setText(Helper.sqlToGermanDate(editPlan.getStartDate().toString()));
            inputEndDate.setText(Helper.sqlToGermanDate(editPlan.getEndDate().toString()));

            setTitle("Bearbeite " + editPlan.getName());

            b.setText("Änderungen schreiben");
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    di.updateDataPlans(editPlan.getId(), inputName.getText().toString(), Helper.dateToSQL(inputStartDate.getText().toString()), Helper.dateToSQL(inputEndDate.getText().toString()));
                    finish();
                }
            });
        }
    }
}

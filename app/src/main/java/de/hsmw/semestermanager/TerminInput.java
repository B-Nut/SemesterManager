package de.hsmw.semestermanager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by Benjamin on 06.12.2016.
 */

public class TerminInput extends AppCompatActivity {
    DatabaseHandler dh;
    DatabaseInterface di;

    EditText terminName, dozent, ort;
    CheckBox ganztagstermin;
    TextView startDate, endDate, startTime, endTime;
    Spinner periode, semester, modul, typ, priorität;
    Button submit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termin_input);

        dh = new DatabaseHandler(this);
        di = new DatabaseInterface(dh.getWritableDatabase());

        terminName = (EditText) findViewById(R.id.new_termin_name);
        ganztagstermin = (CheckBox) findViewById(R.id.new_termin_ganztags_checkbox);
        startDate = (TextView) findViewById(R.id.new_termin_date_start);
        endDate = (TextView) findViewById(R.id.new_termin_date_end);
        startTime = (TextView) findViewById(R.id.new_termin_time_start);
        endTime = (TextView) findViewById(R.id.new_termin_time_end);
        periode = (Spinner) findViewById(R.id.new_termin_periodisch);
        semester = (Spinner) findViewById(R.id.new_termin_semesterzugehörigkeit);
        modul = (Spinner) findViewById(R.id.new_termin_modulauswahl);
        typ = (Spinner) findViewById(R.id.new_termin_termintypauswahl);
        priorität = (Spinner) findViewById(R.id.new_termin_priorityauswahl);
        dozent = (EditText) findViewById(R.id.new_termin_dozent);
        ort = (EditText) findViewById(R.id.new_termin_ort);
        submit = (Button) findViewById(R.id.new_termin_submit);

        Helper.pickDate(this, startDate, "Wähle Beginn der Terminwiederholung");
        Helper.pickDate(this, endDate, "Wähle Ende der Terminwiederholung");
        Helper.pickTime(this, startTime, "Wähle Beginn des Termins");
        Helper.pickTime(this, endTime, "Wähle Ende des Termins");

        String[] perioden = {"Wöchentlich", "Zweiwöchentlich"};
        Helper.fillSpinner(this, perioden, periode);

        final String[] typen = {"Vorlesung", "Seminar"};
        Helper.fillSpinner(this, typen, typ);

        final String[] prioritäten = {"Normal", "Niedrig"};
        Helper.fillSpinner(this, prioritäten, priorität);

        final Plan[] allPlans = di.getAllPlans();
        Helper.fillSpinner(this, allPlans, semester);

        Module[] allModules = di.getAllDataModules();
        Helper.fillSpinner(this, allModules, modul);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                di.insertDataEntries(terminName.getText().toString(),Helper.dateToSQL(startDate.getText().toString()),Helper.dateToSQL(endDate.getText().toString()),Helper.dateToSQL(startDate.getText().toString()),Helper.dateToSQL(endDate.getText().toString()),startTime.getText().toString()+":00",endTime.getText().toString()+":00",ort.getText().toString(),typen[typ.getSelectedItemPosition()],priorität.getSelectedItemPosition(),semester.getSelectedItemPosition()+1,modul.getSelectedItemPosition()+1,ganztagstermin.isChecked()?1:0,dozent.getText().toString(),periode.getSelectedItemPosition());
                finish();
            }
        });
    }
}

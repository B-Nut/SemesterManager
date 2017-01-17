package de.hsmw.semestermanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ExceptionInput extends AppCompatActivity {
    DatabaseInterface di;

    EditText terminName, dozent, ort;
    CheckBox deleteTermin, ganztagsTermin;
    TextView startDate, startTime, endTime;
    Spinner typ, priorität;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_exception_input);

        di = DatabaseInterface.getInstance(this);


        terminName = (EditText) findViewById(R.id.new_exception_name);
        deleteTermin = (CheckBox) findViewById(R.id.new_exception_delete_checkbox);
        ganztagsTermin = (CheckBox) findViewById(R.id.new_exception_ganztags_checkbox);
        startDate = (TextView) findViewById(R.id.new_exception_date_start);
        startTime = (TextView) findViewById(R.id.new_exception_time_start);
        endTime = (TextView) findViewById(R.id.new_exception_time_end);
        typ = (Spinner) findViewById(R.id.new_exception_termintypauswahl);
        priorität = (Spinner) findViewById(R.id.new_exception_priorityauswahl);
        dozent = (EditText) findViewById(R.id.new_exception_dozent);
        ort = (EditText) findViewById(R.id.new_exception_ort);
        submit = (Button) findViewById(R.id.new_exception_submit);

        final String[] typen = {"Kein Typ", "Vorlesung", "Seminar", "Praktikum", "Meeting", "Privat"};
        Helper.fillSpinner(this, typen, typ);

        final String[] prioritäten = {"Hoch", "Normal", "Niedrig"};
        Helper.fillSpinner(this, prioritäten, priorität);
        priorität.setSelection(1);

        Helper.pickDate(this, startDate, "Wähle Beginn der Terminwiederholung");
        Helper.pickTime(this, startTime, "Wähle Beginn des Termins");
        Helper.pickTime(this, endTime, "Wähle Ende des Termins");

        final Bundle x = getIntent().getExtras();
        terminName.setText(x.getString("terminName", ""));
        startDate.setText(x.getString("startDate", "Wähle Datum"));
        startTime.setText(x.getString("startZeit", "Wähle StartZeit"));
        endTime.setText(x.getString("endZeit", "Wähle EndZeit"));
        ort.setText(x.getString("ort", "Schreibe Ort"));
        int selection = 0;
        switch (x.getString("typ")) {
            case "Kein Typ":
                selection = 0;
                break;
            case "Vorlesung":
                selection = 1;
                break;
            case "Seminar":
                selection = 2;
                break;
            case "Praktikum":
                selection = 3;
                break;
            case "Meeting":
                selection = 4;
                break;
            case "Privat":
                selection = 5;
                break;
            default:
                selection = 0;
                break;
        }
        typ.setSelection(selection);
        priorität.setSelection(x.getInt("priorität", 0));

        final int planID = x.getInt("planID", 0);
        final int modulID = x.getInt("modulID", 0);
        final int periode = x.getInt("periode", 0);

        ganztagsTermin.setChecked(x.getInt("isGanztagsTermin") != 0);
        dozent.setText(x.getString("dozent", ""));

        final int editID = getIntent().getExtras().getInt("ID", -1);
        if (editID == -1) {
            setTitle("Statt " + terminName.getText().toString() + " am " + x.getString("targetDay"));
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        di.insertDataTermine(terminName.getText().toString(), Helper.dateToSQL(startDate.getText().toString()), Helper.dateToSQL(startDate.getText().toString()), startTime.getText().toString() + ":00", endTime.getText().toString() + ":00", ort.getText().toString(), typen[typ.getSelectedItemPosition()], priorität.getSelectedItemPosition(), planID, modulID, ganztagsTermin.isChecked() ? 1 : 0, dozent.getText().toString(), periode, 1, x.getInt("exceptionContextID"), Helper.dateToSQL(x.getString("targetDay")), deleteTermin.isChecked() ? 1 : 0);
                    } catch (IllegalArgumentException iae) {
                        Toast.makeText(getApplicationContext(), iae.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    finish();
                }
            });
        } else {
            setTitle("Statt " + di.getTerminByID(x.getInt("exceptionContextID")).getName() + " am " + x.getString("targetDay"));
            deleteTermin.setChecked(x.getInt("isDeleted") != 0);
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        di.updateDataTermine(editID, terminName.getText().toString(), Helper.dateToSQL(startDate.getText().toString()), Helper.dateToSQL(startDate.getText().toString()), startTime.getText().toString() + ":00", endTime.getText().toString() + ":00", ort.getText().toString(), typen[typ.getSelectedItemPosition()], priorität.getSelectedItemPosition(), planID, modulID, ganztagsTermin.isChecked() ? 1 : 0, dozent.getText().toString(), periode, 1, x.getInt("exceptionContextID"), Helper.dateToSQL(x.getString("targetDay")), deleteTermin.isChecked() ? 1 : 0);
                    } catch (IllegalArgumentException iae) {
                        Toast.makeText(getApplicationContext(), iae.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    finish();
                }
            });
        }
    }

}

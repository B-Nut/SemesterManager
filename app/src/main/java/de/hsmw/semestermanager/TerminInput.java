package de.hsmw.semestermanager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Benjamin on 06.12.2016.
 */

public class TerminInput extends AppCompatActivity {
    DatabaseInterface di;

    EditText terminName, dozent, ort;
    CheckBox ganztagstermin;
    TextView startDate, endDate, startTime, endTime;
    Spinner periode, semester, modul, typ, priorität;
    Button submit;

    Module[] currentModuleSelection;
    Plan[] currentSemesterSelection;

    /**
     * Der Modulspinner ist dahingehend besonders, weil dieser anhand des Semesters aktualisiert werden muss.
     * Diese Funktion vereinfacht es den Spinner zu aktualisieren, zu wissen was gerade drinsteht und die Auswahl "Kein Modul" konsistent in der Auswahl zu lassen.
     *
     * @param m Liste der Module (außer "Kein Modul") die in den Spinner geladen werden soll.
     */
    private void refreshModuleSpinner(Module[] m) {
        ArrayList<Module> am = new ArrayList<>();
        am.add(new Module(0, "Kein Modul", 0));
        if (m != null) {
            am.addAll(Arrays.asList(m));
        }
        currentModuleSelection = am.toArray(new Module[am.size()]);
        Helper.fillSpinner(this, currentModuleSelection, modul);
    }

    private void refreshSemesterSpinner(Plan[] m) {
        ArrayList<Plan> am = new ArrayList<>();
        if (m != null) {
            am.addAll(Arrays.asList(m));
        }
        currentSemesterSelection = am.toArray(new Plan[am.size()]);
        Helper.fillSpinner(this, currentSemesterSelection, semester);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termin_input);

        di = DatabaseInterface.getInstance(this);

        terminName = (EditText) findViewById(R.id.new_termin_name);
        ganztagstermin = (CheckBox) findViewById(R.id.new_termin_ganztags_checkbox);
        startDate = (TextView) findViewById(R.id.new_termin_date_start);
        endDate = (TextView) findViewById(R.id.new_termin_date_end);
        startTime = (TextView) findViewById(R.id.new_termin_time_start);
        endTime = (TextView) findViewById(R.id.new_termin_time_end);
        periode = (Spinner) findViewById(R.id.new_termin_periodisch);
        semester = (Spinner) findViewById(R.id.new_termin_semesterzugehoerigkeit);
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

        //Der Listener ist dafür Verantwortlich, dass das Enddatum mit dem Startdatum gefüllt wird, wenn noch der Default-String drinsteht
        startDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (endDate.getText().toString().compareTo("Enddatum") == 0) {
                    endDate.setText(startDate.getText());
                }
            }
        });

        //Verhindert, dass der Fokus auf dem obersten TextFeld bleibt.
        View.OnTouchListener tl = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //Der Parent der View ist die gesamte View an sich, dieser nimmt den Fokus nur deshalb an, weil focusable und focusableInTouchMode in activity_termin_input wahr gesetzt wurde.
                ((View) v.getParent()).requestFocus();
                //Gibt zurück, dass das TouchEvent noch nicht vollständig abgefangen wurde. True würde dazu führen, dass das Dropdownmenü nicht mehr aufklappt.
                return false;
            }
        };
        periode.setOnTouchListener(tl);
        semester.setOnTouchListener(tl);
        modul.setOnTouchListener(tl);
        typ.setOnTouchListener(tl);
        priorität.setOnTouchListener(tl);


        String[] perioden = {"keine Wiederholung", "Wöchentlich", "Zweiwöchentlich", "Vierwöchentlich"};
        Helper.fillSpinner(this, perioden, periode);

        final String[] typen = {"Kein Typ", "Vorlesung", "Seminar", "Praktikum", "Meeting", "Privat"};
        Helper.fillSpinner(this, typen, typ);

        final String[] prioritäten = {"Hoch", "Normal", "Niedrig"};
        Helper.fillSpinner(this, prioritäten, priorität);
        priorität.setSelection(1);

        final Plan[] allPlans = di.getAllPlans();
        refreshSemesterSpinner(allPlans);
        semester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int s = modul.getSelectedItemPosition();
                Module[] oldModuleSelection = currentModuleSelection;
                refreshModuleSpinner(di.getModulesByPlanID(currentSemesterSelection[position].getId()));
                boolean isthesame = true;
                if (oldModuleSelection.length == currentModuleSelection.length) {
                    for (int i = 0; i < oldModuleSelection.length; i++) {
                        if (oldModuleSelection[i].getId() != currentModuleSelection[i].getId()) {
                            isthesame = false;
                        }
                    }
                } else {
                    isthesame = false;
                }
                if (isthesame) {
                    modul.setSelection(s);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        refreshModuleSpinner(di.getAllModules());
        terminName.requestFocus();
        int editID = -1;
        if (getIntent().getExtras() != null) {
            editID = getIntent().getExtras().getInt("ID", -1);
        }
        if (editID == -1) {
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        di.insertDataTermine(terminName.getText().toString(), Helper.dateToSQL(startDate.getText().toString()), Helper.dateToSQL(endDate.getText().toString()), startTime.getText().toString() + ":00", endTime.getText().toString() + ":00", ort.getText().toString(), typen[typ.getSelectedItemPosition()], priorität.getSelectedItemPosition(), currentSemesterSelection[semester.getSelectedItemPosition()].getId(), currentModuleSelection[modul.getSelectedItemPosition()].getId(), ganztagstermin.isChecked() ? 1 : 0, dozent.getText().toString(), periodenSelectiontToPeriode(periode.getSelectedItemPosition()), 0, 0, "2016-01-01", 0);
                    } catch (IllegalArgumentException iae) {
                        Toast.makeText(getApplicationContext(), iae.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    finish();
                }
            });
        } else {
            final Termin editTermin = di.getTerminByID(editID);
            setTitle("Bearbeite " + editTermin.getName());

            terminName.setText(editTermin.getName());
            ganztagstermin.setChecked(editTermin.getIsGanztagsTermin() == 1);
            startDate.setText(Helper.sqlToGermanDate(editTermin.getStartDate().toString()));
            endDate.setText(Helper.sqlToGermanDate(editTermin.getWiederholungsEnde().toString()));
            startTime.setText(editTermin.getStartTime().toString().substring(0, 5));
            endTime.setText(editTermin.getEndTime().toString().substring(0, 5));
            int p = editTermin.getPeriode();
            int selection;
            switch (p) {
                case 0:
                    selection = 0;
                    break;
                case 7:
                    selection = 1;
                    break;
                case 14:
                    selection = 2;
                    break;
                case 28:
                    selection = 3;
                    break;
                default:
                    selection = 0;
                    break;
            }
            periode.setSelection(selection);
            semester.setSelection(editTermin.getPlanID() - 1);
            refreshModuleSpinner(di.getModulesByPlanID(editTermin.getPlanID()));
            selection = 0;
            if (currentModuleSelection != null) {
                for (int i = 0; i < currentModuleSelection.length; i++) {
                    Module m = currentModuleSelection[i];
                    if (m.getId() == editTermin.getModulID()) {
                        selection = i;
                        break;
                    }
                }
            }
            modul.setSelection(selection);

            switch (editTermin.getTyp()) {
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

            priorität.setSelection(editTermin.getPrioritaet());
            dozent.setText(editTermin.getDozent());
            ort.setText(editTermin.getOrt());

            submit.setText("Änderungen schreiben");
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        di.updateDataTermine(editTermin.getId(), terminName.getText().toString(), Helper.dateToSQL(startDate.getText().toString()), Helper.dateToSQL(endDate.getText().toString()), startTime.getText().toString() + ":00", endTime.getText().toString() + ":00", ort.getText().toString(), typen[typ.getSelectedItemPosition()], priorität.getSelectedItemPosition(), currentSemesterSelection[semester.getSelectedItemPosition()].getId(), currentModuleSelection[modul.getSelectedItemPosition()].getId(), ganztagstermin.isChecked() ? 1 : 0, dozent.getText().toString(), periodenSelectiontToPeriode(periode.getSelectedItemPosition()), 0, 0, "2016-01-01", 0);
                    } catch (IllegalArgumentException iae) {
                        Toast.makeText(getApplicationContext(), iae.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    finish();
                }
            });
        }
    }

    private int periodenSelectiontToPeriode(int s) {
        switch (s) {
            case 0:
                return 0;
            case 1:
                return 7;
            case 2:
                return 14;
            case 3:
                return 28;
            default:
                return 0;
        }
    }
}

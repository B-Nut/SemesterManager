package de.hsmw.semestermanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Benjamin on 06.12.2016.
 */

public class ModulInput extends AppCompatActivity {
    DatabaseInterface di;

    Plan[] plans;
    Spinner spinner;

    Button b;
    EditText inputName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modul_input);


        di = DatabaseInterface.getInstance(this);
        // you need to have a list of data that you want the spinner to display
        plans = di.getAllPlans(); //Die Liste (kann) nicht null sein, weil man Module nur dann setzen kann, wenn es auch einen Plan gibt. :)

        List<String> planNames = new ArrayList();
        List<Integer> planIDs = new ArrayList();
        for (Plan plan : plans) {
            planNames.add(plan.getName());
            planIDs.add(plan.getId());
        }
        //k
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, planNames);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner = (Spinner) findViewById(R.id.new_modul_semester);
        spinner.setAdapter(adapter);

        b = (Button) findViewById(R.id.new_modul_submit);
        inputName = (EditText) findViewById(R.id.new_modul_name);

        int editID = -1;
        if (getIntent().getExtras() != null) {
            editID = getIntent().getExtras().getInt("ID", -1);
        }
        if (editID == -1) {
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        di.insertDataModules(inputName.getText().toString(), plans[spinner.getSelectedItemPosition()].getId());
                    } catch (IllegalArgumentException iae) {
                        Toast.makeText(getApplicationContext(), iae.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    finish();
                }
            });
        } else {
            final Module editModul = di.getModuleByID(editID);
            b.setText("Änderungen schreiben");
            setTitle("Bearbeite " + editModul.getName());
            inputName.setText(editModul.getName());
            spinner.setSelection(editModul.getPlanID() - 1);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        di.updateDataModules(editModul.getId(), inputName.getText().toString(), plans[spinner.getSelectedItemPosition()].getId());
                    } catch (IllegalArgumentException iae) {
                        Toast.makeText(getApplicationContext(), iae.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    finish();
                }
            });
        }
    }
}

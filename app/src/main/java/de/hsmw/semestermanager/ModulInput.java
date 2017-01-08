package de.hsmw.semestermanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Benjamin on 06.12.2016.
 */

public class ModulInput extends AppCompatActivity {
    DatabaseHandler dh;
    DatabaseInterface di;

    Plan[] plans;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modul_input);


        di = DatabaseInterface.getInstance(this);

        // you need to have a list of data that you want the spinner to display
        plans = di.getAllPlans();

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

        Button b = (Button) findViewById(R.id.new_modul_submit);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                di.insertDataModules(((EditText) findViewById(R.id.new_modul_name)).getText().toString(), plans[spinner.getSelectedItemPosition()].getId());
                finish();
            }
        });
    }
}

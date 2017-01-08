package de.hsmw.semestermanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Benjamin on 04.12.2016.
 */

public class ModulView extends AppCompatActivity {

    Module selectedModule;

    ListView terminList;
    ArrayList<Termin> termine;
    TerminAdapterStandard termineListAdapter;
    TextView progress;

    DatabaseInterface di;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_modulview);

        progress = (TextView) findViewById(R.id.modulview_stunden);
        di = DatabaseInterface.getInstance(this);
        selectedModule = di.getDataByIDModules(getIntent().getExtras().getInt("ID"));
        terminList = (ListView) findViewById(R.id.list_termin_modulview);
        termine = new ArrayList<>();
        termineListAdapter = new TerminAdapterStandard(this, R.layout.listview_semesterview_termine, termine);
        terminList.setAdapter(termineListAdapter);
        terminList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {

                final Termin t = (Termin) parent.getItemAtPosition(position);
                String question;
                if (t.getPeriode() != 0 && t.getIsException() == 0) {
                    question = "Are you sure you want to delete this repeated appointment? This will also delete " + di.getCountExceptionsByID(t.getId()) + " exceptions.";
                } else if (t.getIsException() != 0) {
                    question = "Are you sure you want to delete this exception? The regular appointment the exception is referring to will be restored.";
                } else {
                    question = "Are you sure you want to delete this appointment?";
                }

                AlertDialog.Builder alert = new AlertDialog.Builder(terminList.getContext());
                alert.setMessage(question);
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        t.delete(parent.getContext());
                        updateView();
                        dialog.dismiss();
                    }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alert.show();
                return true;
            }
        });
        updateView();
    }

    public void updateView() {
        termine.clear();
        Termin[] t = di.getTermineByModulID(selectedModule.getId());
        for (Termin tt : t) {
            if (tt.getModulID() == selectedModule.getId()) {
                termine.add(tt);
            }
        }
        progress.setText(selectedModule.getProgressString(this));
        termineListAdapter.notifyDataSetChanged();
    }
}

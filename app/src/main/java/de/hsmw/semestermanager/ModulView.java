package de.hsmw.semestermanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

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
        selectedModule = di.getModuleByID(getIntent().getExtras().getInt("ID"));
        terminList = (ListView) findViewById(R.id.list_termin_modulview);
        termine = new ArrayList<>();
        termineListAdapter = new TerminAdapterStandard(this, R.layout.listview_semesterview_termine, termine);
        terminList.setAdapter(termineListAdapter);
        terminList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int terminID = ((Termin) parent.getItemAtPosition(position)).getId();
                Intent i = new Intent("de.hsmw.semestermanager.TerminView");
                i.putExtra("ID", terminID);
                startActivity(i);
            }
        });

        registerForContextMenu(terminList);
        updateView();
    }

    public void updateView() {
        termine.clear();
        try {
            termine.addAll(Arrays.asList(di.getTermineByModulID(selectedModule.getId())));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        progress.setText(selectedModule.getProgressString(this));
        termineListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle(selectedModule.getName());
        updateView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_edit_object:
                selectedModule.edit(this);
                return true;
            case R.id.action_delete_object:
                String question;
                question = "Are you sure you want to delete this module? All appointments associated with it are going to be deleted too";

                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setMessage(question);
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedModule.delete(getParent());
                        finish();
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
            default:
                return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.databaseobject, menu);
        return true;
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.list_termin_modulview) {
            menu.add("bearbeiten");
            menu.add("löschen");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Termin t = termineListAdapter.getItem(info.position);
        if (item.getTitle() == "löschen") {
            String question;
            if (t.getPeriode() != 0 && t.getIsException() == 0) {
                question = "Are you sure you want to delete this repeated appointment? This will also delete " + di.getCountExceptionsByID(t.getId()) + " exceptions.";
            } else if (t.getIsException() != 0) {
                question = "Are you sure you want to delete this exception? The regular appointment the exception is referring to will be restored.";
            } else {
                question = "Are you sure you want to delete this appointment?";
            }

            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage(question);
            alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    t.delete(info.targetView.getContext());
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
        } else if (item.getTitle() == "bearbeiten") {
            t.edit(this);
            return true;
        } else {
            return false;
        }
    }
}

package de.hsmw.semestermanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Benjamin on 04.12.2016.
 */

public class SemesterplanView extends AppCompatActivity {
    Plan selectedPlan;

    ScrollView scrollView;

    ListView moduleList;
    ArrayList<Module> modules;
    ArrayAdapter<Module> modulesListAdapter;

    ListView entryList;
    ArrayList<Termin> termine;
    TerminAdapterStandard termineListAdapter;

    DatabaseInterface di;

    // Copied from http://stackoverflow.com/questions/18367522/android-list-view-inside-a-scroll-view
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semesterplan_view);

        scrollView = (ScrollView) findViewById(R.id.semesterview_scrollview);

        di = DatabaseInterface.getInstance(this);

        //TextView id = (TextView) findViewById(R.id.semesterview_name);
        //id.setText("Der Name ist: " + selectedPlan.getName());

        moduleList = (ListView) findViewById(R.id.list_semesterview_module);
        modules = new ArrayList<>();
        modulesListAdapter = new ArrayAdapter<Module>(this, R.layout.listview_semesterview_modules, modules) {
            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = convertView;
                if (v == null) {
                    LayoutInflater vi = LayoutInflater.from(getContext());
                    v = vi.inflate(R.layout.listview_semesterview_modules, null);
                }
                TextView input = (TextView) v.findViewById(R.id.listentry_module_name);
                Module m = getItem(position);
                input.setText(m.getName());
                return v;
            }
        };
        moduleList.setAdapter(modulesListAdapter);

        moduleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int modulID = ((Module) parent.getItemAtPosition(position)).getId();
                Intent i = new Intent("de.hsmw.semestermanager.ModulView");
                i.putExtra("ID", modulID);
                startActivity(i);
            }
        });
        registerForContextMenu(moduleList);

        entryList = (ListView) findViewById(R.id.list_semesterview_termine);
        termine = new ArrayList<>();
        termineListAdapter = new TerminAdapterStandard(this, R.layout.listview_semesterview_termine, termine);
        entryList.setAdapter(termineListAdapter);
        entryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int terminID = ((Termin) parent.getItemAtPosition(position)).getId();
                Intent i = new Intent("de.hsmw.semestermanager.TerminView");
                i.putExtra("ID", terminID);
                startActivity(i);
            }
        });
        registerForContextMenu(entryList);
    }

    /**
     * Aktualisiert beide Listen, durch eine erneute Datenbankabfrage.
     */
    public void updateLists() {
        modules.clear();
        try {
            modules.addAll(Arrays.asList(di.getModulesByPlanID(selectedPlan.getId())));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        setListViewHeightBasedOnChildren(moduleList);
        modulesListAdapter.notifyDataSetChanged();

        termine.clear();
        try {
            termine.addAll(Arrays.asList(di.getTermineByPlanIDButNotModulID(selectedPlan.getId())));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        setListViewHeightBasedOnChildren(entryList);
        termineListAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.plan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_new_modul) {
            startActivity(new Intent("de.hsmw.semestermanager.ModulInput"));
            return true;
        }
        if (id == R.id.action_add_new_entry) {
            startActivity(new Intent("de.hsmw.semestermanager.TerminInput"));
            return true;
        }
        if (id == R.id.action_view_day) {
            Intent i = new Intent("de.hsmw.semestermanager.DayView");
            i.putExtra("page", 21);
            i.putExtra("ID", selectedPlan.getId());
            startActivity(i);
            return true;
        }
        if (id == R.id.action_edit_object) {
            selectedPlan.edit(this);
            return true;
        }
        if (id == R.id.action_delete_object) {
            String question;
            question = "Are you sure you want to delete this semester? All appointments and modules associated with it are going to be deleted too.";

            android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(this);
            alert.setMessage(question);
            alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    selectedPlan.delete(getParent());
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.list_semesterview_termine || v.getId() == R.id.list_semesterview_module) {
            menu.add("bearbeiten");
            menu.add("löschen");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (info.targetView.getParent().equals(entryList)) {
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

                AlertDialog.Builder alert = new AlertDialog.Builder(entryList.getContext());
                alert.setMessage(question);
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        t.delete(info.targetView.getContext());
                        updateLists();
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
        } else if (info.targetView.getParent().equals(moduleList)) {
            final Module m = modulesListAdapter.getItem(info.position);
            if (item.getTitle() == "löschen") {
                String question;
                question = "Are you sure you want to delete this module? All appointments associated with it are going to be deleted too";

                AlertDialog.Builder alert = new AlertDialog.Builder(moduleList.getContext());
                alert.setMessage(question);
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m.delete(getParent());
                        updateLists();
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
                m.edit(this);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    protected void onStart() {

        int i = getIntent().getExtras().getInt("ID");
        selectedPlan = di.getPlanByID(i);

        setTitle(selectedPlan.getName());

        super.onStart();
        updateLists();
        scrollView.fullScroll(View.FOCUS_UP);
    }
}
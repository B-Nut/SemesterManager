package de.hsmw.semestermanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DatabaseInterface di;

    ListView listView;
    ArrayList<Plan> values;

    private SemesterAdapter sAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        di = DatabaseInterface.getInstance(this);

        listView = (ListView) findViewById(R.id.mainList);

        values = new ArrayList<>();
        sAdapter = new SemesterAdapter(this, R.layout.listview_overview, values);
        listView.setAdapter(sAdapter);

        updateList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Plan clickedPlan = (Plan) parent.getItemAtPosition(position);
                Intent i = new Intent("de.hsmw.semestermanager.SemesterplanView");
                i.putExtra("ID", clickedPlan.getId());
                //Toast.makeText(MainActivity.this, Integer.toString(clickedPlan.getId()), Toast.LENGTH_LONG).show();
                startActivity(i);
            }
        });
        registerForContextMenu(listView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateList();
    }

    public void updateList() {
        values.clear();
        Plan[] plans = di.getAllPlans();
        try {
            values.addAll(Arrays.asList(plans));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        sAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.mainList) {
            menu.add("bearbeiten");
            menu.add("löschen");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int planID = Integer.valueOf(info.targetView.findViewById(R.id.textSemesterName).getContentDescription().toString());
        final Plan selectedPlan = di.getPlanByID(planID);
        switch (item.getTitle().toString()) {
            case "löschen":
                String question;
                question = "Are you sure you want to delete this semester? All appointments and modules associated with it are going to be deleted too.";

                android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(listView.getContext());
                alert.setMessage(question);
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedPlan.delete(getApplicationContext());
                        updateList();
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
            case "bearbeiten":
                selectedPlan.edit(this);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_new_semester) {
            startActivity(new Intent("de.hsmw.semestermanager.SemesterplanInput"));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        return true;
    }
}

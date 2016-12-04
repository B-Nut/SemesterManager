package de.hsmw.semestermanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DatabaseHandler dh;
    DatabaseInterface di;

    ListView listView;
    ArrayList<Plan> values;

    private SemesterAdapter sAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dh = new DatabaseHandler(this);
        di = new DatabaseInterface(dh.getWritableDatabase());

        listView = (ListView) findViewById(R.id.mainList);

        values = new ArrayList<>();
        sAdapter = new SemesterAdapter(this, R.layout.listview_overview, values);
        listView.setAdapter(sAdapter);

        updateList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Plan clickedPlan = (Plan) parent.getItemAtPosition(position);
                Intent i = new Intent("de.hsmw.semestermanager.SemesterplanView");
                i.putExtra("ID", (long) clickedPlan.getId());
                //Toast.makeText(MainActivity.this, Integer.toString(clickedPlan.getId()), Toast.LENGTH_LONG).show();
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateList();
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void updateList() {
        values.clear();
        Plan[] plans = di.getAllPlans();
        values.addAll(Arrays.asList(plans));
        sAdapter.notifyDataSetChanged();
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

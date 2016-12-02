package de.hsmw.semestermanager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DatabaseHandler dh;
    DatabaseInterface di;
    EditText editName, editTime, editID;
    Button btnAddData, btnGetData;
    ListView listView;
    TextView result;
    ArrayList<Semester> values;
    private SemesterAdapter sAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        dh = new DatabaseHandler(this);
        di = new DatabaseInterface(dh.getWritableDatabase());

        editName = (EditText) findViewById(R.id.editName);
        editTime = (EditText) findViewById(R.id.editZeitraum);
        btnAddData = (Button) findViewById(R.id.buttonAdd);
        btnGetData = (Button) findViewById(R.id.buttonViewAll);

        editID = (EditText) findViewById(R.id.editID);
        result = (TextView) findViewById(R.id.outputResult);
        listView = (ListView) findViewById(R.id.mainList);

        //addData();
        //viewAll();
        viewID();
        viewList();
        updateList();
    }

    public void addData() {
        btnAddData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, Long.toString(di.insertData(editName.getText().toString(), editTime.getText().toString())), Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    public void viewAll() {
        btnGetData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cursor c = di.getAllData();
                        if (c.getCount() == 0) {
                            // BITCH YOU ARE STUPID
                            showMessage("BITCH YOU ARE STUPID", "Ter is Noting");
                            return;
                        }
                        StringBuffer buffer = new StringBuffer();
                        while (c.moveToNext()) {
                            buffer.append("ID : " + c.getString(0) + "\n");
                            buffer.append("Name : " + c.getString(1) + "\n");
                            buffer.append("Zeitraum : " + c.getString(2) + "\n\n");
                        }
                        showMessage("Data", buffer.toString());
                    }
                }
        );
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void viewID() {
        btnGetData.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int id = Integer.parseInt(editID.getText().toString());
                Cursor c = di.getDataByID(id);
                c.moveToNext(); //Cursor zeigt Anfangs auf nichts (−1).
                result.setText(c.getString(1));
            }
        });
    }

    public void viewList() {
        values = new ArrayList<>();
        sAdapter = new SemesterAdapter(this, R.layout.listview_overview, values);
        listView.setAdapter(sAdapter);
        btnAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                di.insertData(editName.getText().toString(), editTime.getText().toString());
                updateList();
            }
        });
    }

    public void updateList() {
        Cursor c = di.getAllData();
        values.clear();
        while (c.moveToNext()) {
            values.add(new Semester(c.getInt(0), c.getString(1), c.getString(2), c.getString(2)));
        }
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
        if (id == R.id.action_settings) {
            return true;
        }
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

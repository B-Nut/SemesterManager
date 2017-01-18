package de.hsmw.semestermanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Created by Benjamin on 14.01.2017.
 */

public class TerminView extends AppCompatActivity {

    Termin selectedTermin;
    DatabaseInterface di;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminview);

        di = DatabaseInterface.getInstance(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_edit_object:
                selectedTermin.edit(this);
                return true;
            case R.id.action_delete_object:
                String question;
                if (selectedTermin.getPeriode() != 0 && selectedTermin.getIsException() == 0) {
                    question = "Are you sure you want to delete this repeated appointment? This will also delete " + di.getCountExceptionsByID(selectedTermin.getId()) + " exceptions.";
                } else if (selectedTermin.getIsException() != 0) {
                    question = "Are you sure you want to delete this exception? The regular appointment the exception is referring to will be restored.";
                } else {
                    question = "Are you sure you want to delete this appointment?";
                }

                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setMessage(question);
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedTermin.delete(getParent());
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
    protected void onStart() {
        super.onStart();

        TextView uhrzeit = (TextView) findViewById(R.id.terminview_zeiten);
        TextView datum = (TextView) findViewById(R.id.terminview_datum);
        TextView wiederholung = (TextView) findViewById(R.id.terminview_wiederholungsbezeichnung);
        TextView prioritaet = (TextView) findViewById(R.id.terminview_prioritätwert);
        TextView modul = (TextView) findViewById(R.id.terminview_modulbezeichnung);
        TextView typ = (TextView) findViewById(R.id.terminview_termintypbezeichnung);
        TextView dozent = (TextView) findViewById(R.id.terminview_dozentenbezeichnung);
        TextView ort = (TextView) findViewById(R.id.terminview_ortsbezeichnung);
        TextView ausnahmenzaehler = (TextView) findViewById(R.id.terminview_ausnahmenbezeichnung);

        int i = getIntent().getExtras().getInt("ID");
        selectedTermin = di.getTerminByID(i);
        setTitle(selectedTermin.getName());

        uhrzeit.setText(selectedTermin.getTimeString());
        datum.setText(selectedTermin.getDateString());
        wiederholung.setText(selectedTermin.getWiederholungsString());
        switch (selectedTermin.getPrioritaet()) {
            case 0:
                prioritaet.setText("Hoch");
                prioritaet.setTextColor(Color.RED);
                break;
            case 1:
                prioritaet.setText("Normal");
                prioritaet.setTextColor(Color.BLACK);
                break;
            case 2:
                prioritaet.setText("Niedrig");
                prioritaet.setTextColor(Color.GRAY);
                break;
            default:
                prioritaet.setText("Priorität korrupt.");
                prioritaet.setTextColor(Color.BLACK);
                break;
        }
        try {
            modul.setText(selectedTermin.getModule(this).getName());
        } catch (NullPointerException e) {
            modul.setText("Ohne");
        }
        typ.setText(selectedTermin.getTyp());
        dozent.setText(selectedTermin.getDozent());
        ort.setText(selectedTermin.getOrt());
        if (selectedTermin.getPeriode() > 0 && selectedTermin.getIsException() == 0) {
            ausnahmenzaehler.setText(Long.toString(di.getCountExceptionsByID(selectedTermin.getId())));
        } else if (selectedTermin.getIsException() != 0) {
            ausnahmenzaehler.setText("");
            ((TextView) findViewById(R.id.terminview_ausnahmen)).setText("Ersetzt " + di.getTerminByID(selectedTermin.getExceptionContextID()).getName() + " am "+ Helper.sqlToGermanDate(selectedTermin.getExceptionTargetDay().toString()));
        }else {
            ausnahmenzaehler.setText("");
            ((TextView) findViewById(R.id.terminview_ausnahmen)).setText("");
            findViewById(R.id.strich5).setAlpha(0);
            findViewById(R.id.strich6).setAlpha(0);
        }
    }
}

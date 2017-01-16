package de.hsmw.semestermanager;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Thomas on 04.12.2016.
 */

public class TerminAdapterStandard extends ArrayAdapter<Termin> {

    int viewResource;

    public TerminAdapterStandard(Context context, int resource, List<Termin> items) {
        super(context, resource, items);
        viewResource = resource;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi = LayoutInflater.from(getContext());
            v = vi.inflate(viewResource, null);
        }

        Termin t = getItem(position);

        if (t != null) {
            TextView semesterview_termin_zeiten = (TextView) v.findViewById(R.id.semesterview_termin_zeiten);
            TextView semesterview_termin_datum = (TextView) v.findViewById(R.id.semesterview_termin_datum);
            TextView semesterview_termin_ort = (TextView) v.findViewById(R.id.semesterview_termin_ort);
            TextView semesterview_termin_modul = (TextView) v.findViewById(R.id.semesterview_termin_modul);
            TextView semesterview_termin_dozenten = (TextView) v.findViewById(R.id.semesterview_termin_dozenten);
            TextView semesterview_termin_wiederholung = (TextView) v.findViewById(R.id.semesterview_termin_wiederholung);
            TextView semesterview_termin_prioritätswert = (TextView) v.findViewById(R.id.semesterview_termin_prioritätswert);
            TextView semesterview_termin_termintyp = (TextView) v.findViewById(R.id.semesterview_termin_termintyp);

            if (semesterview_termin_zeiten != null) {
                semesterview_termin_zeiten.setText(t.getTimeString());
            }
            if (semesterview_termin_datum != null) {
                semesterview_termin_datum.setText(t.getDateString());
            }
            if (semesterview_termin_ort != null) {
                semesterview_termin_ort.setText(t.getOrt());
            }
            if (semesterview_termin_modul != null) {
                semesterview_termin_modul.setText(t.getName());
            }
            if (semesterview_termin_dozenten != null) {
                semesterview_termin_dozenten.setText(t.getDozent());
            }
            if (semesterview_termin_wiederholung != null) {
                if (t.getPeriode() != 0 && t.getIsException() == 0) {
                    semesterview_termin_wiederholung.setText(t.getWiederholungsString());
                } else if (t.getIsException() != 0) {
                    semesterview_termin_wiederholung.setText("Ausnahme");
                } else {
                    semesterview_termin_wiederholung.setText("");
                }
            }
            if (semesterview_termin_prioritätswert != null) {
                switch (t.getPrioritaet()) {
                    case 0:
                        semesterview_termin_prioritätswert.setText("Hoch");
                        semesterview_termin_prioritätswert.setTextColor(Color.RED);
                        break;
                    case 1:
                        semesterview_termin_prioritätswert.setText("Normal");
                        semesterview_termin_prioritätswert.setTextColor(Color.BLACK);
                        break;
                    case 2:
                        semesterview_termin_prioritätswert.setText("Niedrig");
                        semesterview_termin_prioritätswert.setTextColor(Color.GRAY);
                        break;
                    default:
                        semesterview_termin_prioritätswert.setText("Priorität korrupt.");
                        semesterview_termin_prioritätswert.setTextColor(Color.BLACK);
                        break;
                }
            }
            if (semesterview_termin_termintyp != null) {
                semesterview_termin_termintyp.setText(t.getTyp());
            }
        }
        return v;
    }
}

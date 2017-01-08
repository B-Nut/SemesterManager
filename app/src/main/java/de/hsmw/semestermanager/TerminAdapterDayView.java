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
 * Created by Benjamin on 30.12.2016.
 */

public class TerminAdapterDayView<T> extends ArrayAdapter {

    int viewResource;

    public TerminAdapterDayView(Context context, int resource, List<T> objects) {
        super(context, resource, objects);
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
        Termin t = (Termin) getItem(position);

        if (t != null) {
            TextView tagesview_termin_zeiten = (TextView) v.findViewById(R.id.tagesview_termin_zeiten);
            TextView tagesview_termin_datum = (TextView) v.findViewById(R.id.tagesview_termin_datum);
            TextView tagesview_termin_ort = (TextView) v.findViewById(R.id.tagesview_termin_ort);
            TextView tagesview_termin_modul = (TextView) v.findViewById(R.id.tagesview_termin_modul);
            TextView tagesview_termin_dozenten = (TextView) v.findViewById(R.id.tagesview_termin_dozenten);
            TextView tagesview_termin_wiederholung = (TextView) v.findViewById(R.id.tagesview_termin_wiederholung);
            TextView tagesview_termin_prioritätswert = (TextView) v.findViewById(R.id.tagesview_termin_prioritätswert);
            TextView tagesview_termin_termintyp = (TextView) v.findViewById(R.id.tagesview_termin_termintyp);

            if (tagesview_termin_zeiten != null) {
                tagesview_termin_zeiten.setText(t.getTimeString());
            }
            if (tagesview_termin_datum != null) {
                String text;
                try {
                    text = t.getModule(getContext()).getName();
                } catch (Exception e) {
                    text = "";
                }
                tagesview_termin_datum.setText(text);
            }
            if (tagesview_termin_ort != null) {
                tagesview_termin_ort.setText(t.getOrt());
            }
            if (tagesview_termin_modul != null) {
                tagesview_termin_modul.setText(t.getName());
            }
            if (tagesview_termin_dozenten != null) {
                tagesview_termin_dozenten.setText(t.getDozent());
            }
            if (tagesview_termin_wiederholung != null) {
                tagesview_termin_wiederholung.setText("");
            }
            if (tagesview_termin_prioritätswert != null) {
                setPrioritaet(tagesview_termin_prioritätswert, t);
            }
            if (tagesview_termin_termintyp != null) {
                tagesview_termin_termintyp.setText(t.getTyp());
            }
        }
        return v;
    }

    private void setPrioritaet(TextView tv, Termin t) {
        if (t.getPrioritaet() == 0) {
            tv.setText("Hoch");
            tv.setTextColor(Color.RED);
        } else if (t.getPrioritaet() == 1) {
            tv.setText("Normal");
            tv.setTextColor(Color.BLACK);
        } else {
            tv.setText("Niedrig");
            tv.setTextColor(Color.GRAY);
        }
    }
}

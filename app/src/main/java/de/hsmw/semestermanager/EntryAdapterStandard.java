package de.hsmw.semestermanager;

import android.content.Context;
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

public class EntryAdapterStandard extends ArrayAdapter <Entry> {

    public EntryAdapterStandard(Context context, int resource) {
        super(context, resource);
    }

    public EntryAdapterStandard(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public EntryAdapterStandard(Context context, int resource, Entry[] objects) {
        super(context, resource, objects);
    }

    public EntryAdapterStandard(Context context, int resource, int textViewResourceId, Entry[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public EntryAdapterStandard(Context context, int resource, List<Entry> objects) {
        super(context, resource, objects);
    }

    public EntryAdapterStandard(Context context, int resource, int textViewResourceId, List<Entry> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.listview_semesterview_termine,null);
        }

        Entry e = (Entry) getItem(position);

        if(e != null) {
            TextView semesterview_termin_zeiten = (TextView) v.findViewById(R.id.semesterview_termin_zeiten);
            TextView semesterview_termin_datum = (TextView) v.findViewById(R.id. semesterview_termin_datum);
            TextView semesterview_termin_ort = (TextView) v.findViewById(R.id.semesterview_termin_ort);
            TextView semesterview_termin_modul = (TextView) v.findViewById(R.id.semesterview_termin_modul);
            TextView semesterview_termin_dozenten = (TextView) v.findViewById(R.id.semesterview_termin_dozenten);
            TextView semesterview_termin_wiederholung = (TextView) v.findViewById(R.id.semesterview_termin_wiederholung);
            TextView semesterview_termin_prioritätswert = (TextView) v.findViewById(R.id.semesterview_termin_prioritätswert);
            TextView semesterview_termin_termintyp = (TextView) v.findViewById(R.id.semesterview_termin_termintyp);

            if (semesterview_termin_zeiten != null) {
                semesterview_termin_zeiten.setText(e.getTimeString());
            }
            if (semesterview_termin_datum != null) {
                semesterview_termin_datum.setText(e.getDateString());
            }
            if (semesterview_termin_ort != null) {
                semesterview_termin_ort.setText(e.getOrt());
            }
            if (semesterview_termin_modul != null) {
                semesterview_termin_modul.setText(e.getModulID());
            }
            if (semesterview_termin_dozenten != null) {
                semesterview_termin_dozenten.setText(e.getDozent());
            }
            if (semesterview_termin_wiederholung != null) {
                semesterview_termin_wiederholung.setText("");
            }
            if (semesterview_termin_prioritätswert != null) {
                semesterview_termin_prioritätswert.setText(e.getPrioritaet());
            }
            if (semesterview_termin_termintyp != null) {
                semesterview_termin_termintyp.setText(e.getTyp());
            }
        }
        return v;
    }
}

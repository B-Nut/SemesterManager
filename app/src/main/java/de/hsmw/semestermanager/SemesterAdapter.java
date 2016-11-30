package de.hsmw.semestermanager;

import android.content.ClipData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Benjamin on 30.11.2016.
 */

public class SemesterAdapter extends ArrayAdapter<Semester> {

    public SemesterAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public SemesterAdapter(Context context, int resource, List<Semester> items) {
        super(context, resource, items);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null){
            LayoutInflater vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.listview_overview,null);
        }

        Semester s = (Semester) getItem(position);

        if( s != null) {
            TextView textName = (TextView) v.findViewById(R.id.textSemesterName);
            TextView textStartDate = (TextView) v.findViewById(R.id.textSemesterStartDate);
            TextView textEndDate = (TextView) v.findViewById(R.id.textSemesterEndDate);

            if (textName != null){
                textName.setText(s.getName());
            }
            if (textEndDate != null){
                textEndDate.setText(s.getEndDate());
            }
            if (textStartDate != null){
                textStartDate.setText(s.getStartDate());
            }
        }
        return v;
    }
}

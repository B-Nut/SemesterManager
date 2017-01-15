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
 * Created by Eric on 30.11.2016.
 */

public class SemesterAdapter extends ArrayAdapter<Plan> {
    int viewResource;

    public SemesterAdapter(Context context, int resource, List<Plan> items) {
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

        Plan s = getItem(position);

        if (s != null) {
            TextView textName = (TextView) v.findViewById(R.id.textSemesterName);
            TextView textStartDate = (TextView) v.findViewById(R.id.textSemesterStartDate);
            TextView textEndDate = (TextView) v.findViewById(R.id.textSemesterEndDate);

            if (textName != null) {
                textName.setText(s.getName());
                textName.setContentDescription(Integer.toString(s.getId()));
            }
            if (textEndDate != null) {
                textEndDate.setText(Helper.sqlToGermanDate(s.getEndDate().toString()));
            }
            if (textStartDate != null) {
                textStartDate.setText(Helper.sqlToGermanDate(s.getStartDate().toString()));
            }
        }
        return v;
    }
}

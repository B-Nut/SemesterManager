package de.hsmw.semestermanager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    ArrayList<Entry> entries;
    ArrayAdapter<Entry> entriesListAdapter;

    DatabaseHandler dh;
    DatabaseInterface di;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semesterplan_view);

        scrollView = (ScrollView) findViewById(R.id.semesterview_scrollview);

        dh = new DatabaseHandler(this);
        di = new DatabaseInterface(dh.getWritableDatabase());

        int i = (int) getIntent().getExtras().getLong("ID");
        selectedPlan = di.getDataByIDPlans(i);

        setTitle(selectedPlan.getName());

        //TextView id = (TextView) findViewById(R.id.semesterview_name);
        //id.setText("Der Name ist: " + selectedPlan.getName());

        moduleList = (ListView) findViewById(R.id.list_semesterview_module);
        modules = new ArrayList<>();
        modulesListAdapter = new ArrayAdapter<Module>(this, R.layout.listview_semesterview_modules, modules){
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

        entryList = (ListView) findViewById(R.id.list_semesterview_termine);
        entries = new ArrayList<>();
        entriesListAdapter = new EntryAdapterStandard(this, R.layout.listview_element, entries);
        entryList.setAdapter(entriesListAdapter);
        updateLists();
        scrollView.fullScroll(View.FOCUS_UP);
    }

    public void updateLists() {
        modules.clear();
        modules.addAll(Arrays.asList(di.getModulesByPlanID(selectedPlan.getId())));
        setListViewHeightBasedOnChildren(moduleList);
        modulesListAdapter.notifyDataSetChanged();

        entries.clear();
        entries.addAll(Arrays.asList(di.getEntriesByPlanID(selectedPlan.getId())));
        setListViewHeightBasedOnChildren(entryList);
        entriesListAdapter.notifyDataSetChanged();
    }


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
}
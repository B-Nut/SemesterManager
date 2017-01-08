package de.hsmw.semestermanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class DayView extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_dayview);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(21);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewPager.setCurrentItem(21);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private ListView listView;
        private ArrayList<Termin> termine;
        private TerminAdapterDayView<Termin> terminListAdapter;
        private DatabaseInterface di;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                                 final Bundle savedInstanceState) {
            int scrollOffset = 22;
            final int currentPage = getArguments().getInt(ARG_SECTION_NUMBER) - scrollOffset;
            final View rootView = inflater.inflate(R.layout.fragment_day_view, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            final Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, currentPage);
            SimpleDateFormat df = new SimpleDateFormat("EEEE - dd.MM.yyyy");
            String formattedDate = df.format(c.getTime());
            Log.d("onCreateView", formattedDate);
            textView.setText(formattedDate);

            di = DatabaseInterface.getInstance(rootView.getContext());

            listView = (ListView) rootView.findViewById(R.id.list_termin_dayview);
            termine = new ArrayList<>();
            terminListAdapter = new TerminAdapterDayView<>(rootView.getContext(), R.layout.listview_tagesview_termine, termine);
            listView.setAdapter(terminListAdapter);

            final SimpleDateFormat af = new SimpleDateFormat("yyyy-MM-dd");
            updateList(af.format(c.getTime()));
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {

                    final Termin t = (Termin) parent.getItemAtPosition(position);
                    String question;
                    if (t.getPeriode() != 0 && t.getIsException() == 0) {
                        question = "Are you sure you want to delete this repeated appointment? This will also delete " + di.getCountExceptionsByID(t.getId()) + " exceptions.";
                    } else if (t.getIsException() != 0) {
                        question = "Are you sure you want to delete this exception? The regular appointment the exception is referring to will be restored.";
                    } else {
                        question = "Are you sure you want to delete this appointment?";
                    }

                    AlertDialog.Builder alert = new AlertDialog.Builder(listView.getContext());
                    //alert.setTitle(question);
                    alert.setMessage(question);
                    alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            t.delete(parent.getContext());
                            updateList(af.format(c.getTime()));
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
                }
            });
            return rootView;
        }

        public void updateList(String date) {
            termine.clear();
            //TODO: Hardcoded integer entfernen.
            termine.addAll(Arrays.asList(di.getTermineByDate(date, 1, true)));
            //Log.d("database",di.getTermineByDate(date)[0].getName());
            terminListAdapter.notifyDataSetChanged();
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 100;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
                case 3:
                    return "Section 4";
            }
            return null;
        }
    }
}
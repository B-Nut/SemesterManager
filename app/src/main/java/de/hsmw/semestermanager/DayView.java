package de.hsmw.semestermanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Date;
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
        mViewPager.setCurrentItem(getIntent().getExtras().getInt("page", 1));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
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
        final int scrollOffset = 22;
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
            final int currentPage = getArguments().getInt(ARG_SECTION_NUMBER) - scrollOffset;
            final View rootView = inflater.inflate(R.layout.fragment_day_view, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            final Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, currentPage);
            SimpleDateFormat df = new SimpleDateFormat("EEEE - dd.MM.yyyy");
            String formattedDate = df.format(c.getTime());
            Log.d("onCreateView", formattedDate);
            Log.d("onCreateView", Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
            textView.setText(formattedDate);

            //Speichere die aktuelle Seitennummer in die View, um nach dem Aufruf des Contextmenuclicklisteners die Activity an der richtigen Stelle neu zu starten.
            textView.setContentDescription(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));

            di = DatabaseInterface.getInstance(rootView.getContext());

            listView = (ListView) rootView.findViewById(R.id.list_termin_dayview);
            termine = new ArrayList<>();
            terminListAdapter = new TerminAdapterDayView<>(rootView.getContext(), R.layout.listview_tagesview_termine, termine);
            listView.setAdapter(terminListAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    final int page = Integer.valueOf(((View) view.getParent().getParent()).findViewById(R.id.section_label).getContentDescription().toString());
                    getActivity().getIntent().putExtra("page", page - 1);

                    int terminID = ((Termin) parent.getItemAtPosition(position)).getId();
                    Intent i = new Intent("de.hsmw.semestermanager.TerminView");
                    i.putExtra("ID", terminID);
                    startActivity(i);
                }
            });

            //Aktualisiere die Terminliste
            final SimpleDateFormat af = new SimpleDateFormat("yyyy-MM-dd");
            String date = af.format(c.getTime());
            int semesterID = getActivity().getIntent().getExtras().getInt("ID", 1);
            termine.clear();
            try {
                //getTermineByDate gibt null zurück, wenn es keine Termine an dem Tag gibt...
                termine.addAll(Arrays.asList(di.getTermineByDate(date, semesterID, true)));
            } catch (NullPointerException e) {
                //Kein Stacktrace - kein Problem.
                //e.printStackTrace();
            }
            terminListAdapter.notifyDataSetChanged();
            registerForContextMenu(listView);

            return rootView;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            int terminID = Integer.valueOf(info.targetView.findViewById(R.id.tagesview_termin_zeiten).getContentDescription().toString());
            final Termin t = DatabaseInterface.getInstance(getContext()).getTerminByID(terminID);

            if (v.getId() == R.id.list_termin_dayview) {
                menu.add("bearbeiten");
                if (t.getPeriode() > 0 && t.getIsException() == 0) {
                    menu.add("erstelle Ausnahme");
                }
                menu.add("löschen");
            }
        }

        @Override
        public boolean onContextItemSelected(MenuItem item) {
            /*
            Schmutzig schmutzig, kleine Erklärung hierzu:
            Wenn ein ContextItem ausgewählt wurde, also eine Option des Popup-Menüs, dann muss ich mich erst einmal Orientieren, zu welchem Termin die Auswahl gehört hat.
            NORMALERWEISE rufe ich den terminListAdapter, der die Schnittstelle zwischen den Items und der Liste darstellt,
            ABER dadurch, dass wir diese coole Seiten zum links und rechts schieben haben, ist dieses Objekt mit Items eines anderen Tages geladen, da die View außerhalb meiner Kontrolle Nebenseiten lädt.
            Das einzige, was ich habe ist die tatsächliche gefüllte XML-View, denen ich zwei Werte als ContentDescription hinter TextWerte untergeschoben habe:
                1. Die Nummer der aktuellen Seite auf der wir gerade sind.
                2. Die ID des Termins, das gerade angezeigt wird.
            Ich saß da bestimmt 5 Stunden bevor ich zu dieser Lösung gegriffen habe, sorry.
            */
            final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int terminID = Integer.valueOf(info.targetView.findViewById(R.id.tagesview_termin_zeiten).getContentDescription().toString());
            final Termin t = DatabaseInterface.getInstance(getContext()).getTerminByID(terminID);

            final int page = Integer.valueOf(((View) info.targetView.getParent().getParent()).findViewById(R.id.section_label).getContentDescription().toString());

            if (item.getTitle() == "löschen") {
                String question;
                if (t.getPeriode() != 0 && t.getIsException() == 0) {
                    question = "Are you sure you want to delete this repeated appointment? This will also delete " + di.getCountExceptionsByID(t.getId()) + " exceptions.";
                } else if (t.getIsException() != 0) {
                    question = "Are you sure you want to delete this exception? The regular appointment the exception is referring to will be restored.";
                } else {
                    question = "Are you sure you want to delete this appointment?";
                }

                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setMessage(question);
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        t.delete(info.targetView.getContext());
                        //Da sich der FragmentManager nicht aktualisieren lässt, habe ich entschlossen die ganze Activity neu zu starten!
                        getActivity().getIntent().putExtra("page", page - 1);
                        getActivity().recreate();
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
            } else if (item.getTitle() == "bearbeiten") {
                getActivity().getIntent().putExtra("page", page - 1);
                t.edit(getContext());
                return true;
            } else if (item.getTitle() == "erstelle Ausnahme") {
                //Ich möchte nicht den Termin haben, sondern die Terminwiederholungsausnahme an dem Tag.
                //Weil ich aber nur die TerminID übertrage, erzeuge ich mir damit und mit dem Datum aus dem Titel der View NOCHMAL das Terminwiederholungsobjekt für den besagten Tag.
                //Ich bin so ziemlich am Ende...
                String titleString = ((TextView) ((View) info.targetView.getParent().getParent()).findViewById(R.id.section_label)).getText().toString(); //Frag nicht...
                //Sooo, den hinteren Teil des Titels noch in einen Date umwandeln... oops, Stringkonvertierung... und getTerminAtDate aufgerufen - voila!
                Termin terminwiederholung = t.getTerminAtDate(Date.valueOf(Helper.dateToSQL(titleString.split(" ")[2])));
                getActivity().getIntent().putExtra("page", page - 1);
                terminwiederholung.createException(getContext());
                return true;
            } else {
                return false;
            }
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
            return 100;
        }
    }
}
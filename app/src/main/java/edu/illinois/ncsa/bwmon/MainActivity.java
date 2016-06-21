package edu.illinois.ncsa.bwmon;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import android.graphics.Color;


import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private GraphicalView mChartView;
    private CategorySeries distributionSeries;
    private DefaultRenderer defaultRenderer;
    int nPieDrawn=0;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mViewPager.setOffscreenPageLimit(3);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        //drawPie(0,"Time,0,0,0");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_action_update) {
            new DownloadTask(0).execute("http://isce.ncsa.illinois.edu/tools/use.php");
            new DownloadTask(1).execute("http://isce.ncsa.illinois.edu/tools/big10.php?txt");
            new DownloadTask(2).execute("http://isce.ncsa.illinois.edu/tools/backfill.php");

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private InputStream downloadUrl(String urlString) throws IOException {
        // BEGIN_INCLUDE(get_inputstream)
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Start the query
        conn.connect();
        InputStream stream = conn.getInputStream();
        return stream;
        // END_INCLUDE(get_inputstream)
    }

    private String loadFromNetwork(String urlString) throws IOException {
        InputStream stream = null;
        String str = "";

        try {
            stream = downloadUrl(urlString);
            str = readIt(stream, 1024);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        return str;
    }

    private String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://edu.illinois.ncsa.bwmon/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://edu.illinois.ncsa.bwmon/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    class DownloadTask extends AsyncTask<String, Void, String> {
        private int nPosition;

        public DownloadTask(int nPos) {
            nPosition = nPos;
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                return loadFromNetwork(urls[0]);
            } catch (IOException e) {
                return "Connection Error";
            }
        }

        /**
         * Uses the logging framework to display the output of the fetch
         * operation in the log fragment.
         */
        @Override
        protected void onPostExecute(String result) {
            //Log.i(TAG, result);
            //fragment.setTitle(result, nPosition);
            if(nPosition==0) {
                updatePie(0, result);
            }

        else {
                View v1 = mViewPager.getChildAt(nPosition);
                TextView tv1 = (TextView) v1.findViewById(R.id.section_label);
                tv1.setText(result);
            }
        }

    }

    public void updatePie( int position, String data ) {
        if(nPieDrawn==0)
        {
            drawPie(position,data);
            nPieDrawn=1;

        }
        else {
            if (mChartView != null) {
                distributionSeries.clear();
                String[] headdat = data.split("\n");
                String [] header =   headdat[0].split(",");
                String[] vals = headdat[1].split(",");
                String date=header[1];
                Double dInUse = Double.parseDouble(vals[0]);
                Double dIdle = Double.parseDouble(vals[1]);
                Double dDown = Double.parseDouble(vals[2]);
                double[] distribution = {dInUse, dIdle, dDown};
                final String[] status = new String[]{"InUse", "Idle", "Down"};
                for (int i = 0; i < distribution.length; i++) {

                    // Adding a slice with its values and name to the Pie Chart
                    distributionSeries.add(status[i], distribution[i]);
                    //distributionSeries.add(status[i], 1);
                }
                defaultRenderer.setChartTitle(date);
                mChartView.repaint();
            }
        }


    }
    public void drawPie( int position, String data )
    {
        View view = mViewPager.getChildAt(position);

        // Pie Chart Slice Names
        final String[] status = new String[] { "InUse", "Idle", "Down" };
        String[] headdat = data.split("\n");
        String [] header =   headdat[0].split(",");
        String[] vals = headdat[1].split(",");
        String date=header[1];
        Double dInUse=Double.parseDouble(vals[0]);
        Double dIdle=Double.parseDouble(vals[1]);
        Double dDown=Double.parseDouble(vals[2]);

        // Pie Chart Slice Values
        double[] distribution = { dInUse, dIdle, dDown };

        // Color of each Pie Chart Slices
        int[] colors = { Color.GREEN, Color.YELLOW, Color.RED };

        // Instantiating CategorySeries to plot Pie Chart
        distributionSeries = new CategorySeries(" Usage ");


        for (int i = 0; i < distribution.length; i++) {

            // Adding a slice with its values and name to the Pie Chart
            distributionSeries.add(status[i], distribution[i]);
        }


        // Instantiating a renderer for the Pie Chart
        defaultRenderer = new DefaultRenderer();
        for (int i = 0; i < distribution.length; i++) {
            SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
            seriesRenderer.setColor(colors[i]);

            //seriesRenderer.setDisplayChartValues(true);

            // Adding the renderer of a slice to the renderer of the pie chart
            defaultRenderer.addSeriesRenderer(seriesRenderer);
        }

        defaultRenderer.setChartTitle(date);
        defaultRenderer.setChartTitleTextSize(40);
        defaultRenderer.setZoomButtonsVisible(true);
        defaultRenderer.setLegendTextSize(40);
        defaultRenderer.setLabelsTextSize(40);

        ViewGroup chartContainer = (ViewGroup)view.findViewById(R.id.chart);

        mChartView = ChartFactory.getPieChartView(this,
                distributionSeries, defaultRenderer);

        // Adding the pie chart to the custom layout
        chartContainer.addView(mChartView);

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
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
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
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Util";
                case 1:
                    return "Top 10 jobs";
                case 2:
                    return "Backfill";
            }
            return null;
        }
    }

}
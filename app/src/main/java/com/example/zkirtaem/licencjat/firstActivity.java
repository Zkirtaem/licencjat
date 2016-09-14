package com.example.zkirtaem.licencjat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.*;

import android.widget.TextView;

public class firstActivity extends AppCompatActivity {


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

    Context appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        setTitle("Obecności");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        appContext=getApplicationContext();

        /*
        //POZWOLENIA
        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        Log.i("POZWOLENIE NA ZAPIS:",""+permissionCheck);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED) {

            if (!(ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        2);
            }
        }

        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            Log.v("ZAPIS","Permission is granted");
        }
*/
    }

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
            return 5;
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
                    return "SECTION 4";
                case 4:
                    return "SECTION 5";
            }
            return null;
        }
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

        public PlaceholderFragment() {
        }



        //Load a bitmap from a resource with a target size
        static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(res, resId, options);
            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeResource(res, resId, options);
        }

        //Given the bitmap size and View size calculate a subsampling size (powers of 2)
        static int calculateInSampleSize( BitmapFactory.Options options, int reqWidth, int reqHeight) {
            int inSampleSize = 1;	//Default subsampling size
            // See if image raw height and width is bigger than that of required view
            if (options.outHeight > reqHeight || options.outWidth > reqWidth) {
                //bigger
                final int halfHeight = options.outHeight / 2;
                final int halfWidth = options.outWidth / 2;
                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while ((halfHeight / inSampleSize) > reqHeight
                        && (halfWidth / inSampleSize) > reqWidth) {
                    inSampleSize *= 2;
                }
            }
            return inSampleSize;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_first, container, false);
            final TextView cat_title = (TextView) rootView.findViewById(R.id.cat_title);
            TextView cat_subtitle = (TextView) rootView.findViewById(R.id.cat_subtitle);
            ImageView cat_img = (ImageView) rootView.findViewById(R.id.cat_img);

            WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();

            Point disp_dim=new Point();
            display.getSize(disp_dim);
            int dispx=disp_dim.x/5;
            int dispy=disp_dim.x/5;
            Log.i("res", "resx:"+dispx+",resy:"+dispy);


            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                cat_img.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.tlo01, dispx, dispy));
                cat_title.setText("Rozpocznij zajęcia");
                cat_subtitle.setText("Otwórz nową listę obecności dla jednych z zajęć dodanych w programie");


                rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(cat_title.getContext(), zajeciaRozpoczeteActivity.class);
                        startActivity(i);
                    }
                });
            }
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
                cat_img.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.tlo02, dispx, dispy));
                cat_title.setText("Dodaj studenta");
                cat_subtitle.setText("Wprowadź dane osobowe nowego uczestnika wykładu i przypisz mu identyfikator ELS");

                rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(cat_title.getContext(), addStudentActivity.class);
                        startActivity(i);
                    }
                });
            }

            if (getArguments().getInt(ARG_SECTION_NUMBER) == 3) {
                cat_img.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.tlo03, dispx, dispy));
                cat_title.setText("Obecności");
                cat_subtitle.setText("Sprawdź listę obecności jednych z odbytych zajęć");

                rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(cat_title.getContext(), listObecnosciActivity.class);
                        startActivity(i);
                    }
                });
            }
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 4) {
                cat_img.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.tlo04, dispx, dispy));
                cat_title.setText("Dodaj nowe zajęcia");
                cat_subtitle.setText("Dodaj nowe zajęcia, dla których zostanie utworzona oddzielna lista obecności");

                rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(cat_title.getContext(), addZajeciaActivity.class);
                        startActivity(i);
                    }
                });
            }
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 5) {
                cat_img.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.tlo05, dispx, dispy));
                cat_title.setText("Ustawienia");
                cat_subtitle.setText("Spersonalizuj interfejs programu lub wyczyść istniejącą bazę danych");
                rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(cat_title.getContext(), settingsActivity.class);
                        startActivity(i);
                    }
                });
            }
            return rootView;
        }
    }
}

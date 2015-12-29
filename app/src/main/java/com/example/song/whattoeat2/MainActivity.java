package com.example.song.whattoeat2;

import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.song.whattoeat2.fragment.AddGroupDialog;
import com.example.song.whattoeat2.fragment.AddRestaurantDialog;
import com.example.song.whattoeat2.fragment.Groups;
import com.example.song.whattoeat2.fragment.Home;
import com.example.song.whattoeat2.fragment.Restaurants;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    //private FloatingActionButton mFab;
    private TabLayout mTabLayout;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

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
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);
        //mFab = (FloatingActionButton) findViewById(R.id.fab);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public enum Section {
        HOME, GROUPS, RESTAURANTS;
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
            Fragment fragment = null;
            switch(Section.values()[position]) {
                case HOME:
                    return Home.newInstance(position);
                case GROUPS:
                    return Groups.newInstance(position);
                case RESTAURANTS:
                    return Restaurants.newInstance(position);
                default:
                    return null;
            }
        }
        @Override
        public int getCount() {
            return Section.values().length;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            switch (Section.values()[position]) {
                case HOME:
                    return getString(R.string.tab_title_home);
                case GROUPS:
                    return getString(R.string.tab_title_groups);
                case RESTAURANTS:
                    return getString(R.string.tab_title_restaurants);
                default:
                    return null;
            }
        }
    }
}

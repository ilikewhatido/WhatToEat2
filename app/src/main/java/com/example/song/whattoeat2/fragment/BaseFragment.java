package com.example.song.whattoeat2.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.song.whattoeat2.R;
import com.example.song.whattoeat2.database.DBAdapter;

/**
 * Created by Song on 2015/12/29.
 */
public abstract class BaseFragment extends Fragment {

    public static final String ARG_SECTION_NUMBER = "section_number";

    protected DBAdapter dbAdapter;
    protected Menu menu;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        dbAdapter = new DBAdapter(getActivity());
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_action_mode, menu);
        this.menu = menu;
    }

    public abstract boolean closeActionMode();
}

package com.example.song.whattoeat2.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.song.whattoeat2.R;

/**
 * Created by Song on 2015/12/29.
 */
public class Groups extends BaseFragment {

    public static Groups newInstance(int sectionNumber) {
        Groups fragment = new Groups();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_groups, container, false);
    }
}

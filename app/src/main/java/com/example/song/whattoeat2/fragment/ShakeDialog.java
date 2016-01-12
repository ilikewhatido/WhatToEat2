package com.example.song.whattoeat2.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.example.song.whattoeat2.R;
import com.example.song.whattoeat2.database.DBAdapter;
import com.example.song.whattoeat2.database.Restaurant;
import com.example.song.whattoeat2.widget.WheelView;
import com.example.song.whattoeat2.widget.adapters.AbstractWheelTextAdapter;

import java.util.List;
import java.util.Random;

/**
 * Created by Song on 2016/1/12.
 */
public class ShakeDialog extends DialogFragment {

    public static final String TAG = "dialog_shake";

    private static final int SHAKER_SIZE = 5;
    private ShakerAdapter mShakerAdapter;
    private DBAdapter mDb;
    private WheelView mShaker;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mDb = new DBAdapter(getActivity());
        View view = inflater.inflate(R.layout.dialog_shake, null);
        mShakerAdapter = new ShakerAdapter(getActivity(), mDb.getRestaurants());
        mShaker = (WheelView) view.findViewById(R.id.shaker);
        mShaker.setVisibleItems(SHAKER_SIZE);
        mShaker.setViewAdapter(mShakerAdapter);
        mShaker.setCyclic(true);
        // Create dialog without title
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        Button button = (Button) view.findViewById(R.id.roll);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roll(v);
            }
        });

        return view;
    }

    public void roll(View v) {
        final Random rand = new Random();
        int r = rand.nextInt(mShakerAdapter.getItemsCount());
        mShaker.scroll(-r-40 , 4000);
    }

    private class ShakerAdapter extends AbstractWheelTextAdapter {
        private List<Restaurant> mData;

        public ShakerAdapter(Context context, List<Restaurant> data) {
            super(context, R.layout.row_shaker_restaurant, R.id.shaker_row_restaurant_name);
            mData = data;
        }

        @Override
        public int getItemsCount() {
            return mData.size();
        }

        @Override
        protected CharSequence getItemText(int index) {
            return mData.get(index).getName();
        }
    }
}

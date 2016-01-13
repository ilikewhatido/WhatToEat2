package com.example.song.whattoeat2.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.song.whattoeat2.EditRestaurantActivity;
import com.example.song.whattoeat2.R;
import com.example.song.whattoeat2.RestaurantByGroupActivity;
import com.example.song.whattoeat2.database.Restaurant;

import java.util.List;

public class RestaurantFragment extends BaseFragment implements RecyclerViewClickListener {

    public static final String BUNDLE_RESTAURANT_ID = "bundle_restaurant_id";

    private RecyclerView mRestaurantsRecyclerView;
    private RestaurantAdapter mRestaurantAdapter;

    private boolean mSelectionMode = false;
    private MenuItem mAddButton;
    private Toolbar mToolbar;

    public static RestaurantFragment newInstance(int sectionNumber) {
        RestaurantFragment fragment = new RestaurantFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_restaurants, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
    }

    @Override
    public void onResume() {
        super.onResume();
        mRestaurantsRecyclerView = (RecyclerView) getActivity().findViewById(R.id.fragment_restaurants_list);
        mRestaurantAdapter = new RestaurantAdapter(dbAdapter.getRestaurants(), this);
        mRestaurantsRecyclerView.setAdapter(mRestaurantAdapter);
        mRestaurantsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRestaurantsRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        mAddButton = menu.findItem(R.id.menu_action_mode_action);
        mAddButton.setTitle("新增");
        mAddButton.setIcon(R.drawable.plus);
    }

    @Override
    public boolean closeActionMode() {
        if(mSelectionMode) {
            closeSelection();
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_action_mode_action:
                if(!mSelectionMode) {
                    //Adding new restaurant
                    AddRestaurantDialog dialog = new AddRestaurantDialog();
                    dialog.setTargetFragment(this, 0);
                    dialog.show(getActivity().getSupportFragmentManager(), AddRestaurantDialog.TAG);
                    return true;
                } else {
                    // Deleting selected restaurants
                    dbAdapter.removeRestaurantsById(mRestaurantAdapter.getSelectedItemIds());
                    updateUI();
                    closeSelection();
                }
                return true;
            case android.R.id.home:
                closeSelection();
                return true;
            default:
                return true;
        }
    }

    @Override
    public void onItemClicked(int position) {
        if (!mSelectionMode) {
            Bundle extra = new Bundle();
            extra.putLong(BUNDLE_RESTAURANT_ID, mRestaurantAdapter.getItemId(position));
            Intent intent = new Intent(getActivity(), EditRestaurantActivity.class);
            intent.putExtras(extra);
            startActivity(intent);
        } else {
            toggleSelection(position);
        }
    }

    @Override
    public boolean onItemLongClicked(int position) {
        if (!mSelectionMode) {
            openSelection();
        }
        toggleSelection(position);
        return true;
    }

    public long addRestaurant(Restaurant restaurant) {
        return dbAdapter.addRestaurant(restaurant);
    }

    public void updateUI() {
        mRestaurantAdapter.update(dbAdapter.getRestaurants());
    }

    private void openSelection() {
        mSelectionMode = true;
        mAddButton.setTitle("刪除");
        mAddButton.setIcon(R.drawable.delete);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void closeSelection() {
        mRestaurantAdapter.clearSelection();
        mSelectionMode = false;
        mAddButton.setTitle("新增");
        mAddButton.setIcon(R.drawable.plus);
        mToolbar.setTitle("WhatToEat2");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    private void toggleSelection(int position) {
        mRestaurantAdapter.toggleSelection(position);
        int total = mRestaurantAdapter.getItemCount();
        int count = mRestaurantAdapter.getSelectedItemCount();
        if (count == 0) {
            closeSelection();
        } else {
            mToolbar.setTitle(count + "/" + total);
        }
    }
}
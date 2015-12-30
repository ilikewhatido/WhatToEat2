package com.example.song.whattoeat2.fragment;

import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.song.whattoeat2.R;
import com.example.song.whattoeat2.database.Restaurant;

public class Restaurants extends BaseFragment {

    private RecyclerView mRestaurantsRecyclerView;
    private RestaurantAdapter mRestaurantAdapter;

    public static Restaurants newInstance(int sectionNumber) {
        Restaurants fragment = new Restaurants();
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
        mRestaurantsRecyclerView = (RecyclerView) getActivity().findViewById(R.id.fragment_restaurants_list);

        // TODO
        // https://www.bignerdranch.com/blog/recyclerview-part-2-choice-modes/
        mRestaurantAdapter = new RestaurantAdapter(mDBAdapter.getRestaurants());
        mRestaurantsRecyclerView.setAdapter(mRestaurantAdapter);
        mRestaurantsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRestaurantsRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_add_item:
                AddRestaurantDialog dialog = new AddRestaurantDialog();
                dialog.setTargetFragment(this, 0);
                dialog.show(getActivity().getSupportFragmentManager(), AddRestaurantDialog.TAG);
                return true;
            default:
                return true;
        }
    }
    public long addRestaurant(Restaurant restaurant) {
        return mDBAdapter.addRestaurant(restaurant);
    }
    public void updateUI() {
        mRestaurantAdapter.update(mDBAdapter.getRestaurants());
    }
}

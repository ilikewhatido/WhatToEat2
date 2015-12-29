package com.example.song.whattoeat2.fragment;

import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.song.whattoeat2.R;
import com.example.song.whattoeat2.database.DBAdapter;

public class Restaurants extends BaseFragment {

    private ListView mRestaurantsListView;

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
        mRestaurantsListView = (ListView) getActivity().findViewById(R.id.fragment_restaurants_list);
        mRestaurantsListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                getActivity(),
                R.layout.row_restaurant,
                mDBAdapter.getRestaurants(),
                new String[] { DBAdapter.RESTAURANT_NAME, DBAdapter.RESTAURANT_NUMBER },
                new int[] { R.id.row_restaurant_name, R.id.row_restaurant_number }
        );
        mRestaurantsListView.setAdapter(adapter);
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
    public long addRestaurant(String name, String number) {
        return mDBAdapter.addRestaurant(name, number);
    }
    public void updateUI() {
        SimpleCursorAdapter adapter = (SimpleCursorAdapter) mRestaurantsListView.getAdapter();
        adapter.changeCursor(mDBAdapter.getRestaurants());
        adapter.notifyDataSetChanged();
    }
}

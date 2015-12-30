package com.example.song.whattoeat2.fragment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.song.whattoeat2.R;
import com.example.song.whattoeat2.database.Restaurant;

public class RestaurantFragment extends BaseFragment implements RestaurantAdapter.ClickListener {

    private RecyclerView mRestaurantsRecyclerView;
    private RestaurantAdapter mRestaurantAdapter;
    private ActionMode mActionMode;
    private ActionModeCallback mActionModeCallback;

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
        mRestaurantsRecyclerView = (RecyclerView) getActivity().findViewById(R.id.fragment_restaurants_list);

        // TODO
        // https://www.bignerdranch.com/blog/recyclerview-part-2-choice-modes/
        mRestaurantAdapter = new RestaurantAdapter(mDBAdapter.getRestaurants(), this);
        mRestaurantsRecyclerView.setAdapter(mRestaurantAdapter);
        mRestaurantsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRestaurantsRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        mActionModeCallback = new ActionModeCallback();
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

    @Override
    public void onItemClicked(int position) {
        if(mActionMode == null) {
            //TODO
            // Not in action mode... do the normal thing
        } else {
            toggleSelection(position);
        }
    }

    @Override
    public boolean onItemLongClicked(int position) {
        if (mActionMode == null) {
            mActionMode = ((AppCompatActivity)getActivity()).startSupportActionMode(mActionModeCallback);
        }
        toggleSelection(position);
        return true;
    }

    private void toggleSelection(int position) {
        mRestaurantAdapter.toggleSelection(position);
        int total = mRestaurantAdapter.getItemCount();
        int count = mRestaurantAdapter.getSelectedItemCount();
        if (count == 0) {
            mActionMode.finish();
        } else {
            mActionMode.setTitle(count + "/" + total);
            mActionMode.invalidate();
        }
    }

    private class ActionModeCallback implements ActionMode.Callback {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate (R.menu.menu_remove, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_remove_remove_item:
                    //TODO
                    mRestaurantAdapter.getSelectedItems();
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mRestaurantAdapter.clearSelection();
            mActionMode = null;
        }
    }
}

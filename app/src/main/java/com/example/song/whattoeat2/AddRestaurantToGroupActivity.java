package com.example.song.whattoeat2;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.song.whattoeat2.fragment.RecyclerViewClickListener;
import com.example.song.whattoeat2.fragment.RestaurantAdapter;
import com.example.song.whattoeat2.fragment.SimpleDividerItemDecoration;

import java.util.List;

public class AddRestaurantToGroupActivity extends BaseActivity implements RecyclerViewClickListener {

    private RecyclerView mRestaurantsRecyclerView;
    private RestaurantAdapter mRestaurantAdapter;

    private ActionMode mActionMode;
    private ActionModeCallback mActionModeCallback;

    private long groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restaurant_to_group);
        setUpToolbar();
        setUpRecyclerView();

        groupId = getIntent().getExtras().getLong(RestaurantByGroupActivity.BUNDLE_GROUP_ID);

        Log.e("wawawa", "groupId=" + groupId);


        mActionModeCallback = new ActionModeCallback();
    }

    private void setUpToolbar() {
        Bundle bundle = getIntent().getExtras();
        String groupName = bundle.getString(RestaurantByGroupActivity.BUNDLE_GROUP_NAME);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setSubtitle("Adding restaurants to " + groupName);
    }

    private void setUpRecyclerView() {
        Bundle bundle = getIntent().getExtras();
        long groupId = bundle.getLong(RestaurantByGroupActivity.BUNDLE_GROUP_ID);
        mRestaurantsRecyclerView = (RecyclerView) findViewById(R.id.add_restaurant_to_group_list);
        mRestaurantAdapter = new RestaurantAdapter(mDBAdapter.getRestaurantsNotInGroup(groupId), this);
        mRestaurantsRecyclerView.setAdapter(mRestaurantAdapter);
        mRestaurantsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRestaurantsRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return true;
        }
    }

    @Override
    public void onItemClicked(int position) {
        if (mActionMode == null) {
            //TODO
            // Not in action mode... do the normal thing
        } else {
            toggleSelection(position);
        }
    }

    @Override
    public boolean onItemLongClicked(int position) {
        if (mActionMode == null) {
            mActionMode = startSupportActionMode(mActionModeCallback);
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
            mode.getMenuInflater().inflate(R.menu.menu_add, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_add_add_item:
                    for(Long restaurantId : mRestaurantAdapter.getSelectedItemIds()) {
                        mDBAdapter.addRestaurantToGroupById(restaurantId, groupId);
                    }
                    mode.finish();
                    finish();
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

package com.example.song.whattoeat2;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.example.song.whattoeat2.fragment.RecyclerViewClickListener;
import com.example.song.whattoeat2.fragment.RestaurantAdapter;
import com.example.song.whattoeat2.fragment.SimpleDividerItemDecoration;

public class AddRestaurantToGroupActivity extends BaseActivity implements RecyclerViewClickListener {

    private RecyclerView mRestaurantsRecyclerView;
    private RestaurantAdapter mRestaurantAdapter;
    private Menu mMenu;
    private MenuItem mAddButton;
    private boolean mSelectionMode = false;
    private Toolbar mToolbar;
    private long groupId;
    private String groupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restaurant_to_group);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_action_mode, menu);
        mMenu = menu;
        mAddButton = mMenu.findItem(R.id.menu_action_mode_action);
        mAddButton.setTitle("加入");
        mAddButton.setIcon(android.R.drawable.ic_menu_add);
        mAddButton.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_action_mode_action:
                for (Long restaurantId : mRestaurantAdapter.getSelectedItemIds()) {
                    dbAdapter.addRestaurantToGroupById(restaurantId, groupId);
                }
                finish();
                return true;
            // Home back button
            case android.R.id.home:
                if (mSelectionMode) {
                    closeSelection();
                } else {
                    finish();
                }
                return true;
            default:
                return true;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mSelectionMode) {
            closeSelection();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onItemClicked(int position) {
        if (!mSelectionMode) {
            openSelection();
        }
        toggleSelection(position);
    }

    @Override
    public boolean onItemLongClicked(int position) {
        return false;
    }

    private void openSelection() {
        mSelectionMode = true;
        mAddButton.setVisible(true);
    }

    private void closeSelection() {
        mRestaurantAdapter.clearSelection();
        mSelectionMode = false;
        mAddButton.setVisible(false);
        mToolbar.setTitle("群組 > " + groupName);
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

    private void init() {
        groupId = getIntent().getExtras().getLong(RestaurantByGroupActivity.BUNDLE_GROUP_ID);
        groupName = getIntent().getExtras().getString(RestaurantByGroupActivity.BUNDLE_GROUP_NAME);

        //Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("群組 > " + groupName);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // RecyclerView
        mRestaurantsRecyclerView = (RecyclerView) findViewById(R.id.add_restaurant_to_group_list);
        mRestaurantAdapter = new RestaurantAdapter(dbAdapter.getRestaurantsNotInGroup(groupId), this);
        mRestaurantsRecyclerView.setAdapter(mRestaurantAdapter);
        mRestaurantsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRestaurantsRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
    }
}
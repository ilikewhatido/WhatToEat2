package com.example.song.whattoeat2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.song.whattoeat2.fragment.GroupFragment;
import com.example.song.whattoeat2.fragment.RecyclerViewClickListener;
import com.example.song.whattoeat2.fragment.RestaurantAdapter;
import com.example.song.whattoeat2.fragment.SimpleDividerItemDecoration;

public class RestaurantByGroupActivity extends BaseActivity implements RecyclerViewClickListener {

    public static final String BUNDLE_GROUP_ID = "bundle_group_id";
    public static final String BUNDLE_GROUP_NAME = "bundle_group_name";

    private RecyclerView mRestaurantsRecyclerView;
    private RestaurantAdapter mRestaurantAdapter;
    private long groupId;
    private String groupName;

    private ActionMode mActionMode;
    private ActionModeCallback mActionModeCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurany_by_group);
        groupId = getIntent().getExtras().getLong(GroupFragment.BUNDLE_GROUP_ID);
        groupName = getIntent().getExtras().getString(GroupFragment.BUNDLE_GROUP_NAME);
        mActionModeCallback = new ActionModeCallback();
        setupToolbar();
        setupTitle();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpRecyclerView();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void setupTitle() {
        TextView title = (TextView) findViewById(R.id.restaurant_by_group_title);
        title.setText("群組 > " + groupName);
    }

    private void setUpRecyclerView() {
        mRestaurantsRecyclerView = (RecyclerView) findViewById(R.id.restaurant_by_group_list);
        mRestaurantAdapter = new RestaurantAdapter(mDBAdapter.getRestaurantsByGroupId(groupId), this);
        mRestaurantsRecyclerView.setAdapter(mRestaurantAdapter);
        mRestaurantsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRestaurantsRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_add_item:
                Bundle extra = new Bundle();
                extra.putLong(BUNDLE_GROUP_ID, groupId);
                extra.putString(BUNDLE_GROUP_NAME, groupName);
                Intent intent = new Intent(this, AddRestaurantToGroupActivity.class);
                intent.putExtras(extra);
                startActivity(intent);
                return true;
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

    private void toggleSelection(int position) {
        mRestaurantAdapter.toggleSelection(position);
        int total = mRestaurantAdapter.getItemCount();
        int count = mRestaurantAdapter.getSelectedItemCount();
        if (count == 0) {
            mActionMode.finish();
        } else {
            mActionMode.setTitle(count + "/" + total + "  加入群組");
            mActionMode.invalidate();
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

    private class ActionModeCallback implements ActionMode.Callback {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_remove, menu);
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
                    mDBAdapter.removeRestaurantFromGroupById(mRestaurantAdapter.getSelectedItemIds(), groupId);
                    mRestaurantAdapter.update(mDBAdapter.getRestaurantsByGroupId(groupId));
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

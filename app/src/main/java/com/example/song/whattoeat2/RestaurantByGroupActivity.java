package com.example.song.whattoeat2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.song.whattoeat2.fragment.GroupFragment;
import com.example.song.whattoeat2.fragment.RecyclerViewClickListener;
import com.example.song.whattoeat2.fragment.RestaurantAdapter;
import com.example.song.whattoeat2.fragment.ShakeDialog;
import com.example.song.whattoeat2.fragment.SimpleDividerItemDecoration;

public class RestaurantByGroupActivity extends BaseActivity implements RecyclerViewClickListener {

    public static final String BUNDLE_GROUP_ID = "bundle_group_id";
    public static final String BUNDLE_GROUP_NAME = "bundle_group_name";

    private RecyclerView mRestaurantsRecyclerView;
    private RestaurantAdapter mRestaurantAdapter;
    private long groupId;
    private String groupName;

    private boolean mSelectionMode = false;
    private MenuItem mAddButton;
    private MenuItem mDiceButton;
    private Menu mMenu;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurany_by_group);
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_action_mode, menu);
        mMenu = menu;
        mAddButton = mMenu.findItem(R.id.menu_action_mode_action);
        mAddButton.setTitle("加入餐廳至群組");
        mAddButton.setIcon(R.drawable.plus);

        mDiceButton = mMenu.findItem(R.id.menu_action_mode_dice);
        mDiceButton.setVisible(true);
        mDiceButton.setTitle("Shake!");
        mDiceButton.setIcon(R.drawable.dice);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_action_mode_action:
                if (!mSelectionMode) {
                    Bundle extra = new Bundle();
                    extra.putLong(BUNDLE_GROUP_ID, groupId);
                    extra.putString(BUNDLE_GROUP_NAME, groupName);
                    Intent intent = new Intent(this, AddRestaurantToGroupActivity.class);
                    intent.putExtras(extra);
                    startActivity(intent);
                } else {
                    // Delete selected items
                    dbAdapter.removeRestaurantFromGroupById(mRestaurantAdapter.getSelectedItemIds(), groupId);
                    mRestaurantAdapter.update(dbAdapter.getRestaurantsByGroupId(groupId));
                    closeSelection();
                }
                return true;
            // Home back button
            case android.R.id.home:
                if (mSelectionMode) {
                    closeSelection();
                } else {
                    finish();
                }
                return true;
            case R.id.menu_action_mode_dice:
                ShakeDialog shakeDialog = new ShakeDialog();
                Bundle bundle = new Bundle();
                bundle.putLong(BUNDLE_GROUP_ID, groupId);
                if(dbAdapter.getRestaurantsByGroupId(groupId).size() == 0) {
                    Toast.makeText(this, "該群組無餐廳", Toast.LENGTH_SHORT).show();
                } else {
                    shakeDialog.setArguments(bundle);
                    shakeDialog.show(getSupportFragmentManager(), ShakeDialog.TAG);
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
        if (mSelectionMode) {
            toggleSelection(position);
        } else {
            // Not in action mode... do the normal thing
            //TODO
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

    private void openSelection() {
        mSelectionMode = true;
        mAddButton.setTitle("刪除");
        mAddButton.setIcon(R.drawable.delete);
    }

    private void closeSelection() {
        mRestaurantAdapter.clearSelection();
        mSelectionMode = false;
        mAddButton.setTitle("加入餐廳");
        mAddButton.setIcon(R.drawable.plus);
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

    public void init() {
        groupId = getIntent().getExtras().getLong(GroupFragment.BUNDLE_GROUP_ID);
        groupName = getIntent().getExtras().getString(GroupFragment.BUNDLE_GROUP_NAME);

        // Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("群組 > " + groupName);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //RecyclerView
        mRestaurantsRecyclerView = (RecyclerView) findViewById(R.id.restaurant_by_group_list);
        mRestaurantAdapter = new RestaurantAdapter(dbAdapter.getRestaurantsByGroupId(groupId), this);
        mRestaurantsRecyclerView.setAdapter(mRestaurantAdapter);
        mRestaurantsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRestaurantsRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
    }
}

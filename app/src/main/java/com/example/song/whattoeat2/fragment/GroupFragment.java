package com.example.song.whattoeat2.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.song.whattoeat2.R;
import com.example.song.whattoeat2.RestaurantByGroupActivity;
import com.example.song.whattoeat2.database.Group;

public class GroupFragment extends BaseFragment implements RecyclerViewClickListener {

    public static final String BUNDLE_GROUP_ID = "bundle_group_id";
    public static final String BUNDLE_GROUP_NAME = "bundle_group_name";

    private RecyclerView mGroupsRecyclerView;
    private GroupAdapter mGroupAdapter;
    private boolean mSelectionMode = false;
    private MenuItem mAddButton;
    private Toolbar mToolbar;

    public static GroupFragment newInstance(int sectionNumber) {
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        GroupFragment fragment = new GroupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_groups, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mGroupAdapter = new GroupAdapter(dbAdapter.getGroups(), this);
        mGroupsRecyclerView = (RecyclerView) getActivity().findViewById(R.id.fragment_groups_list);
        mGroupsRecyclerView.setAdapter(mGroupAdapter);
        mGroupsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mGroupsRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
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
                //TODO
                // 新增
                if (!mSelectionMode) {
                    AddGroupDialog dialog = new AddGroupDialog();
                    dialog.setTargetFragment(this, 0);
                    dialog.show(getActivity().getSupportFragmentManager(), AddGroupDialog.TAG);
                    return true;
                    // 刪除
                } else {
                    dbAdapter.removeGroupsById(mGroupAdapter.getSelectedItemIds());
                    updateUI();
                    closeSelection();
                }
                return true;
            // Home back button
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
            //TODO
            // Not in action mode... open new activity
            Bundle extra = new Bundle();
            extra.putLong(BUNDLE_GROUP_ID, mGroupAdapter.getItemId(position));
            extra.putString(BUNDLE_GROUP_NAME, mGroupAdapter.getItemName(position));
            Intent intent = new Intent(getActivity(), RestaurantByGroupActivity.class);
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

    public long addGroup(Group group) {
        return dbAdapter.addGroup(group);
    }

    public void updateUI() {
        mGroupAdapter.update(dbAdapter.getGroups());
    }

    private void openSelection() {
        mSelectionMode = true;
        mAddButton.setTitle("刪除");
        mAddButton.setIcon(R.drawable.delete);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void closeSelection() {
        mGroupAdapter.clearSelection();
        mSelectionMode = false;
        mAddButton.setTitle("新增");
        mAddButton.setIcon(R.drawable.plus);
        mToolbar.setTitle("WhatToEat2");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    private void toggleSelection(int position) {
        mGroupAdapter.toggleSelection(position);
        int total = mGroupAdapter.getItemCount();
        int count = mGroupAdapter.getSelectedItemCount();
        if (count == 0) {
            closeSelection();
        } else {
            mToolbar.setTitle(count + "/" + total);
        }
    }
}
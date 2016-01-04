package com.example.song.whattoeat2.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.song.whattoeat2.R;
import com.example.song.whattoeat2.RestaurantByGroupActivity;
import com.example.song.whattoeat2.database.Group;

public class GroupFragment extends BaseFragment implements RecyclerViewClickListener {

    private RecyclerView mGroupsRecyclerView;
    private GroupAdapter mGroupAdapter;
    private ActionMode mActionMode;
    private ActionModeCallback mActionModeCallback;

    public static final String BUNDLE_GROUP_ID = "bundle_group_id";
    public static final String BUNDLE_GROUP_NAME = "bundle_group_name";

    public static GroupFragment newInstance(int sectionNumber) {
        GroupFragment fragment = new GroupFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
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
        mGroupAdapter = new GroupAdapter(mDBAdapter.getGroups(), this);
        mGroupsRecyclerView = (RecyclerView) getActivity().findViewById(R.id.fragment_groups_list);
        mGroupsRecyclerView.setAdapter(mGroupAdapter);
        mGroupsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mGroupsRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        mActionModeCallback = new ActionModeCallback();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_add_item:
                AddGroupDialog dialog = new AddGroupDialog();
                dialog.setTargetFragment(this, 0);
                dialog.show(getActivity().getSupportFragmentManager(), AddGroupDialog.TAG);
                return true;
            default:
                return true;
        }
    }

    public long addGroup(Group group) {
        return mDBAdapter.addGroup(group);
    }

    public void updateUI() {
        mGroupAdapter.update(mDBAdapter.getGroups());
    }

    @Override
    public void onItemClicked(int position) {
        if (mActionMode == null) {
            //TODO
            // Not in action mode... do the normal thing
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
        if (mActionMode == null) {
            mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(mActionModeCallback);
        }
        toggleSelection(position);
        return true;
    }

    private void toggleSelection(int position) {
        mGroupAdapter.toggleSelection(position);
        int total = mGroupAdapter.getItemCount();
        int count = mGroupAdapter.getSelectedItemCount();
        if (count == 0) {
            mActionMode.finish();
        } else {
            mActionMode.setTitle(count + "/" + total);
            mActionMode.invalidate();
        }
    }

    @Override
    public void closeActionMode() {
        if(mActionMode != null) {
            mActionMode.finish();
        }
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
                    //TODO
                    mGroupAdapter.getSelectedItems();
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mGroupAdapter.clearSelection();
            mActionMode = null;
        }
    }
}

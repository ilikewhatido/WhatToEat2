package com.example.song.whattoeat2.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.song.whattoeat2.R;
import com.example.song.whattoeat2.database.Group;

public class GroupFragment extends BaseFragment {

    private RecyclerView mGroupsRecyclerView;
    private GroupAdapter mGroupAdapter;

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
        mGroupAdapter = new GroupAdapter(mDBAdapter.getGroups());
        mGroupsRecyclerView = (RecyclerView) getActivity().findViewById(R.id.fragment_groups_list);
        mGroupsRecyclerView.setAdapter(mGroupAdapter);
        mGroupsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mGroupsRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
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
}

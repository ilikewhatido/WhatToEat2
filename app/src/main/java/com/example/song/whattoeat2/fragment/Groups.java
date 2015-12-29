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

public class Groups extends BaseFragment {

    private ListView mGroupsListView;

    public static Groups newInstance(int sectionNumber) {
        Groups fragment = new Groups();
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
        mGroupsListView = (ListView) getActivity().findViewById(R.id.fragment_groups_list);
        mGroupsListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                getActivity(),
                R.layout.row_group,
                mDBAdapter.getGroups(),
                new String[] { DBAdapter.GROUP_NAME},
                new int[] { R.id.row_group_name }
        );
        mGroupsListView.setAdapter(adapter);
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
    public long addGroup(String name) {
        return mDBAdapter.addGroup(name);
    }
    public void updateUI() {
        SimpleCursorAdapter adapter = (SimpleCursorAdapter) mGroupsListView.getAdapter();
        adapter.changeCursor(mDBAdapter.getGroups());
        adapter.notifyDataSetChanged();
    }
}

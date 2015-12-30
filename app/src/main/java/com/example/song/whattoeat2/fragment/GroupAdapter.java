package com.example.song.whattoeat2.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.song.whattoeat2.R;
import com.example.song.whattoeat2.database.Group;

import java.util.List;

/**
 * Created by Song on 2015/12/29.
 */
public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {

    private List<Group> mGroups;

    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mName;
        public ViewHolder(View v) {
            super(v);
            mName = (TextView) v.findViewById(R.id.row_group_name);
        }
    }
    public GroupAdapter(List<Group> mGroups) {
        this.mGroups = mGroups;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_group, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String name = mGroups.get(position).getName();
        holder.mName.setText(name);
    }
    @Override
    public int getItemCount() {
        return mGroups.size();
    }
    public void update(List<Group> groups) {
        mGroups = groups;
        notifyDataSetChanged();
    }
}

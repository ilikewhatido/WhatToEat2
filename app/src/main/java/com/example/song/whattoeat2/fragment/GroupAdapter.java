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
public class GroupAdapter extends SelectableAdapter<GroupAdapter.ViewHolder> {

    private List<Group> mGroups;
    private RecyclerViewClickListener listener;

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public TextView mName;
        private RecyclerViewClickListener listener;

        public ViewHolder(View itemView, RecyclerViewClickListener listener) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.row_group_name);
            this.listener = listener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClicked(getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (listener != null) {
                return listener.onItemLongClicked(getAdapterPosition());
            }
            return false;
        }
    }

    public GroupAdapter(List<Group> mGroups, RecyclerViewClickListener listener) {
        this.mGroups = mGroups;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_group, parent, false);
        return new ViewHolder(view, listener);
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

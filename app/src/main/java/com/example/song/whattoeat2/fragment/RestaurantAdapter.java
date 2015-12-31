package com.example.song.whattoeat2.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.song.whattoeat2.R;
import com.example.song.whattoeat2.database.Restaurant;

import java.util.List;

/**
 * Created by Song on 2015/12/29.
 */
public class RestaurantAdapter extends SelectableAdapter<RestaurantAdapter.ViewHolder> {

    private List<Restaurant> restaurants;
    private RecyclerViewClickListener listener;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public TextView name;
        public TextView number;
        //public View selectedOverlay;

        private RecyclerViewClickListener listener;

        public ViewHolder(View itemView, RecyclerViewClickListener listener) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.row_restaurant_name);
            number = (TextView) itemView.findViewById(R.id.row_restaurant_number);
            //selectedOverlay = itemView.findViewById(R.id.row_restaurant_selected_overlay);
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

    public RestaurantAdapter(List<Restaurant> mRestaurants, RecyclerViewClickListener listener) {
        this.restaurants = mRestaurants;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_restaurant, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String name = restaurants.get(position).getName();
        String number = restaurants.get(position).getNumber();
        holder.name.setText(name);
        holder.number.setText(number);

        // Highlight selected item
        Context context = ((Fragment)listener).getActivity();
        if(isSelected(position)) {
            holder.name.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            holder.number.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        } else {
            holder.name.setTextColor(ContextCompat.getColor(context, R.color.textColorPrimary));
            holder.number.setTextColor(ContextCompat.getColor(context, R.color.textColorSecondary));
        }
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    public void update(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
        notifyDataSetChanged();
    }
}

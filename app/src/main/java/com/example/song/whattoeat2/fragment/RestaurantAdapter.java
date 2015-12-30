package com.example.song.whattoeat2.fragment;

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
    private ClickListener listener;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView name;
        public TextView number;
        private ClickListener listener;

        public ViewHolder(View itemView, ClickListener listener) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.row_restaurant_name);
            number = (TextView) itemView.findViewById(R.id.row_restaurant_number);
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

    public RestaurantAdapter(List<Restaurant> mRestaurants, ClickListener listener) {
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
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    public void update(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
        notifyDataSetChanged();
    }

    public interface ClickListener {
        void onItemClicked(int position);
        boolean onItemLongClicked(int position);
    }
}

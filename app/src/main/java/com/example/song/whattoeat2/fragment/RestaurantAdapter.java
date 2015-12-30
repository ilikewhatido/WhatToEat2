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
public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {

    private List<Restaurant> mRestaurants;

    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mName;
        public TextView mNumber;
        public ViewHolder(View v) {
            super(v);
            mName = (TextView) v.findViewById(R.id.row_restaurant_name);
            mNumber = (TextView) v.findViewById(R.id.row_restaurant_number);
        }
    }
    public RestaurantAdapter(List<Restaurant> mRestaurants) {
        this.mRestaurants = mRestaurants;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_restaurant, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String name = mRestaurants.get(position).getName();
        String number = mRestaurants.get(position).getNumber();
        holder.mName.setText(name);
        holder.mNumber.setText(number);
    }
    @Override
    public int getItemCount() {
        return mRestaurants.size();
    }

    public void update(List<Restaurant> restaurants) {
        mRestaurants = restaurants;
        notifyDataSetChanged();
    }
}

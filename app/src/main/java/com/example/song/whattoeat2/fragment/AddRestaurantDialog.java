package com.example.song.whattoeat2.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.song.whattoeat2.R;
import com.example.song.whattoeat2.database.Restaurant;

public class AddRestaurantDialog extends DialogFragment implements View.OnClickListener {

    public static final String TAG = "dialog_add_restaurant";

    private Button mOk, mCancel;
    private EditText mName, mNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_restaurant, null);
        mOk = (Button) view.findViewById(R.id.dialog_add_restaurant_ok);
        mOk.setOnClickListener(this);
        mCancel = (Button) view.findViewById(R.id.dialog_add_restaurant_cancel);
        mCancel.setOnClickListener(this);
        mName = (EditText) view.findViewById(R.id.dialog_add_restaurant_name);
        mNumber = (EditText) view.findViewById(R.id.dialog_add_restaurant_number);
        setCancelable(true);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE); // dialog without title
        return view;
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.dialog_add_restaurant_ok) {
            RestaurantFragment fragment = ((RestaurantFragment) getTargetFragment());
            Restaurant restaurant = new Restaurant(mName.getText().toString(), mNumber.getText().toString());
            fragment.addRestaurant(restaurant);
            fragment.updateUI();
            dismiss();
        } else if (id == R.id.dialog_add_restaurant_cancel) {
            dismiss();
        }
    }
}

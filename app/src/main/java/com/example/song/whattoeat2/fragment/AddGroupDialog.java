package com.example.song.whattoeat2.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.song.whattoeat2.R;

public class AddGroupDialog extends DialogFragment implements View.OnClickListener {

    public static final String TAG = "dialog_add_group";

    private Button mOk, mCancel;
    private EditText mName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_group, null);
        mOk = (Button) view.findViewById(R.id.dialog_add_group_ok);
        mOk.setOnClickListener(this);
        mCancel = (Button) view.findViewById(R.id.dialog_add_group_cancel);
        mCancel.setOnClickListener(this);
        mName = (EditText) view.findViewById(R.id.dialog_add_group_name);
        setCancelable(true);
        return view;
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.dialog_add_group_ok) {
            Groups fragment = ((Groups) getTargetFragment());
            fragment.addGroup(mName.getText().toString());
            fragment.updateUI();
            dismiss();
        } else if (id == R.id.dialog_add_group_cancel) {
            dismiss();
        }
    }
}

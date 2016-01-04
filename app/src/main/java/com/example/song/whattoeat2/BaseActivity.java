package com.example.song.whattoeat2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import com.example.song.whattoeat2.database.DBAdapter;
import com.example.song.whattoeat2.fragment.RecyclerViewClickListener;

/**
 * Created by Song on 2016/1/4.
 */
public class BaseActivity extends AppCompatActivity {

    protected DBAdapter mDBAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDBAdapter = new DBAdapter(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}

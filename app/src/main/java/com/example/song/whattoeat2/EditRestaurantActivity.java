package com.example.song.whattoeat2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.song.whattoeat2.database.DBAdapter;
import com.example.song.whattoeat2.database.Restaurant;
import com.example.song.whattoeat2.fragment.RestaurantFragment;

public class EditRestaurantActivity extends BaseActivity {

    private Restaurant mRestaurant;

    private Toolbar mToolbar;
    private EditText mEditName, mEditNumber;
    private TextView mName, mNumber;
    private Menu mMenu;
    private MenuItem mEditButton;
    private boolean mEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_restaurant);
        init();
        loadRestaurant();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_action_mode, menu);
        mMenu = menu;
        mEditButton = mMenu.findItem(R.id.menu_action_mode_action);
        closeEditMode();
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mEditMode) {
            closeEditMode();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(mEditMode) {
                    closeEditMode();
                } else {
                    finish();
                }
                return true;
            case R.id.menu_action_mode_action:
                if(mEditMode) {
                    mRestaurant.setName(mEditName.getText().toString());
                    mRestaurant.setNumber(mEditNumber.getText().toString());
                    dbAdapter.updateRestaurant(mRestaurant);
                    closeEditMode();
                } else {
                    initEditMode();
                }
                return true;
            default:
                return true;
        }
    }

    private void closeEditMode() {
        mEditMode = false;
        mName.setVisibility(View.VISIBLE);
        mNumber.setVisibility(View.VISIBLE);
        mEditName.setVisibility(View.GONE);
        mEditNumber.setVisibility(View.GONE);
        mEditButton.setTitle("編輯");
        mEditButton.setIcon(R.drawable.border_color);
        loadRestaurant();
    }

    private void initEditMode() {
        mEditMode = true;
        mName.setVisibility(View.GONE);
        mNumber.setVisibility(View.GONE);
        mEditName.setVisibility(View.VISIBLE);
        mEditNumber.setVisibility(View.VISIBLE);
        mEditButton.setTitle("存檔");
        mEditButton.setIcon(R.drawable.content_save);
        loadRestaurant();
    }

    private void loadRestaurant() {
        mEditName = (EditText) findViewById(R.id.edit_name);
        mEditName.setText(mRestaurant.getName());
        mEditNumber = (EditText) findViewById(R.id.edit_number);
        mEditNumber.setText(mRestaurant.getNumber());

        mName = (TextView) findViewById(R.id.name);
        mName.setText(mRestaurant.getName());
        mNumber = (TextView) findViewById(R.id.number);
        mNumber.setText(mRestaurant.getNumber());
    }

    private void init() {
        // load  the restaurant
        long id = getIntent().getExtras().getLong(RestaurantFragment.BUNDLE_RESTAURANT_ID);
        mRestaurant = dbAdapter.getRestaurant(id);

        //Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("餐廳 > " + mRestaurant.getName());
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}

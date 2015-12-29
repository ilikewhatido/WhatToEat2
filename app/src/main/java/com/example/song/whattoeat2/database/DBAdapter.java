package com.example.song.whattoeat2.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Song on 2015/12/7.
 */
public class DBAdapter {

    private static final String DB_NAME = "wte2.db";
    private static final int DB_VERSION = 1;
    private SQLiteDatabase mSQLiteDatabase;

    // TABLE: CIRCLE
    public static final String TABLE_CIRCLE = "circle";
    public static final String CIRCLE_ID = "_id";
    public static final String CIRCLE_NAME = "name";
    public static final String CREATE_TABLE_CIRCLE = "CREATE TABLE " + TABLE_CIRCLE + " (" +
            CIRCLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CIRCLE_NAME + " VARCHAR(255) NOT NULL " +
            ");";
    public static final String DEFAULT_CIRCLE = "\"ALL\"";
    public static final String CREATE_DEFAULT_CIRCLE = "INSERT INTO " + TABLE_CIRCLE + " (" + CIRCLE_NAME + ")" + " VALUES (" + DEFAULT_CIRCLE + ");";

    // TABLE: RESTAURANT
    public static final String TABLE_RESTAURANT = "restaurant";
    public static final String RESTAURANT_ID = "_id";
    public static final String RESTAURANT_NAME = "name";
    public static final String RESTAURANT_NUMBER = "number";
    public static final String CREATE_TABLE_RESTAURANT = "CREATE TABLE " + TABLE_RESTAURANT + " (" +
            RESTAURANT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            RESTAURANT_NAME + " VARCHAR(255) NOT NULL UNIQUE, " +
            RESTAURANT_NUMBER + " VARCHAR(255)" +
            ");";
    public static final String DROP_TABLE_RESTAURANT = "DROP TABLE " + TABLE_RESTAURANT + " IF EXISTS;";

    // TABLE RESTAURANT_CIRCLE
    public static final String TABLE_RESTAURANT_CIRCLE = "restaurant_circle";
    public static final String RESTAURANT_CIRCLE_RESTAURANT_ID = "restaurant_id";
    public static final String RESTAURANT_CIRCLE_CIRCLE_ID = "circle_id";
    public static final String CREATE_TABLE_TABLE_RESTAURANT_CIRCLE = "CREATE TABLE " + TABLE_RESTAURANT_CIRCLE + " (" +
            RESTAURANT_CIRCLE_RESTAURANT_ID  + " INTEGER, " +
            RESTAURANT_CIRCLE_CIRCLE_ID + " INTEGER, " +
            "PRIMARY KEY (" + RESTAURANT_CIRCLE_RESTAURANT_ID + ", " + RESTAURANT_CIRCLE_CIRCLE_ID + "), " +
            "FOREIGN KEY (" + RESTAURANT_CIRCLE_RESTAURANT_ID + ") REFERENCES " + TABLE_RESTAURANT + " ("+ RESTAURANT_ID + ") ON DELETE CASCADE, " +
            "FOREIGN KEY (" + RESTAURANT_CIRCLE_CIRCLE_ID + ") REFERENCES " + TABLE_CIRCLE + " ("+ CIRCLE_ID + ") ON DELETE CASCADE" +
            ");";

    public DBAdapter(Context context) {
        mSQLiteDatabase = new DbHelper(context, DB_NAME, null, DB_VERSION).getWritableDatabase();
    }

    public long addRestaurant(String name, String number) {
        Log.d(getClass().getName(), "Adding restaurant(name=" + name + ", number= " + number + ")");
        ContentValues cv = new ContentValues();
        cv.put(RESTAURANT_NAME, name);
        cv.put(RESTAURANT_NUMBER, number);
        return mSQLiteDatabase.insert(TABLE_RESTAURANT, null, cv);
    }

    public void deleteGroups(long[] ids) {
        String[] idsToDelete = new String[ids.length];
        for(int i = 0; i < ids.length; i++)
            idsToDelete[i] = String.valueOf(ids[i]);
        String whereClause = CIRCLE_ID + " IN (" + TextUtils.join(",", idsToDelete) + ")";
        Log.d(getClass().getName(), "deleteRestaurantByGroup" + " WHERE " + whereClause);
        mSQLiteDatabase.delete(TABLE_CIRCLE, whereClause, null);
    }

    public long addRestaurantToGroup(long restaurantId, long circleId) {
        ContentValues cv = new ContentValues();
        cv.put(RESTAURANT_CIRCLE_RESTAURANT_ID, restaurantId);
        cv.put(RESTAURANT_CIRCLE_CIRCLE_ID, circleId);
        return mSQLiteDatabase.insert(TABLE_RESTAURANT_CIRCLE, null, cv);
    }

    public void removeRestaurantFromGroup(long[] restaurantIds, long circleId) {
        String[] idsToDelete = new String[restaurantIds.length];
        for(int i = 0; i < restaurantIds.length; i++)
            idsToDelete[i] = String.valueOf(restaurantIds[i]);
        String whereClause = RESTAURANT_CIRCLE_CIRCLE_ID + " = " + circleId + " AND " + RESTAURANT_CIRCLE_RESTAURANT_ID + " IN (" + TextUtils.join(",", idsToDelete) + ")";
        Log.d(getClass().getName(), "unlinkRestaurantFromGroup" + " WHERE " + whereClause);
        mSQLiteDatabase.delete(TABLE_RESTAURANT_CIRCLE, whereClause, null);
    }

    public long addGroup(String name) {
        Log.d(getClass().getName(), "Adding circle(" + name + ")");
        ContentValues cv = new ContentValues();
        cv.put(CIRCLE_NAME, name);
        return mSQLiteDatabase.insert(TABLE_CIRCLE, null, cv);
    }

    public void removeRestaurants(long[] ids) {
        String[] idsToDelete = new String[ids.length];
        for(int i = 0; i < ids.length; i++)
            idsToDelete[i] = String.valueOf(ids[i]);
        String whereClause = RESTAURANT_ID + " IN (" + TextUtils.join(",", idsToDelete) + ")";
        Log.d(getClass().getName(), "deleteRestaurants" + " WHERE " + whereClause);
        mSQLiteDatabase.delete(TABLE_RESTAURANT, whereClause, null);
    }

    public Cursor getRestaurants() {
        Log.d(getClass().getName(), "executing getRestaurants...");
        String[] columns = {RESTAURANT_ID, RESTAURANT_NAME, RESTAURANT_NUMBER};
        return mSQLiteDatabase.query(TABLE_RESTAURANT, columns, null, null, null, null, null);
    }

    public Cursor getRestaurantsByGroup(long id) {
        String sql = "SELECT * FROM " + TABLE_RESTAURANT +
                " WHERE " + RESTAURANT_ID +
                " IN" +
                " ( SELECT " + RESTAURANT_CIRCLE_RESTAURANT_ID + " FROM " + TABLE_RESTAURANT_CIRCLE +
                " WHERE " + RESTAURANT_CIRCLE_CIRCLE_ID + " = " + id + ");";
        return mSQLiteDatabase.rawQuery(sql, null);
    }

    public Cursor getRestaurantsNotInGroup(long id) {
        String sql = "SELECT * FROM " + TABLE_RESTAURANT +
                " WHERE " + RESTAURANT_ID +
                " NOT IN" +
                " ( SELECT " + RESTAURANT_CIRCLE_RESTAURANT_ID + " FROM " + TABLE_RESTAURANT_CIRCLE +
                " WHERE " + RESTAURANT_CIRCLE_CIRCLE_ID + " = " + id + ");";
        return mSQLiteDatabase.rawQuery(sql, null);
    }

    public Cursor getGroups() {
        Log.d(getClass().getName(), "executing getCircles...");
        String[] columns = {CIRCLE_ID, CIRCLE_NAME};
        return mSQLiteDatabase.query(TABLE_CIRCLE, columns, null, null, null, null, null);
    }

    private class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onConfigure(SQLiteDatabase db) {
            super.onConfigure(db);
            db.setForeignKeyConstraintsEnabled(true);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                Log.e(getClass().getName(), "Creating database...");
                db.execSQL(CREATE_TABLE_CIRCLE);
                db.execSQL(CREATE_DEFAULT_CIRCLE);
                db.execSQL(CREATE_TABLE_RESTAURANT);
                db.execSQL(CREATE_TABLE_TABLE_RESTAURANT_CIRCLE);
            } catch (SQLException e) {
                Log.e(getClass().getName(), e.getMessage());
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                Log.e(getClass().getName(), "Upgrading database...");
                db.execSQL(DROP_TABLE_RESTAURANT);
                onCreate(db);
            } catch (SQLException e) {
                Log.e(getClass().getName(), e.getMessage());
            }
        }
    }
}

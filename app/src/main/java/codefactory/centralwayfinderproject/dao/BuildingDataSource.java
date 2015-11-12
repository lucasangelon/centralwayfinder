package codefactory.centralwayfinderproject.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import codefactory.centralwayfinderproject.database.MySQLiteHelper;
import codefactory.centralwayfinderproject.models.Building;

/**
 * Created by Gustavo on 10/11/2015.
 */
public class BuildingDataSource {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;

    public BuildingDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void close() {
        dbHelper.close();
    }

    /**
     * Save building object on the table Campus
     */
    public void insertBuilding(Building object){
        database = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(dbHelper.COLUMN_BUILDING_ID, object.getId());
        values.put(dbHelper.COLUMN_BUILDING_IMAGE, object.getImage());
        values.put(dbHelper.COLUMN_BUILDING_NAME, object.getName());
        values.put(dbHelper.COLUMN_BUILDING_LONGITUDE, object.getLongitude());
        values.put(dbHelper.COLUMN_BUILDING_LATITUDE, object.getLongitude());

        database.insert(dbHelper.TABLE_BUILDING, null, values);
        Log.d("INSERT BUILDING", object.toString());
        close();

    }
}

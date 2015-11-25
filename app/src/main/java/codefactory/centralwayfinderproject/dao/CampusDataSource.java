package codefactory.centralwayfinderproject.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import codefactory.centralwayfinderproject.database.MySQLiteHelper;
import codefactory.centralwayfinderproject.models.Campus;

/**
 * Created by Gustavo on 22/10/2015.
 */
public class CampusDataSource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;

    public CampusDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void close() {
        dbHelper.close();
    }

    /**
     * Save campus object on the table Campus
     */
    public void insertCampus(Campus objCampus){
        database = dbHelper.getWritableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(dbHelper.COLUMN_ID, objCampus.getCampusID());
        values.put(dbHelper.COLUMN_NAME, objCampus.getCampusName());
        values.put(dbHelper.COLUMN_VERSION, objCampus.getCampusVersion());
        values.put(dbHelper.COLUMN_LONG, objCampus.getCampusLong());
        values.put(dbHelper.COLUMN_LAT, objCampus.getCampusLat());
        values.put(dbHelper.COLUMN_ZOOM, objCampus.getCampusZoom());

        database.insert(dbHelper.TABLE_CAMPUS, null, values);
        Log.d("INSERT CAMPUS" ,objCampus.toString());
        close();

    }

    /**
     * Update campus object on the table Campus
     */
    public void updateCampus(Campus objCampus){
        database = dbHelper.getWritableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(dbHelper.COLUMN_ID, objCampus.getCampusID());
        values.put(dbHelper.COLUMN_NAME, objCampus.getCampusName());
        values.put(dbHelper.COLUMN_VERSION, objCampus.getCampusVersion());
        values.put(dbHelper.COLUMN_LONG, objCampus.getCampusLong());
        values.put(dbHelper.COLUMN_LAT, objCampus.getCampusLat());
        values.put(dbHelper.COLUMN_ZOOM, objCampus.getCampusZoom());

        database.update(dbHelper.TABLE_CAMPUS, values, dbHelper.COLUMN_ID + " = ?", new String[]{objCampus.getCampusID()});
        Log.d("UPDATE CAMPUS" ,objCampus.toString());
        close();

    }

    /**
     * Get all elements from Campus table
     */
    public ArrayList<Campus> getAllCampus(){
        ArrayList<Campus> allCampus = new ArrayList<Campus>();
        database = dbHelper.getReadableDatabase();

        Cursor res = database.rawQuery(dbHelper.SQL_SELECT_TABLE_CAMPUS,null);
        res.moveToFirst();

        while (res.isAfterLast()==false){
            Campus campus = new Campus();
            campus.setCampusID(res.getString(res.getColumnIndex(dbHelper.COLUMN_ID)));
            campus.setCampusName(res.getString(res.getColumnIndex(dbHelper.COLUMN_NAME)));
            campus.setCampusVersion(res.getString(res.getColumnIndex(dbHelper.COLUMN_VERSION)));
            campus.setCampusLat(res.getDouble(res.getColumnIndex(dbHelper.COLUMN_LAT)));
            campus.setCampusLong(res.getDouble(res.getColumnIndex(dbHelper.COLUMN_LONG)));
            campus.setCampusZoom(res.getDouble(res.getColumnIndex(dbHelper.COLUMN_ZOOM)));

            allCampus.add(campus);
            res.moveToNext();
        }
        close();
        return allCampus;
    }

    /**
     * Return specific element from Campus table
     */
    public Campus getSpecificCampus(String name){
        Campus result = new Campus();
        database = dbHelper.getReadableDatabase();

        Cursor res = database.rawQuery(dbHelper.SQL_SELECT_TABLE_CAMPUS + " WHERE " + dbHelper.COLUMN_NAME + " = '" + name+ "'", null);
        res.moveToFirst();

        //Setting query value on the object
        result.setCampusID(res.getString(res.getColumnIndex(dbHelper.COLUMN_ID)));
        result.setCampusName(res.getString(res.getColumnIndex(dbHelper.COLUMN_NAME)));
        result.setCampusVersion(res.getString(res.getColumnIndex(dbHelper.COLUMN_VERSION)));
        result.setCampusLat(res.getDouble(res.getColumnIndex(dbHelper.COLUMN_LAT)));
        result.setCampusLong(res.getDouble(res.getColumnIndex(dbHelper.COLUMN_LONG)));
        result.setCampusZoom(res.getDouble(res.getColumnIndex(dbHelper.COLUMN_ZOOM)));

        close();
        return result;
    }

    public void dropTable(){
        database = dbHelper.getWritableDatabase();

        database.execSQL(dbHelper.SQL_DROP_TABLE_ROOM);
        database.execSQL(dbHelper.SQL_CREATE_TABLE_ROOM);
    }
}

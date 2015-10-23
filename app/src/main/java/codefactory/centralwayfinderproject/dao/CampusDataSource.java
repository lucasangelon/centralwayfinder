package codefactory.centralwayfinderproject.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;
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
    private String[] allColumns = {MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_NAME, MySQLiteHelper.COLUMN_LONG, MySQLiteHelper.COLUMN_LAT, MySQLiteHelper.COLUMN_ZOOM};

    public CampusDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        // Gets the data repository in write mode
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    /**
     * Save campus object on the table Campus
     */
    public void insertCampus(Campus objCampus){
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(dbHelper.COLUMN_ID, objCampus.getCampusID());
        values.put(dbHelper.COLUMN_NAME, objCampus.getCampusName());
        values.put(dbHelper.COLUMN_LONG, objCampus.getCampusLong());
        values.put(dbHelper.COLUMN_LAT, objCampus.getCampusLat());
        values.put(dbHelper.COLUMN_ZOOM, objCampus.getCampusZoom());

        database.insert(dbHelper.TABLE_CAMPUS, null, values);
        Log.d(CampusDataSource.class.getName(), "INSERT: " + objCampus.toString());

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
            campus.setCampusLat(res.getDouble(res.getColumnIndex(dbHelper.COLUMN_LAT)));
            campus.setCampusLong(res.getDouble(res.getColumnIndex(dbHelper.COLUMN_LONG)));
            campus.setCampusZoom(res.getDouble(res.getColumnIndex(dbHelper.COLUMN_ZOOM)));

            allCampus.add(campus);
            res.moveToNext();
        }

        return allCampus;
    }
}

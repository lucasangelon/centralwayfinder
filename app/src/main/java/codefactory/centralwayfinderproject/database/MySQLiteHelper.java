package codefactory.centralwayfinderproject.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Gustavo on 21/10/2015.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "CentralWayFinder.db";

    //Campus table structure
    public static final String TABLE_CAMPUS = "campus";
    public static final String COLUMN_ID = "CampusID";
    public static final String COLUMN_NAME = "CampusName";
    public static final String COLUMN_VERSION = "CampusVersion";
    public static final String COLUMN_LONG = "CampusLong";
    public static final String COLUMN_LAT = "CampusLat";
    public static final String COLUMN_ZOOM = "CampusZoom";

    //Room table structure
    public static final String TABLE_ROOM = "room";
    public static final String COLUMN_ROOM_ID = "RoomID";
    public static final String COLUMN_ROOM_NAME = "RoomName";
    public static final String COLUMN_BUILDING_ID_FK = "BuildingID";
    public static final String COLUMN_ROOM_IMAGE = "RoomImage";

    //Building table structure
    public static final String TABLE_BUILDING = "building";
    public static final String COLUMN_BUILDING_ID = "BuildingID";
    public static final String COLUMN_BUILDING_NAME = "BuildingName";
    public static final String COLUMN_BUILDING_LATITUDE = "BuildingLatitude";
    public static final String COLUMN_BUILDING_LONGITUDE = "BuildingLongitude";
    public static final String COLUMN_BUILDING_IMAGE = "BuildingImage";

    //Database queries from Campus
    public static final String SQL_SELECT_TABLE_CAMPUS = "SELECT * FROM " + TABLE_CAMPUS ;
    public static final String SQL_CREATE_TABLE_CAMPUS = "CREATE TABLE " + TABLE_CAMPUS + "(" + COLUMN_ID + " text primary key, " + COLUMN_NAME + " text, " + COLUMN_VERSION + " text, " + COLUMN_LONG + " double, " + COLUMN_LAT + " double, " + COLUMN_ZOOM + " double)";
    public static final String SQL_DROP_TABLE_CAMPUS = "DROP TABLE IF EXISTS " + TABLE_CAMPUS;

    //Database queries from Room
    public static final String SQL_SELECT_TABLE_ROOM = "SELECT * FROM " + TABLE_ROOM ;
    public static final String SQL_CREATE_TABLE_ROOM = "CREATE TABLE " + TABLE_ROOM + "(" + COLUMN_ROOM_ID + " integer primary key, " + COLUMN_ROOM_NAME + " text, " + COLUMN_BUILDING_ID_FK + " integer, " + COLUMN_ROOM_IMAGE + " text)";
    public static final String SQL_DROP_TABLE_ROOM = "DROP TABLE IF EXISTS " + TABLE_ROOM;

    //Database queries from Building
    public static final String SQL_SELECT_TABLE_BUILDING = "SELECT * FROM " + TABLE_BUILDING;
    public static final String SQL_CREATE_TABLE_BUILDING = "CREATE TABLE " + TABLE_BUILDING +  "(" + COLUMN_BUILDING_ID + " integer primary key, " + COLUMN_BUILDING_IMAGE + " text, " + COLUMN_BUILDING_NAME + " text, " + COLUMN_BUILDING_LONGITUDE + " double, " + COLUMN_BUILDING_LATITUDE + " double)";
    public static final String SQL_DROP_TABLE_BUILDING = "DROP TABLE IF EXISTS " + TABLE_BUILDING;

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_TABLE_CAMPUS);
        db.execSQL(SQL_CREATE_TABLE_ROOM);
        db.execSQL(SQL_CREATE_TABLE_BUILDING);
        Log.d("DATABASE CREATE", "SUCCESS ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL(SQL_DROP_TABLE_CAMPUS);
        db.execSQL(SQL_DROP_TABLE_ROOM);
        db.execSQL(SQL_DROP_TABLE_BUILDING);
        onCreate(db);
    }
}

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
    public static final String COLUMN_LONG = "CampusLong";
    public static final String COLUMN_LAT = "CampusLat";
    public static final String COLUMN_ZOOM = "CampusZoom";

    //Database queries from Campus
    public static final String SQL_SELECT_TABLE_CAMPUS = "SELECT * FROM " + TABLE_CAMPUS ;
    public static final String SQL_CREATE_TABLE_CAMPUS = "CREATE TABLE " + TABLE_CAMPUS + "(" + COLUMN_ID + " text primary key, " + COLUMN_NAME + " text, " + COLUMN_LONG + " double, " + COLUMN_LAT + " double, " + COLUMN_ZOOM + " double)";
    public static final String SQL_DROP_TABLE_CAMPUS = "DROP TABLE IF EXISTS " + TABLE_CAMPUS;

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
        Log.d("DATABASE", "SUCCESS ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL(SQL_DROP_TABLE_CAMPUS);
        onCreate(db);
    }
}

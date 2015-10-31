package codefactory.centralwayfinderproject.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import codefactory.centralwayfinderproject.database.MySQLiteHelper;
import codefactory.centralwayfinderproject.models.Room;

/**
 * Created by Gustavo on 28/10/2015.
 */
public class RoomDataSource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;

    public RoomDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void close() {
        dbHelper.close();
    }

    /**
     * Save room object on the table Room
     */
    public void insertRoom(Room objRoom){
        database = dbHelper.getWritableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(dbHelper.COLUMN_ROOM_ID, objRoom.getRoomID());
        values.put(dbHelper.COLUMN_ROOM_NAME, objRoom.getRoomName());

        database.insert(dbHelper.TABLE_ROOM, null, values);

        Log.d("INSERT ROOM",objRoom.toString());
        close();

    }
}

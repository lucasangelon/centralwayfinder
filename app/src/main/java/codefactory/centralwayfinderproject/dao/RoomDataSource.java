package codefactory.centralwayfinderproject.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

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
     * Save room object on table Room
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

    /**
     * Get all elements from Room table
     */
    public ArrayList<Room> getAllRooms(){
        ArrayList<Room> allRooms = new ArrayList<>();
        database = dbHelper.getReadableDatabase();

        Cursor res = database.rawQuery(dbHelper.SQL_SELECT_TABLE_ROOM,null);
        res.moveToFirst();

        while (res.isAfterLast()==false){
            Room room = new Room();
            room.setRoomID(res.getInt(res.getColumnIndex(dbHelper.COLUMN_ROOM_ID)));
            room.setRoomName(res.getString(res.getColumnIndex(dbHelper.COLUMN_ROOM_NAME)));

            allRooms.add(room);
            res.moveToNext();
        }

        close();
        return allRooms;
    }

    /**
     * Get all elements from Room table
     */
    public ArrayList<Room> getServices(){
        ArrayList<Room> serviceList = new ArrayList<>();
        database = dbHelper.getReadableDatabase();

        /* NEED TO CHANGE SQL QUERY */
        Cursor res = database.rawQuery(dbHelper.SQL_SELECT_TABLE_ROOM,null);
        res.moveToFirst();

        while (res.isAfterLast()==false){
            Room room = new Room();
            room.setRoomID(res.getInt(res.getColumnIndex(dbHelper.COLUMN_ROOM_ID)));
            room.setRoomName(res.getString(res.getColumnIndex(dbHelper.COLUMN_ROOM_NAME)));

            serviceList.add(room);
            res.moveToNext();
        }

        close();
        return serviceList;
    }
}

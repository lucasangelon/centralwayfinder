package codefactory.centralwayfinderproject.models;

/**
 * Created by Gustavo T. Dias
 * Class to represent Room's object
 */
public class Room {
    //Variables
    private int roomID;
    private String roomName;

    public Room() {
        //Default construction
    }

    public Room(int roomID, String roomName) {
        this.roomID = roomID;
        this.roomName = roomName;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomID=" + roomID +
                ", roomName='" + roomName + '\'' +
                '}';
    }
}

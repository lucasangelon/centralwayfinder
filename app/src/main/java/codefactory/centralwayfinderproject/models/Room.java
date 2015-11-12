package codefactory.centralwayfinderproject.models;

/**
 * Created by Gustavo T. Dias
 * Class to represent Room's object
 */
public class Room {
    //Variables
    private int roomID;
    private String roomName;
    private int buildingID;
    private String roomImage;

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

    public int getBuildingID() {
        return buildingID;
    }

    public void setBuildingID(int buildingID) {
        this.buildingID = buildingID;
    }

    public String getRoomImage() {
        return roomImage;
    }

    public void setRoomImage(String roomImage) {
        this.roomImage = roomImage;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomID=" + roomID +
                ", roomName='" + roomName + '\'' +
                ", buildingID=" + buildingID +
                ", roomImage='" + roomImage + '\'' +
                '}';
    }
}

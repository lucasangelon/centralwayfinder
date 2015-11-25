package codefactory.centralwayfinderproject.models;

import android.app.Application;

import java.util.ArrayList;

/**
 * Created by Gustavo on 17/11/2015.
 */
public class GlobalObject extends Application {

    private int buildingID;
    private String buildingImage;
    private double buildingLongitude;
    private double buildingLatitude;
    private String buildingName;
    private int roomID;
    private String roomName;
    private ArrayList<String> maps;



    public int getBuildingID() {
        return buildingID;
    }

    public void setBuildingID(int buildingID) {
        this.buildingID = buildingID;
    }

    public String getBuildingImage() {
        return buildingImage;
    }

    public void setBuildingImage(String buildingImage) {
        this.buildingImage = buildingImage;
    }

    public double getBuildingLongitude() {
        return buildingLongitude;
    }

    public void setBuildingLongitude(double buildingLongitude) {
        this.buildingLongitude = buildingLongitude;
    }

    public double getBuildingLatitude() {
        return buildingLatitude;
    }

    public void setBuildingLatitude(double buildingLatitude) {
        this.buildingLatitude = buildingLatitude;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
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

    public ArrayList<String> getMaps() {
        return maps;
    }

    public void setMaps(ArrayList<String> maps) {
        this.maps = maps;
    }
}

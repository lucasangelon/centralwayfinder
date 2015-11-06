package codefactory.centralwayfinderproject.models;

/**
 * Created by Gustavo on 4/11/2015.
 */
public class Building {

    private double buildingLong;
    private double buildingLat;
    private String buildingTitle;
    private String buildingAddress;
    private int buildingID;
    private String campusID;

    public Building() {

    }

    public Building(double buildingLong, double buildingLat, String buildingTitle, String buildingAddress, int buildingID, String campusID) {
        this.buildingLong = buildingLong;
        this.buildingLat = buildingLat;
        this.buildingTitle = buildingTitle;
        this.buildingAddress = buildingAddress;
        this.buildingID = buildingID;
        this.campusID = campusID;
    }

    public double getBuildingLong() {
        return buildingLong;
    }

    public void setBuildingLong(double buildingLong) {
        this.buildingLong = buildingLong;
    }

    public double getBuildingLat() {
        return buildingLat;
    }

    public void setBuildingLat(double buildingLat) {
        this.buildingLat = buildingLat;
    }

    public String getBuildingTitle() {
        return buildingTitle;
    }

    public void setBuildingTitle(String buildingTitle) {
        this.buildingTitle = buildingTitle;
    }

    public String getBuildingAddress() {
        return buildingAddress;
    }

    public void setBuildingAddress(String buildingAddress) {
        this.buildingAddress = buildingAddress;
    }

    public int getBuildingID() {
        return buildingID;
    }

    public void setBuildingID(int buildingID) {
        this.buildingID = buildingID;
    }

    public String getCampusID() {
        return campusID;
    }

    public void setCampusID(String campusID) {
        this.campusID = campusID;
    }
}

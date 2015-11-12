package codefactory.centralwayfinderproject.models;

/**
 * Created by Gustavo on 4/11/2015.
 */
public class Building {

    private int id;
    private String image;
    private double longitude;
    private double latitude;
    private String name;
    private String campusID;

    public Building() {

    }

    public Building(double longitude, double latitude, String name, String address, int id, String campusID) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.name = name;
        this.id = id;
        this.campusID = campusID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCampusID() {
        return campusID;
    }

    public void setCampusID(String campusID) {
        this.campusID = campusID;
    }

    @Override
    public String toString() {
        return "Building{" +
                "id=" + id +
                ", image='" + image + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", name='" + name + '\'' +
                ", campusID='" + campusID + '\'' +
                '}';
    }
}

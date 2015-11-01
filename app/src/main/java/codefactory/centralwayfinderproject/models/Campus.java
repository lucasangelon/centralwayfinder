package codefactory.centralwayfinderproject.models;

/**
 * Created by Gustavo T. Dias
 * Class to represent Campus' object
 */
public class Campus {

    //Variables
    private String campusID;
    private String campusName;
    private double campusLong;
    private double campusLat;
    private double campusZoom;

    public Campus(){
        //Default construction
    }

    public String getCampusID() {
        return campusID;
    }

    public void setCampusID(String campusID) {
        this.campusID = campusID;
    }

    public String getCampusName() {
        return campusName;
    }

    public void setCampusName(String campusName) {
        this.campusName = campusName;
    }

    public double getCampusLong() {
        return campusLong;
    }

    public void setCampusLong(double campusLong) {
        this.campusLong = campusLong;
    }

    public double getCampusLat() {
        return campusLat;
    }

    public void setCampusLat(double campusLat) {
        this.campusLat = campusLat;
    }

    public double getCampusZoom() {
        return campusZoom;
    }

    public void setCampusZoom(double campusZoom) {
        this.campusZoom = campusZoom;
    }

    @Override
    public String toString() {
        return "Campus{" +
                "campusID='" + campusID + '\'' +
                ", campusName='" + campusName + '\'' +
                ", campusLong=" + campusLong +
                ", campusLat=" + campusLat +
                ", campusZoom=" + campusZoom +
                '}';
    }
}

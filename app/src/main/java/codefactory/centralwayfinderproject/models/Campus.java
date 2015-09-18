package codefactory.centralwayfinderproject.models;

import java.io.Serializable;

/**
 * Created by Chuck on 18/09/2015.
 *  implements Serializable so it can be passed as an intent extra
 */
public class Campus implements Serializable {

    public Campus(){};

    public Campus(String CampusID, String CampusName, double CampusLong, double CampusLat, double CampusZoom)
    {
        campusID = CampusID;
        campusName = CampusName;
        campusLong = CampusLong;
        campusLat = CampusLat;
        campusZoom = CampusZoom;
    }

    public String campusID;
    public String campusName;
    public double campusLong;
    public double campusLat;
    public double campusZoom;
}

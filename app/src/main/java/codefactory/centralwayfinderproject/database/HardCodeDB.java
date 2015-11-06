package codefactory.centralwayfinderproject.database;

import android.content.Context;

import java.util.ArrayList;

import codefactory.centralwayfinderproject.dao.CampusDataSource;
import codefactory.centralwayfinderproject.dao.RoomDataSource;
import codefactory.centralwayfinderproject.models.Building;
import codefactory.centralwayfinderproject.models.Campus;
import codefactory.centralwayfinderproject.models.Room;

/**
 * Created by Gustavo on 4/11/2015.
 */
public class HardCodeDB {

    public HardCodeDB() {

    }

    public void Search_Campus(Context mContext) {
        ArrayList<Campus> campusArrayList = new ArrayList<>();
        CampusDataSource campusDataSource = new CampusDataSource(mContext);

        campusArrayList.add(new Campus("EP", "East Perth", -31.95121d, 115.87237d, 19d));
        campusArrayList.add(new Campus("LE", "Leederville", -31.93393d, 115.84264d, 19.5d));
        campusArrayList.add(new Campus("ML", "Mount Lawley", -31.93943d, 115.87567d, 19.5d));
        campusArrayList.add(new Campus("OHCWA", "Nedlands", -31.97000d, 115.81575d, 19.5d));
        campusArrayList.add(new Campus("PE", "Perth", -31.94766d, 115.86212d, 18.75d));

        for (int x = 0; x < campusArrayList.size(); x++) {
            campusDataSource.insertCampus(campusArrayList.get(x));
        }
    }


    public void SearchRooms(Context mContext) {
        ArrayList<Room> roomArrayList = new ArrayList<>();
        RoomDataSource roomDataSource = new RoomDataSource(mContext);

        roomArrayList.add(new Room(3, "R001"));
        roomArrayList.add(new Room(4, "R002"));
        roomArrayList.add(new Room(5, "R003"));
        roomArrayList.add(new Room(6, "R004"));
        roomArrayList.add(new Room(8, "R005"));
        roomArrayList.add(new Room(9, "Aroma Cafe"));

        for (int x = 0; x < roomArrayList.size(); x++) {
            roomDataSource.insertRoom(roomArrayList.get(x));
        }

    }

    public Building ResolvePath(int WaypointID, boolean Disability) {

        return new Building( -31.94760d, 115.86121d, "BUILDING 04", "25 Aberdeen Street", 1,"PE");
    }

    public class SOAP_SearchCampus {
        public String campusID;
        public String campusName;
        public double campusLat;
        public double campusLong;
        public double campusZoom;

        public SOAP_SearchCampus(String CampusID, String CampusName, double CampusLat, double CampusLong, double CampusZoom) {
            campusID = CampusID;
            campusName = CampusName;
            campusLat = CampusLat;
            campusLong = CampusLong;
            campusZoom = CampusZoom;
        }
    }

    public class SOAP_ResolvePath {
        public double[] campusFrom;
        public double[] campusTo;
        public String buildingTitle;
        public String buildingImgPath;
        public String[][] maps;

        public SOAP_ResolvePath(double[] CampusFrom, double[] CampusTo, String BuildingTitle, String BuildingImgPath, String[][] Maps) {
            campusFrom = CampusFrom;
            campusTo = CampusTo;
            buildingTitle = BuildingTitle;
            buildingImgPath = BuildingImgPath;
            maps = Maps;
        }
    }
}

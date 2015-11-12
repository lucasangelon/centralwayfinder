package codefactory.centralwayfinderproject.activites;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import codefactory.centralwayfinderproject.R;
import codefactory.centralwayfinderproject.dao.RoomDataSource;
import codefactory.centralwayfinderproject.database.HardCodeDB;
import codefactory.centralwayfinderproject.helpers.HttpConnection;
import codefactory.centralwayfinderproject.helpers.PathJSONParser;
import codefactory.centralwayfinderproject.helpers.Useful;
import codefactory.centralwayfinderproject.models.Building;
import codefactory.centralwayfinderproject.models.Campus;
import codefactory.centralwayfinderproject.models.Room;

/**
 * Created by Gustavo on 21/09/2015.
 */
public class GoogleMapActivity extends FragmentActivity {

    private LatLng startLocation;
    private LatLng endLocation;
    private Float defaultZoom = 15.0f;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private final static String MODE_DRIVING = "driving";
    private final static String MODE_WALKING = "walking";

    private Campus startPoint;
    private Useful util;

    private Building building;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);

        startPoint = new Campus();
        util = new Useful(this);

        /*
        * Check if the gps is ON or OFF
        * In case ON: Use current location as start point
        * In case OFF: Use campus start point as start point
        */
        if(util.isGpsOn()) {
            //Get current location
            startPoint.setCampusLat(util.getUserLocation().getLatitude());
            startPoint.setCampusLong(util.getUserLocation().getLongitude());
            startPoint.setCampusZoom(15.0);

        } else {
            //Getting default campus name from preference file.
            startPoint = util.getDefaultCampus();

        }

        startLocation = new LatLng(startPoint.getCampusLat(), startPoint.getCampusLong());
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we draw the map for the first time
     * This should only be called once
     */
    private void setUpMap() {

        mMap.addMarker(
                new MarkerOptions()
                        .position(startLocation)
                        .title(startPoint.getCampusName())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLocation, Float.parseFloat(Double.toString(startPoint.getCampusZoom()))));
        mMap.setMyLocationEnabled(false);
    }

    /**
     * Method to search destination place input
     */
    public void onSearch(View v) {

        EditText userDestination = (EditText) findViewById(R.id.txtAddress);
        final String destination = userDestination.getText().toString();
        List<Address> addressList = null;

        //Check if destination is null or empty
        if (destination != null || !destination.equals("")) {

            /** New solution using webservice and database*/
            if (searchForRoom(destination)){
                endLocation = new LatLng(-31.94760d, 115.86121d);
                //endLocation = new LatLng(building.getLatitude(),building.getLongitude());

                //Search for the route
                String url = getMapsApiDirectionsUrl(startLocation, endLocation, MODE_DRIVING);
                ReadTask downloadTask = new ReadTask();
                downloadTask.execute(url);

                //Re-call the map with the new location
                customAddMarker(endLocation, destination, destination);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(endLocation, defaultZoom));
                mMap.setMyLocationEnabled(false);

                // Display popup on which point
                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        //Go to BuildingView
                        Intent intent = new Intent(getApplicationContext(), BuildingViewActivity.class);
                        startActivity(intent);
                    }
                });

                mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                    @Override
                    public View getInfoWindow(Marker marker) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {

                        View v = null;

                        if (!marker.getPosition().equals(startLocation)) {
                            // Getting view from the layout file
                            v = getLayoutInflater().inflate(R.layout.googlemaps_popup, null);

                            //
                            TextView title = (TextView) v.findViewById(R.id.txt_Title);
                            title.setText(building.getName() + "\n" + destination);
                            marker.getPosition();
                        }

                        return v;
                    }
                });

            } else{

                Toast.makeText(this,"Room is unavailable or not in this Campus",Toast.LENGTH_SHORT).show();
            }


            /*Old code
            Geocoder geocoder = new Geocoder(this);
            try {
                //Using Geocoder's object to transform string in geolocation
                addressList = geocoder.getFromLocationName(destination, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Saving the new geolocation on the location object
            Address address = addressList.get(0);
            endLocation = new LatLng(address.getLatitude(), address.getLongitude());

            //Search for the route
            String url = getMapsApiDirectionsUrl(startLocation, endLocation, MODE_DRIVING);
            ReadTask downloadTask = new ReadTask();
            downloadTask.execute(url);

            //Re-call the map with the new location
            customAddMarker(endLocation, destination, destination);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(endLocation, defaultZoom));
            mMap.setMyLocationEnabled(false);

            // Display popup on which point
            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {

                    View v = null;

                    if(!marker.getPosition().equals(startLocation)){
                        // Getting view from the layout file
                        v = getLayoutInflater().inflate(R.layout.googlemaps_popup, null);

                        TextView title = (TextView) v.findViewById(R.id.txt_Title);
                        title.setText(marker.getSnippet());
                        marker.getPosition();
                    }

                    return v;
                }

            });
            */
        }
    }

    public boolean searchForRoom(String room){
        RoomDataSource roomDataSource = new RoomDataSource(this);
        ArrayList<Room> roomList;

        roomList = roomDataSource.getAllRooms();

        for(int index = 0; index < roomList.size(); index++){
            if (roomList.get(index).getRoomName().equalsIgnoreCase(room)) {

                //WebServiceConnection webServiceConnection = new WebServiceConnection(this,3,roomList.get(index).getRoomID());
                //webServiceConnection.checkServiceConnAST.execute();

                HardCodeDB hardCodeDB = new HardCodeDB();
                building = hardCodeDB.ResolvePath(roomList.get(index).getRoomID(),false);
                return true;
            }
        }
        return false;

    }

    public void customAddMarker(LatLng latLng, String title, String snippet) {
        MarkerOptions options = new MarkerOptions();
        options.position(latLng)
                .title(title)
                .snippet(snippet)
                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mMap.addMarker(options);
    }

    private String getMapsApiDirectionsUrl(LatLng start, LatLng end, String mode) {
        String url = "http://maps.googleapis.com/maps/api/directions/json?"
                + "origin=" + start.latitude + "," + start.longitude
                + "&destination=" + end.latitude + "," + end.longitude
                + "&sensor=false&units=metric&mode=" + mode;
        Log.d("url", url);
        return url;
    }

    private class ReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }

    private class ParserTask extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;

            // traversing through routes
            for (int i = 0; i < routes.size(); i++) {
                points = new ArrayList<LatLng>();
                polyLineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = routes.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                polyLineOptions.addAll(points);
                polyLineOptions.width(10);
                polyLineOptions.color(Color.BLUE);
            }

            mMap.addPolyline(polyLineOptions);
        }
    }


}

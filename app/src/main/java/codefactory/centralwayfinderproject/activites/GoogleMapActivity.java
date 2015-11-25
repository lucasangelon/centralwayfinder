package codefactory.centralwayfinderproject.activites;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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
import codefactory.centralwayfinderproject.helpers.HttpConnection;
import codefactory.centralwayfinderproject.helpers.PathJSONParser;
import codefactory.centralwayfinderproject.helpers.Useful;
import codefactory.centralwayfinderproject.models.Campus;
import codefactory.centralwayfinderproject.models.GlobalObject;

/**
 * Created by Gustavo on 21/09/2015.
 */
public class GoogleMapActivity extends FragmentActivity {

    private GlobalObject globalObject;
    private Campus pointOne;
    private Float defaultZoom = 15.0f;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private final static String MODE_DRIVING = "driving";
    private final static String MODE_WALKING = "walking";
    private Useful util;
    private LatLng startLocation;
    private LatLng endLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);

        // Calling Application class (see application tag in AndroidManifest.xml)
        //This object holds information about building and room which the user are looking for.
        globalObject = (GlobalObject) getApplicationContext();

        pointOne = new Campus();
        util = new Useful(this);

        if(util.isGpsOn()){
            //Get current location position
            pointOne.setCampusLat(util.getUserLocation().getLatitude());
            pointOne.setCampusLong(util.getUserLocation().getLongitude());

        } else {
            //Loading campus details from preference file.
            pointOne = util.getDefaultCampus();

        }

        startLocation = new LatLng(pointOne.getCampusLat(), pointOne.getCampusLong());
        endLocation = new LatLng(globalObject.getBuildingLatitude(), globalObject.getBuildingLongitude());

        if (setUpMapIfNeeded()){
            //Search for the route
            String url = getMapsApiDirectionsUrl(startLocation, endLocation, MODE_DRIVING);
            ReadTask downloadTask = new ReadTask();
            downloadTask.execute(url);

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(endLocation, defaultZoom));
            mMap.setMyLocationEnabled(false);
            createPopUpDetailing();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void createPopUpDetailing() {
        // Display popup on which point
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                //Go to BuildingView
                Intent intent = new Intent(getApplicationContext(), BuildingViewActivity.class);
                startActivity(intent);
            }
        });

        //SetUP popup INFORMATION
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
                    TextView title = (TextView) v.findViewById(R.id.txt_Title);
                    title.setText(globalObject.getBuildingName() + "\n" + globalObject.getRoomName());
                    marker.getPosition();
                }
                return v;
            }
        });
    }

    private boolean setUpMapIfNeeded() {
        boolean result = false;
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null){
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

            if(isGoogleMapsInstalled()){
                if (mMap != null){
                    setUpMap();
                    result = true;
                }
                else{
                    result = false;
                }
            }
            else{ //Display a msg if google map is not found
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Install Google Maps");
                builder.setCancelable(false);
                builder.setPositiveButton("Install",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.apps.maps"));
                                startActivity(intent);
                                //Finish the activity so they can't circumvent the check
                                finish();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                result = false;
            }
        }
        return result;
    }

    /**
     * Check if Google maps is installed on the phone
     * @return boolean
     */

    public boolean isGoogleMapsInstalled()
    {
        try{
            ApplicationInfo info = getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0 );
            return true;
        }
        catch(PackageManager.NameNotFoundException e){
            return false;
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
                        .title(pointOne.getCampusName())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        customAddMarker();


    }

    public void customAddMarker() {
        MarkerOptions options = new MarkerOptions();
        options.position(new LatLng(globalObject.getBuildingLatitude(), globalObject.getBuildingLongitude()))
                .title(globalObject.getBuildingName())
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
                points = new ArrayList<>();
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

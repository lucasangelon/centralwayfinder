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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import codefactory.centralwayfinderproject.models.GlobalObject;

/**
 * Created by Gustavo on 21/09/2015.
 */
public class GoogleMapActivity extends AppCompatActivity {

    private SupportMapFragment mapFrag;
    private GoogleMap map;
    private Toolbar toolbar;
    private GlobalObject globalObject;
    private Useful util;
    private ArrayList<LatLng> pins = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        /*GoogleMapOptions options = new GoogleMapOptions();
        options.zOrderOnTop(true);
        options.liteMode(true);
        options.mapToolbarEnabled(true);
        mapFrag = SupportMapFragment.newInstance(options);*/


        if (checkGoogleMapsInstallation()) {
            map = mapFrag.getMap();
            //map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            configMap();
            drawingRoute();
        }


    }

    @Override
    public void onResume() {
        super.onResume();
    }


    public void configMap() {

        globalObject = (GlobalObject) getApplicationContext();
        util = new Useful(this);

        showPoints();

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(pins.get(1), 15)); //Focus and Zoom
        // EVENTS
        // Manages the click in popup option
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(getApplicationContext(), BuildingViewActivity.class);
                startActivity(intent);
            }
        });

        //Creates popup INFORMATION
        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                View v = null;
                if (!marker.getPosition().equals(pins.get(0))) {
                    // Getting view from the layout file
                    v = getLayoutInflater().inflate(R.layout.googlemaps_popup, null);
                    TextView title = (TextView) v.findViewById(R.id.txt_Title);
                    title.setText(Html.fromHtml("<b><font color='#000000'>" + globalObject.getBuildingName() + "<br/>" + globalObject.getRoomName() + "</font></b>"));
                    marker.getPosition();
                }
                return v;
            }
        });
    }

    /**
     * Initiate the map and also checking for google map app and version installed on the phone
     * @return boolean
     */
    public boolean checkGoogleMapsInstallation() {
        boolean result = true;

        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        if (!isGoogleMapsInstalled()) {//Display a msg if google map is not found

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Install Google Maps");
            builder.setCancelable(false);
            builder.setPositiveButton("Install", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.apps.maps"));
                    startActivity(intent);
                    //Finish the activity
                    finish();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            result = false;
        }
        return result;
    }

    /**
     * Checked if Google maps is installed on the phone
     * @return boolean
     */
    public boolean isGoogleMapsInstalled() {
        try {
            ApplicationInfo info = getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * Define start point and final point to draw on the map
     * @return Boolean
     */
    public void showPoints() {

        if(util.isGpsOn()&&(util.getUserLocation()!= null)){
            pins.add(new LatLng(util.getUserLocation().getLatitude(),util.getUserLocation().getLongitude()));//Starting Point

        } else {
            pins.add(new LatLng(util.getLatitudeDefaultCampus(),util.getLongitudeDefaultCampus()));//Starting Point
        }

        pins.add(new LatLng(globalObject.getBuildingLatitude(), globalObject.getBuildingLongitude()));// Destination Point

        //Drawing point one
        map.addMarker(
                new MarkerOptions()
                        .position(pins.get(0))
                        .title("Start Location")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        );

        //Drawing point two
        Marker marker = map.addMarker(
                new MarkerOptions()
                        .position(pins.get(1))
                        .title(globalObject.getBuildingName())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        );

        marker.showInfoWindow();
    }

    public void drawingRoute(){
        //Search for the route between points
        ReadTask getRoute = new ReadTask();
        getRoute.execute();
    }


    private class ReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {

            String data = "";
            String googleUrl = "http://maps.googleapis.com/maps/api/directions/json?"
                    + "origin=" + pins.get(0).latitude + "," + pins.get(0).longitude
                    + "&destination=" + pins.get(1).latitude + "," + pins.get(1).longitude
                    + "&sensor=false&units=metric&mode=driving";
            Log.d("Google Maps URL", googleUrl);
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(googleUrl);
            } catch (Exception e) {
                Log.d("Exception ReadTask", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new BreakingReturnTask().execute(result);
        }
    }

    private class BreakingReturnTask extends
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
            map.addPolyline(polyLineOptions);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;

        if (id == R.id.action_icon1) {
            intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_icon2) {
            intent = new Intent(this, ServiceActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_icon3) {
            Uri uri = Uri.parse("http://central.wa.edu.au/Pages/default.aspx"); // missing 'http://' will cause crash
            intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_icon4) {
            intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
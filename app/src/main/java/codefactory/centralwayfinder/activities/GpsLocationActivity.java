package codefactory.centralwayfinder.activities;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import codefactory.centralwayfinder.R;


public class GpsLocationActivity extends ActionBarActivity implements LocationListener {

    private TextView latituteField;
    private TextView longitudeField;
    private LocationManager locationManager;
    private String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_location);

        latituteField = (TextView) findViewById(R.id.TextView02);
        longitudeField = (TextView) findViewById(R.id.TextView04);

        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Define the criteria how to select the location provider -> use default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
            latituteField.setText("Location not available");
            longitudeField.setText("Location not available");
        }
    }

    /* Request updates at startup */
    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    /* Remove the location listener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        latituteField.setText(String.valueOf(lat));
        longitudeField.setText(String.valueOf(lng));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    //Display a toast when the GPS is TURN ON
    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    //Display a toast when the GPS is TURN OFF
    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    //Display the location by on the selected image
    public void showLocation(View view){
        String[] direction = new String[3];

        /*Initialize array positions
        * 0 = Building 1 - Fancis Street is entry
        * 1 = Building 1 - Museum Street is entry
        * 2 = Building 1 - Abedeen Street is entry
        */
        direction[0] = "Latitude: -31.948460   Longitute: 115.861059";
        direction[1] = "Latitude: -31.948135   Longitute: 115.861326";
        direction[2] = "Latitude: -31.947852   Longitute: 115.861249";

        //Show the location by toast message
        if(view.getId() == R.id.imageButton){
            Toast.makeText(this, direction[2],Toast.LENGTH_SHORT).show();

        }else if(view.getId() == R.id.imageButton2){
            Toast.makeText(this, direction[1],Toast.LENGTH_SHORT).show();

        }else if(view.getId() == R.id.imageButton3){
            Toast.makeText(this, direction[0],Toast.LENGTH_SHORT).show();
        }





    }
}


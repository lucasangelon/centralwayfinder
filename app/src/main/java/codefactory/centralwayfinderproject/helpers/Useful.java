package codefactory.centralwayfinderproject.helpers;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

/**
 * Created by Gustavo
 *
 * Useful class is responsible to handle all method which it will be using multiple times when the app is running.
 */
public class Useful extends Activity {

    private LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    private Location location;
    String provider;

    /**
     * Checks for an GPS Connection
     * @return Boolean
     */
    public boolean isGpsOn() {

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @SuppressWarnings("ResourceType")
    public Location getUserLocation() {

        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        location = locationManager.getLastKnownLocation(provider);

        return location;
    }
}

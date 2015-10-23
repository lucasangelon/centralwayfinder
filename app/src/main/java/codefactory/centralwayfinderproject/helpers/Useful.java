package codefactory.centralwayfinderproject.helpers;

import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

/**
 * Created by Gustavo
 *
 * Useful class is responsible to handle all method which it will be using multiple times when the app is running.
 */
public class Useful extends Application {

    private LocationManager locationManager;
    Context mContext;

    /**
     * Default construction
     */
    public Useful(Context mContext) {
        this.mContext = mContext;
        this.locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
    }

    /**
     * Checks for an GPS Connection
     * @return Boolean
     */
    public boolean isGpsOn() {

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @SuppressWarnings("ResourceType")
    public Location getUserLocation() {

        String provider = locationManager.NETWORK_PROVIDER;
        Location location = locationManager.getLastKnownLocation(provider);

        return location;
    }
}

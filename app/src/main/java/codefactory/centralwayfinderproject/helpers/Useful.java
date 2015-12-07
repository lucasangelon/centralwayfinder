package codefactory.centralwayfinderproject.helpers;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.util.Log;

import codefactory.centralwayfinderproject.models.Campus;

/**
 * Created by Gustavo
 *
 * Useful class is responsible to handle all method which it will be using multiple times when the app is running.
 */
public class Useful extends Application {

    private LocationManager locationManager;
    Context mContext;
    ProgressDialog pd;

    /**
     * Default construction
     */
    public Useful(Context mContext) {
        this.mContext = mContext;

    }

    /**
     * Checks for an GPS Connection
     * @return Boolean
     */
    public boolean isGpsOn() {

        this.locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public Location getUserLocation() {

        this.locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.NETWORK_PROVIDER;
        Location location = null;
        try {
            location = locationManager.getLastKnownLocation(provider);
        }catch (SecurityException ex){
            Log.e("SecurityException",ex.toString());
        }
        return location;
    }

    /**
     * Method which gets default campus Latitude value from preference file "setting"
     * @return - Double Lat
     */
    public Double getLatitudeDefaultCampus(){
        double result;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        result = Double.longBitsToDouble(prefs.getLong("campusLat", 0));

        return result;
    }

    /**
     * Method which gets default campus Longitude value from preference file "setting"
     * @return - Double Lng
     */
    public Double getLongitudeDefaultCampus(){
        double result;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        result = Double.longBitsToDouble(prefs.getLong("campusLong", 0));

        return result;
    }


    /**
     * Method which gets default campus values from preference file "setting"
     * with campus values
     * @return - object Campus
     */
    public Campus getDefaultCampus(){
        Campus defaultCampus = new Campus();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);

        defaultCampus.setCampusName(prefs.getString("campusName", ""));
        defaultCampus.setCampusID(prefs.getString("campusID", ""));
        defaultCampus.setCampusVersion(prefs.getString("campusVersion", ""));
        defaultCampus.setCampusLong(Double.longBitsToDouble(prefs.getLong("campusLong", 0)));
        defaultCampus.setCampusLat(Double.longBitsToDouble(prefs.getLong("campusLat", 0)));
        defaultCampus.setCampusZoom(Double.longBitsToDouble(prefs.getLong("campusZoom", 0)));

        return defaultCampus;
    }

    /**
     * Method which saves or updates the preference file "setting"
     * with campus values
     * @param defaultCampus - object campus which it will be save/update
     */
    public void setDefaultCampus(Campus defaultCampus){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);

        prefs.edit().putString("campusID", defaultCampus.getCampusID()).commit();
        prefs.edit().putString("campusName", defaultCampus.getCampusName()).commit();
        prefs.edit().putString("campusVersion", defaultCampus.getCampusVersion()).commit();
        prefs.edit().putLong("campusLong", Double.doubleToLongBits(defaultCampus.getCampusLong())).commit();
        prefs.edit().putLong("campusLat", Double.doubleToLongBits(defaultCampus.getCampusLat())).commit();
        prefs.edit().putLong("campusZoom", Double.doubleToLongBits(defaultCampus.getCampusZoom())).commit();
    }

    /**
     * Method which get 'firstTime' option at preference file "setting"
     * @return boolean
     */
    public boolean getIsFirstTimeOption(){
        boolean result;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);

        result =  prefs.getBoolean("firstTime",true);

        return result;
    }

    /**
     * Method which set 'firstTime' option at preference file "setting"
     * @return void
     */
    public void setIsFirstTimeOption(boolean value){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        prefs.edit().putBoolean("firstTime", value).commit();
    }

    /**
     * Method which get accessibility option at preference file "setting"
     * @return boolean
     */
    public boolean getAccessibilityOption(){
        boolean result;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);

        result =  prefs.getBoolean("accessibility",false);

        return result;
    }

    /**
     * Method which set accessibility option at preference file "setting"
     * @param value
     */
    public void setAccessibilityOption(boolean value){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        prefs.edit().putBoolean("accessibility", value).commit();
    }

    /**
     * Method which display Loading Message on the screen
     */
    public void displayLoadingMessage(){

        pd = new ProgressDialog(mContext);
        pd.setTitle("Loading...");
        pd.setMessage("Please wait.");
        pd.setCancelable(false);
        pd.show();
    }

    /**
     * Method which disappear with Loading Message on the screen
     */
    public void disappearLoadingMessage(){
        pd.dismiss();
    }

    /**
     * Method which get 'indoorMap' option at preference file "setting"
     * @return boolean
     */
    public boolean getIsFirstIndoorMap(){
        boolean result;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);

        result =  prefs.getBoolean("indoorMap",true);

        return result;
    }

    /**
     * Method which set 'indoorMap' option at preference file "setting"
     * @return void
     */
    public void setIsFirstIndoorMap(boolean value){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        prefs.edit().putBoolean("indoorMap", value).commit();
    }
}

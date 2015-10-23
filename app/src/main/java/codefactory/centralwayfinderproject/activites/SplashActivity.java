package codefactory.centralwayfinderproject.activites;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import codefactory.centralwayfinderproject.R;
import codefactory.centralwayfinderproject.helpers.WebServiceConnection;

public class SplashActivity extends Activity implements OnClickListener {

    private Button btn_startApp;
    private SharedPreferences prefs;
    private boolean isFirstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Loading preferences
        prefs = getSharedPreferences("Settings",MODE_PRIVATE);
        isFirstTime = prefs.getBoolean("firstTime", true);

        //Initialise Button and add listener
        btn_startApp = (Button) findViewById(R.id.btnFirstClick);
        btn_startApp.setOnClickListener(this);

        //Checking Networking Connection
        if (isOnline()) {

            if(isFirstTime){
                WebServiceConnection webServiceConnection = new WebServiceConnection(this);
                //check web service connection and retrieve campus list (if connection available)
                /*AsyncCallWS checkServiceConnAST = new AsyncCallWS();
                checkServiceConnAST.execute();*/
            }

        } else {
            //Prompt user to connect to network or exit application
            noNetworkConnectionDialog();
            Log.d("Not Online", "NOT CONNECTED TO THE INTERNET");
        }


        //Checking GPS Status
        if (!isGpsOn()) {
            /*
                Prompt user to connect to GPS status
             */
            noGpsConnectionDialog();
            Log.d("No GPS", "GPS IS OFF");
        }
        Log.d("DEBUG", "ENABLING BUTTON");
        btn_startApp.setEnabled(true);

    }


    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.btnFirstClick){
            if (isFirstTime){
                prefs.edit().putBoolean("firstTime", false).commit();
                //Go to Select Campus Activity
                Intent intent = new Intent(this, SelectCampusActivity.class);
                startActivity(intent);
            }else{
                //Go to Menu Activity
                Intent intent = new Intent(this, MenuActivity.class);
                startActivity(intent);
            }

        }

    }


    /**
     *  No Network Connection
     *
     *  Prompts User to connect to the internet if they are not
     *
     */
    private void noGpsConnectionDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Some features of this app will not be available without location services enabled")
                .setTitle("LOCATION SERVICE DISABLED")
                .setCancelable(false)
                /*
                    Go to location settings
                 */
                .setPositiveButton("Go to location settings",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(i);
                            }
                        }
                )

                /*
                    Skip GPS check
                 */
                .setNegativeButton("Skip",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SplashActivity.this.onDestroy();
                            }
                        }
                );
        AlertDialog alert = builder.create();
        alert.show();
    }



    /**
     * Checks for an internet Connection
     * @return Boolean
     */
    private boolean isOnline(){

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if(netInfo != null && netInfo.isConnectedOrConnecting()){
            return true;
        } else return false;

    }

    /**
     *  No Network Connection
     *
     *  Prompts User to connect to the internet if they are not
     *
     */
    private void noNetworkConnectionDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This app requires an active network connection, please activate network and restart app")
                .setTitle("NETWORK ERROR")
                .setCancelable(false)
                /*
                    Go to network settings
                 */
                .setPositiveButton("Go to network settings",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent i = new Intent(Settings.ACTION_SETTINGS);
                                startActivity(i);
                            }
                        }
                )

                /*
                    Exits app if user
                 */
                .setNegativeButton("Exit App",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SplashActivity.this.finish();
                            }
                        }
                );
        AlertDialog alert = builder.create();
        alert.show();
    }

   /**
     * Checks for an GPS Connection
     * @return Boolean
     */
    private boolean isGpsOn(){

        // Get the location manager
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }

}

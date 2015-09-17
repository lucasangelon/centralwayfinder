package codefactory.centralwayfinderproject.activites;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

public class SplashActivity extends Activity implements OnClickListener {

    private Button btn_startApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Initialise Button and add listener
        btn_startApp = (Button) findViewById(R.id.btnFirstClick);
        btn_startApp.setOnClickListener(this);

        //Checking Networking Connection
        if (!isOnline()) {
            /*
                Promt user to connect to network or exit application
             */
            noNetworkConnectionDialog();
            Log.d("Not Online", "NOT CONNECTED TO THE INTERNET");
        }

        //Checking GPS Status
        if (!isGpsOn()) {
            /*
                Promt user to connect to GPS status
             */
            noGpsConnectionDialog();
            Log.d("Not GPS", "GPS IS OFF");
        }
    }



    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.btnFirstClick){
            //Go to Menu Activity
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
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
        builder.setMessage("Please, Check Your Location Connection. \n If location is on the App works with more accuracy.")
                .setTitle("No Location Connection")
                .setCancelable(false)
                /*
                    Go to location settings
                 */
                .setPositiveButton("Location Setting",
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
        }else return false;

    }

    /**
     *  No Network Connection
     *
     *  Prompts User to connect to the internet if they are not
     *
     */
    private void noNetworkConnectionDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please, Check Your Internet Connection")
                .setTitle("No Network Connection")
                .setCancelable(false)
                /*
                    Go to network settings
                 */
                .setPositiveButton("Network Setting",
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
                .setNegativeButton("Exit",
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

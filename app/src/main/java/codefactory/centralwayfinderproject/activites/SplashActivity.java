package codefactory.centralwayfinderproject.activites;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import codefactory.centralwayfinderproject.R;

public class SplashActivity extends ActionBarActivity {


    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);                   // Setting toolbar as the ActionBar with setSupportActionBar() call


        /*
            Test if user has Location Services turned on
         */
        LocationManager lm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        //GPS
        try {

            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

        }catch(Exception genericEx){} // I know catching generics is bad, lay off me im tired

        //Network
        try{

            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        } catch(Exception genericEx){}


        //Show dialog if not enabled
        if (!gps_enabled && !network_enabled) locationPrompt();




        /*
            Network Test
         */
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = cm.getActiveNetworkInfo();

        if (net != null && net.isConnectedOrConnecting()) networkPrompt();


        /*
            if connected to internet, check connect to mycentral server

            TEST CONNECTION TO SERVER!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

            Webservice messages
            checkServiceConn() && checkDBConn() == true......
         */



    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    /*
        Prompts user to turn on Location/Network services
     */
    public void locationPrompt(){

        final Context context = getApplicationContext();
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage("Please Enable Location Services");

            /*
                User taken to location settings
             */
        dialog.setPositiveButton(context.getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(myIntent);

            }
        });


            /*
                User Exits application instead of turning on location
                (no fun)
             */
        dialog.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });


        dialog.show();
    }





    /*
        Prompts user to turn on network
     */
    public void networkPrompt(){

        final Context context = getApplicationContext();
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage("Please Enable Network");

            /*
                User taken to location settings
             */
        dialog.setPositiveButton(context.getResources().getString(R.string.open_network_settings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                Intent myIntent = new Intent( Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
                context.startActivity(myIntent);

            }
        });


            /*
                User Exits application instead of turning on location
                (no fun)
             */
        dialog.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });


        dialog.show();
    }
}

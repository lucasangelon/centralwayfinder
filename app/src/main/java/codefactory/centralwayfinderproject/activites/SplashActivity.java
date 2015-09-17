package codefactory.centralwayfinderproject.activites;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import codefactory.centralwayfinderproject.R;

public class SplashActivity extends Activity implements OnClickListener {

    private Button btn_startApp;

    //SOAP vars
    private final String NAMESPACE = "http://tempuri.org/"; //namespace is derived from the ?wsdl
    private final String URL = "http://student.mydesign.central.wa.edu.au/cf_Wayfinding_WebService/WF_Service.svc";
    //check service conn
    private final String SOAP_ACTION_CHECK_SERVICE_CONN = "http://tempuri.org/WF_Service_Interface/checkServiceConn";
    private final String METHOD_CHECK_SERVICE_CONN = "checkServiceConn";
    //also check db conn
    private final String SOAP_ACTION_CHECK_DB_CONN = "http://tempuri.org/WF_Service_Interface/checkDBConn";
    private final String METHOD_CHECK_DB_CONN = "checkDBConn";

    boolean checkServiceResult;
    boolean checkServiceExceptionCaught;
    String exceptionString;

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

        AsyncCallWSCheckServiceConn checkServiceConnAST = new AsyncCallWSCheckServiceConn();
        checkServiceConnAST.execute();

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


    //check web service connection
    private class AsyncCallWSCheckServiceConn extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(String... params) {
            checkServiceConn();
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            if (!checkServiceResult){
                Toast.makeText(SplashActivity.this, "Error Connecting to web service", Toast.LENGTH_SHORT).show();
            }
        }


        @Override
        protected void onProgressUpdate(Void... values) {

        }

        public void checkServiceConn() {

            //theres probably a better way to pass around results but these global bools work ftm
            //i think rather than using public global vars i should create an interface
            checkServiceResult = false;
            checkServiceExceptionCaught = false;

            //Create request
            SoapObject request = new SoapObject(NAMESPACE, METHOD_CHECK_SERVICE_CONN);

            //Create envelope
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            //Set output SOAP object
            envelope.setOutputSoapObject(request);
            //Create HTTP call object
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {
                //Invoke web service
                androidHttpTransport.call(SOAP_ACTION_CHECK_SERVICE_CONN, envelope);
                //Get the response
                //using soapprimitive for a simple primitive (in this case boolean) response,
                //if it was an object or anything with multiple elements we'd use soapobject
                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                //Assign response
                checkServiceResult = Boolean.valueOf(response.toString());
                Log.d("Soap action getDBconn:", "SUCCESS");

            } catch (Exception e) {
                e.printStackTrace();
                checkServiceResult = false;
                checkServiceExceptionCaught = true;
                Log.d("Soap action getDBconn:", "FAIL");
                exceptionString = e.toString();
            }

        }

    }



}

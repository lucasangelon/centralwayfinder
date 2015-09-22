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

import java.util.ArrayList;

import codefactory.centralwayfinderproject.R;
import codefactory.centralwayfinderproject.models.Campus;

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
    //getCampus soap methods
    private final String SOAP_ACTION_GET_CAMPUSES = "http://tempuri.org/WF_Service_Interface/SearchCampus";
    private final String METHOD_GET_CAMPUSES = "SearchCampus";

    //soap error checking vars (probably a better way of doing this)
    boolean checkServiceResult;
    boolean checkServiceExceptionCaught;
    boolean checkDBResult;
    boolean checkDBExceptionCaught;
    boolean getCampusResult;
    boolean getCampusExceptionCaught;
    String exceptionString;

    ArrayList<Campus> campusList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Initialise Button and add listener
        btn_startApp = (Button) findViewById(R.id.btnFirstClick);
        btn_startApp.setOnClickListener(this);

        //Checking Networking Connection


        if (!isOnline()) {

            //Prompt user to connect to network or exit application
            noNetworkConnectionDialog();
            Log.d("Not Online", "NOT CONNECTED TO THE INTERNET");
        } else {
            //check web service connection and retrieve campus list (if connection available)
            AsyncCallWS checkServiceConnAST = new AsyncCallWS();
            checkServiceConnAST.execute();
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
            //Go to Menu Activity
            Intent intent = new Intent(this, MenuActivity.class);
            intent.putExtra("campuses", campusList);
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


    //check web service connection
    private class AsyncCallWS extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(String... params) {
            if (checkServiceConn()){
                getCampuses();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            if (!checkServiceResult){
                Toast.makeText(SplashActivity.this, "Error Connecting to web service\nOnline content may not be available", Toast.LENGTH_SHORT).show();
            }

        }


        @Override
        protected void onProgressUpdate(Void... values) {

        }

        public boolean checkServiceConn() {

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

            return checkServiceResult;

        }

        public void getCampuses() {

            getCampusResult = false;
            getCampusExceptionCaught = false;
            campusList = new ArrayList<>();
            //Create request
            SoapObject request = new SoapObject(NAMESPACE, METHOD_GET_CAMPUSES);
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
                androidHttpTransport.call(SOAP_ACTION_GET_CAMPUSES, envelope);
                //Get the response
                SoapObject response = (SoapObject) envelope.bodyIn;
                //get the array of campus objects
                SoapObject responseTierOne = (SoapObject) response.getProperty(0);
                //for each object in that array

                for (int i = 0; i<responseTierOne.getPropertyCount(); i++){
                    //parse into a soapobject
                    SoapObject responseTierTwo = (SoapObject)responseTierOne.getProperty(i);
                    Campus campus = new Campus();

                    //and pull out the fields
                    //... fsr field names aren't working - i'll look into that

                    campus.campusID = responseTierTwo.getPropertyAsString(0);
                    campus.campusName = responseTierTwo.getPropertyAsString(3);
                    campus.campusLong = Double.parseDouble(responseTierTwo.getProperty(2).toString()); //casting (Double) didnt work
                    campus.campusLat = Double.parseDouble(responseTierTwo.getProperty(1).toString());
                    campus.campusZoom = Double.parseDouble(responseTierTwo.getProperty(4).toString());

                    //campus.campusID = responseTierTwo.getProperty("campusID").toString(); //not sure if theres a dif btween this
                    //campus.campusID = responseTierTwo.getPropertyAsString("campusID");    //and this
                    //campus.campusName = responseTierTwo.getPropertyAsString("campusName");
                    //campus.campusLong = Double.parseDouble(responseTierTwo.getProperty("campusLong")); //try casting .. doesnt work, have to parse
                    //campus.campusLat = Double.parseDouble(responseTierTwo.getProperty("campusLat"));
                    //campus.campusZoom = Double.parseDouble(responseTierTwo.getProperty("campusZoom"));

                    //add to local list
                    campusList.add(campus);
                }

                getCampusResult = true;
                Log.d("Soap act. getCampuses:", "SUCCESS");

            } catch (Exception e) {
                e.printStackTrace();
                getCampusResult = false;
                getCampusExceptionCaught = true;
                Log.d("Soap act. getCampuses:", "FAIL");
                exceptionString = e.toString();
            }

        }

    }



}

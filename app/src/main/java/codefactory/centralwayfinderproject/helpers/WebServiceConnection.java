package codefactory.centralwayfinderproject.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

import codefactory.centralwayfinderproject.dao.CampusDataSource;
import codefactory.centralwayfinderproject.models.Campus;

/**
 * Created by Gustavo on 20/10/2015.
 * This class will handle all events from the webservice
 */
public class WebServiceConnection {

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
    Context mContext;

    public WebServiceConnection(Context mContext) {
        this.mContext = mContext;
        //check web service connection and retrieve campus list (if connection available)
        AsyncCallWS checkServiceConnAST = new AsyncCallWS();
        checkServiceConnAST.execute();
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

                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setMessage("Error Connecting to web service\n" + "Online content may not be available")
                            .setTitle("WEB SERVICE ERROR")
                            .setCancelable(false)
                 /*
                    Exits app if user
                 */
                            .setNegativeButton("Exit App",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            ((Activity)(mContext)).finish();;
                                        }
                                    }
                            );
                    AlertDialog alert = builder.create();
                    alert.show();

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
                    //parse into a soap object
                    SoapObject responseTierTwo = (SoapObject)responseTierOne.getProperty(i);
                    Campus campus = new Campus();

                    //and pull out the fields
                    campus.setCampusID(responseTierTwo.getPropertyAsString(0));
                    campus.setCampusName(responseTierTwo.getPropertyAsString(3));
                    campus.setCampusLong(Double.parseDouble(responseTierTwo.getProperty(2).toString()));
                    campus.setCampusLat(Double.parseDouble(responseTierTwo.getProperty(1).toString()));
                    campus.setCampusZoom(Double.parseDouble(responseTierTwo.getProperty(4).toString()));

                    //Save campus on the local database
                    CampusDataSource campusDataSource = new CampusDataSource(mContext);
                    campusDataSource.open();
                    campusDataSource.insertCampus(campus);

                    //campusList.add(campus);
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

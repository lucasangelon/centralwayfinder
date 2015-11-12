package codefactory.centralwayfinderproject.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.Proxy;
import java.util.ArrayList;

import codefactory.centralwayfinderproject.R;
import codefactory.centralwayfinderproject.dao.BuildingDataSource;
import codefactory.centralwayfinderproject.dao.CampusDataSource;
import codefactory.centralwayfinderproject.dao.RoomDataSource;
import codefactory.centralwayfinderproject.models.Building;
import codefactory.centralwayfinderproject.models.Campus;
import codefactory.centralwayfinderproject.models.Room;

/**
 * Created by Gustavo on 20/10/2015.
 * This class will handle all events from the webservice
 */
public class WebServiceConnection {

    //SOAP vars
    private final String NAMESPACE = "http://tempuri.org/"; //namespace is derived from the ?wsdl
    private final String URL = "http://student.mydesign.central.wa.edu.au/cf_Wayfinding_WebService/WF_Service.svc";
    private final String SOAP_ACTION = "http://tempuri.org/WF_Service_Interface/";
    private final String METHOD_CHECK_SERVICE_CONN = "checkServiceConn";
    private final String METHOD_CHECK_DB_CONN = "checkDBConn";
    private final String METHOD_GET_CAMPUSES = "SearchCampus";
    private final String METHOD_GET_ROOMS_BY_CAMPUS = "SearchRooms";
    private final String METHOD_GET_SERVICE_BY_CAMPUS = "SearchServices";
    private final String METHOD_GET_BUILDING_BY_ROOM = "ResolvePath";

    boolean checkServiceResult;
    public FetchData  checkServiceConnAST;

    //Variables
    Context mContext;
    int option_method;
    int roomId;

    /**
     * Create a instance of WebServiceConnection object
     * @param mContext
     * @param option - Which method do you want to use in your AsyncTask
     */
    public WebServiceConnection(Context mContext,int option) {
        this.mContext = mContext;
        this.option_method = option;

        //check web service connection and retrieve campus list (if connection available)
        checkServiceConnAST = new FetchData ();
    }

    /**
     * Create a instance of WebServiceConnection object
     * @param mContext
     * @param option - Which method do you want to use in your AsyncTask
     * @param roomId - Which room you're looking for
     */
    public WebServiceConnection(Context mContext,int option, int roomId) {
        this.mContext = mContext;
        this.option_method = option;
        this.roomId = roomId;

        //check web service connection and retrieve campus list (if connection available)
        checkServiceConnAST = new FetchData ();
    }

    //check web service connection
    public class FetchData extends AsyncTask<String, Void, Void> {

        //Variables
        CampusDataSource campusDataSource;
        RoomDataSource roomDataSource;
        BuildingDataSource buildingDataSource;
        Useful useful;
        Dialog dialog;
        SoapObject request, soapResult;

        public FetchData() {
        }

        @Override
        protected void onPreExecute() {

            dialog = new Dialog(mContext);
            dialog.setContentView(R.layout.loading_popup);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.show();

        }

        @Override
        protected Void doInBackground(String... params) {
            if (checkServiceConn()){

                switch (option_method) {
                    case 1:
                        getCampusesFromWebService();
                        //hardCodeDB.Search_Campus(mContext);
                        break;
                    case 2:
                        getRoomsByCampusFromWebService();
                        //hardCodeDB.SearchRooms(mContext);
                        break;
                    case 3:
                        getBuildingByRoomFromWebService();
                        break;
                    case 4:
                        getServicesByCampusFromWebService();
                        break;
                }
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
                                        ((Activity) (mContext)).finish();
                                    }
                                }
                        );
                AlertDialog alert = builder.create();
                alert.show();

            }else{
                dialog.dismiss(); // Close loading popup after execute doInBackground is method
            }

        }


        @Override
        protected void onProgressUpdate(Void... values) {

        }

        private void getEnvelope(String action) {
            HttpTransportSE http = new HttpTransportSE(Proxy.NO_PROXY,URL,60000);
            http.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.setAddAdornments(false);
            envelope.setOutputSoapObject(request);

            try
            {
                http.call(SOAP_ACTION + action, envelope);
                soapResult = (SoapObject) envelope.getResponse();
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        }

        // checkServiceConn Methods
        public boolean checkServiceConn() {

            checkServiceResult = false;

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
                androidHttpTransport.call(SOAP_ACTION + METHOD_CHECK_SERVICE_CONN, envelope);
                //Get the response
                //using soap primitive for a simple primitive (in this case boolean) response
                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                //Assign response
                checkServiceResult = Boolean.valueOf(response.toString());
                Log.d(METHOD_CHECK_SERVICE_CONN, "SUCCESS");

            } catch (Exception e) {
                e.printStackTrace();
                checkServiceResult = false;
                Log.d(METHOD_CHECK_SERVICE_CONN, "FAIL");
            }

            return checkServiceResult;

        }

        public void getCampusesFromWebService() {

            request = new SoapObject(NAMESPACE, METHOD_GET_CAMPUSES);

            getEnvelope(METHOD_GET_CAMPUSES);

            try {

                for (int i = 0; i<soapResult.getPropertyCount(); i++){
                    //parse into a soap object
                    SoapObject internalResult = (SoapObject)soapResult.getProperty(i);
                    Campus campus = new Campus();

                    //and pull out the fields
                    campus.setCampusID(internalResult.getPropertyAsString(0));
                    campus.setCampusName(internalResult.getPropertyAsString(1));
                    campus.setCampusVersion(internalResult.getPropertyAsString(2));
                    campus.setCampusLong(Double.parseDouble(internalResult.getPropertyAsString(3)));
                    campus.setCampusLat(Double.parseDouble(internalResult.getPropertyAsString(4)));
                    campus.setCampusZoom(Double.parseDouble(internalResult.getPropertyAsString(5)));

                    //Save campus on the local database
                    campusDataSource = new CampusDataSource(mContext);
                    campusDataSource.insertCampus(campus);
                }

                Log.d(METHOD_GET_CAMPUSES, "SUCCESS");

            } catch (Exception e) {
                e.printStackTrace();
                Log.d(METHOD_GET_CAMPUSES, "FAIL");
            }

        }

        public void getBuildingByRoomFromWebService() {

            Building building = new Building();
            ArrayList<String> maps = new ArrayList();

            request = new SoapObject(NAMESPACE, METHOD_GET_BUILDING_BY_ROOM);

            //Adding Room Id and Disability option as arguments
            //request.addProperty("WaypointID",roomId);
            //request.addProperty("Disability",useful.getAccessibilityOption());

            request.addProperty("WaypointID",3);
            request.addProperty("Disability", true);

            getEnvelope(METHOD_GET_BUILDING_BY_ROOM);

            try {

                building.setId(1);
                building.setImage(((SoapObject) soapResult.getProperty(0)).getPropertyAsString(3));
                building.setName(((SoapObject) soapResult.getProperty(0)).getPropertyAsString(2));
                building.setLongitude(Double.parseDouble(((SoapObject) soapResult.getProperty(0)).getPropertyAsString(0)));
                building.setLatitude(Double.parseDouble(((SoapObject) soapResult.getProperty(0)).getPropertyAsString(1)));

                SoapObject internalResult = ((SoapObject) soapResult.getProperty(1));
                for (int i=0; i< internalResult.getPropertyCount(); i++){
                    maps.add(internalResult.getProperty(i).toString());
                }

                //Save building on the local database
                buildingDataSource = new BuildingDataSource(mContext);
                buildingDataSource.insertBuilding(building);

                Log.d(METHOD_GET_BUILDING_BY_ROOM, "SUCCESS");

            } catch (Exception e) {
                e.printStackTrace();
                Log.d(METHOD_GET_BUILDING_BY_ROOM, "FAIL");
            }

        }

        public void getRoomsByCampusFromWebService() {
            //Variables declaration
            Campus campus;
            useful = new Useful(mContext);
            campus = useful.getDefaultCampus();

            request = new SoapObject(NAMESPACE, METHOD_GET_ROOMS_BY_CAMPUS);

            request.addProperty("CampusID", campus.getCampusID());

            getEnvelope(METHOD_GET_ROOMS_BY_CAMPUS);

            try {
                for (int i = 0; i<soapResult.getPropertyCount(); i++){
                    //parse into a soap object
                    SoapObject internalResult = (SoapObject)soapResult.getProperty(i);
                    Room room = new Room();

                    //and pull out the fields
                    room.setRoomID(Integer.parseInt(internalResult.getPropertyAsString(0)));
                    room.setRoomName(internalResult.getPropertyAsString(1));
                    room.setBuildingID(Integer.parseInt(internalResult.getPropertyAsString(2)));
                    room.setRoomImage(internalResult.getPropertyAsString(3));

                    //Save room in the local database
                    roomDataSource = new RoomDataSource(mContext);
                    roomDataSource.insertRoom(room);
                }
                Log.d(METHOD_GET_ROOMS_BY_CAMPUS, "SUCCESS");

            } catch (Exception e) {
                e.printStackTrace();
                Log.d(METHOD_GET_ROOMS_BY_CAMPUS, "FAIL");
            }

        }

        public void getServicesByCampusFromWebService() {
            //Variables declaration
            Campus campus;
            useful = new Useful(mContext);
            campus = useful.getDefaultCampus();

            //Create request
            SoapObject request = new SoapObject(NAMESPACE, METHOD_GET_SERVICE_BY_CAMPUS);

            //Adding Campus Id as an argument at request object
            request.addProperty("CampusID",campus.getCampusID());

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
                androidHttpTransport.call(SOAP_ACTION + METHOD_GET_SERVICE_BY_CAMPUS, envelope);
                //Get the response
                SoapObject response = (SoapObject) envelope.bodyIn;
                //get the array of campus objects
                SoapObject responseTierOne = (SoapObject) response.getProperty(0);
                //for each object in that array

                for (int i = 0; i<responseTierOne.getPropertyCount(); i++){
                    //parse into a soap object
                    SoapObject responseTierTwo = (SoapObject)responseTierOne.getProperty(i);
                    Room room = new Room();

                    //and pull out the fields
                    room.setRoomID(Integer.parseInt(responseTierTwo.getPropertyAsString(0)));
                    room.setRoomName(responseTierTwo.getPropertyAsString(1));

                    //Save room in the local database
                    roomDataSource = new RoomDataSource(mContext);
                    roomDataSource.insertRoom(room);

                }

                Log.d(METHOD_GET_SERVICE_BY_CAMPUS, "SUCCESS");

            } catch (Exception e) {
                e.printStackTrace();
                Log.d(METHOD_GET_SERVICE_BY_CAMPUS, "FAIL");
            }

        }
    }
}

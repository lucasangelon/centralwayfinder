package codefactory.centralwayfinderproject.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.Proxy;
import java.util.ArrayList;

import codefactory.centralwayfinderproject.activites.GoogleMapActivity;
import codefactory.centralwayfinderproject.activites.MenuActivity;
import codefactory.centralwayfinderproject.activites.SelectCampusActivity;
import codefactory.centralwayfinderproject.activites.ServiceActivity;
import codefactory.centralwayfinderproject.activites.SplashActivity;
import codefactory.centralwayfinderproject.dao.CampusDataSource;
import codefactory.centralwayfinderproject.dao.RoomDataSource;
import codefactory.centralwayfinderproject.models.Campus;
import codefactory.centralwayfinderproject.models.GlobalObject;
import codefactory.centralwayfinderproject.models.Room;

/**
 * Created by Gustavo on 20/10/2015.
 * This class will handle all events from the webservice
 */
public class WebServiceConnection  extends AsyncTask<String, Void, Void> {

    //SOAP vars
    private final String NAMESPACE = "http://tempuri.org/"; //namespace is derived from the ?wsdl
    private final String URL = "http://student.mydesign.central.wa.edu.au/cf_Wayfinding_WebService/WF_Service.svc";
    private final String SOAP_ACTION = "http://tempuri.org/WF_Service_Interface/";
    private final String METHOD_CHECK_SERVICE_CONN = "checkServiceConn";
    private final String METHOD_GET_CAMPUSES = "SearchCampus";
    private final String METHOD_GET_ROOMS_BY_CAMPUS = "SearchRooms";
    private final String METHOD_GET_BUILDING_BY_ROOM = "ResolvePath";
    private final String METHOD_GET_CAMPUS_VERSION = "CampusVersion";
    private final String METHOD_DELETE_IMAGE = "deleteImages";

    //Variables
    boolean checkServiceResult = true;
    CampusDataSource campusDataSource;
    RoomDataSource roomDataSource;
    Useful useful;
    GlobalObject globalObject;
    SoapObject request, soapResult;
    Context mContext;
    int option_method;
    Room room;
    ProgressDialog pd;

    /**
     * Create a instance of WebServiceConnection object
     * @param mContext
     * @param option - Which method do you want to use in your AsyncTask
     */
    public WebServiceConnection(Context mContext,int option) {
        this.mContext = mContext;
        this.option_method = option;
        globalObject = (GlobalObject) mContext.getApplicationContext();
    }

    /**
     * Create a instance of WebServiceConnection object
     * @param mContext
     * @param option - Which method do you want to use in your AsyncTask
     * @param room - Which room you're looking for
     */
    public WebServiceConnection(Context mContext,int option, Room room) {
        this.mContext = mContext;
        this.option_method = option;
        this.room = room;
        globalObject = (GlobalObject) mContext.getApplicationContext();
    }




    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        useful = new Useful(mContext);

        //Display the spinner layout
        pd = new ProgressDialog(mContext);
        pd.setTitle("Loading...");
        pd.setMessage("Please wait.");
        pd.setCancelable(false);
        pd.show();
    }

    @Override
    protected Void doInBackground(String... params) {
        //if (checkServiceConn()){

            switch (option_method) {
                case 0:
                    //getCampusVersion();
                    break;
                case 1:
                    getCampusesFromWebService();
                    break;
                case 2:
                    getRoomsByCampusFromWebService();
                    break;
                case 3:
                    getBuildingByRoomFromWebService();
                    break;
                case 4:
                    deleteImagesFromWebService();
                    break;

            }
        //}

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if (!checkServiceResult){
            //Display a message if the server is offline
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage("Error Connecting to web service\n" + "Online content may not be available")
                    .setTitle("WEB SERVICE ERROR")
                    .setCancelable(false)

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

            Intent intent;

            if (mContext.getClass() == MenuActivity.class) {
                intent = new Intent(mContext.getApplicationContext(), GoogleMapActivity.class);
                mContext.startActivity(intent);

            } else if (mContext.getClass() == SplashActivity.class) {
                if (useful.getIsFirstTimeOption()){
                    intent = new Intent(mContext.getApplicationContext(), SelectCampusActivity.class);
                    mContext.startActivity(intent);
                }else{
                    intent = new Intent(mContext.getApplicationContext(), MenuActivity.class);
                    mContext.startActivity(intent);
                }

            } else if (mContext.getClass() == ServiceActivity.class) {
                intent = new Intent(mContext.getApplicationContext(), GoogleMapActivity.class);
                mContext.startActivity(intent);

            } else if (mContext.getClass() == SelectCampusActivity.class) {
                intent = new Intent(mContext.getApplicationContext(), MenuActivity.class);
                mContext.startActivity(intent);

            }
            //Close the spinner message
            pd.dismiss();
        }
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

    public void getCampusVersion() {

        useful = new Useful(mContext);
        Campus campus = useful.getDefaultCampus();

        request = new SoapObject(NAMESPACE, METHOD_GET_CAMPUS_VERSION);
        //Adding Campus Version as an arguments
        request.addProperty("CampusID",campus.getCampusID());

        getEnvelope(METHOD_GET_CAMPUS_VERSION);

        try {

            /*for (int i = 0; i<soapResult.getPropertyCount(); i++){
                //parse into a soap object
                SoapObject internalResult = (SoapObject)soapResult.getProperty(i);
                Campus campus = new Campus();

                //and pull out the fields
                campus.setCampusID(internalResult.getPropertyAsString(0));
                campus.setCampusName(internalResult.getPropertyAsString(1));
                campus.setCampusVersion(internalResult.getPropertyAsString(2));
                campus.setCampusLong(Double.parseDouble(internalResult.getPropertyAsString(4)));
                campus.setCampusLat(Double.parseDouble(internalResult.getPropertyAsString(3)));
                campus.setCampusZoom(Double.parseDouble(internalResult.getPropertyAsString(5)));

                //Save campus on the local database
                campusDataSource = new CampusDataSource(mContext);
                campusDataSource.insertCampus(campus);
            }*/

            Log.d(METHOD_GET_CAMPUSES, "SUCCESS");

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(METHOD_GET_CAMPUSES, "FAIL");
        }

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
                campus.setCampusLong(Double.parseDouble(internalResult.getPropertyAsString(4)));
                campus.setCampusLat(Double.parseDouble(internalResult.getPropertyAsString(3)));
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

    public Boolean getBuildingByRoomFromWebService() {

        Boolean result;
        ArrayList<String> maps = new ArrayList();
        useful = new Useful(mContext);

        request = new SoapObject(NAMESPACE, METHOD_GET_BUILDING_BY_ROOM);

        //Adding Room Id and Accessibility option as arguments
        request.addProperty("WaypointID",room.getRoomID());
        request.addProperty("Disability",useful.getAccessibilityOption());

        getEnvelope(METHOD_GET_BUILDING_BY_ROOM);

        try {

            globalObject.setBuildingID(room.getBuildingID());
            globalObject.setBuildingImage(((SoapObject) soapResult.getProperty(0)).getPropertyAsString(3));
            globalObject.setBuildingName(((SoapObject) soapResult.getProperty(0)).getPropertyAsString(2));
            globalObject.setBuildingLongitude(Double.parseDouble(((SoapObject) soapResult.getProperty(0)).getPropertyAsString(1)));
            globalObject.setBuildingLatitude(Double.parseDouble(((SoapObject) soapResult.getProperty(0)).getPropertyAsString(0)));

            SoapObject internalResult = ((SoapObject) soapResult.getProperty(1));
            for (int i=0; i< internalResult.getPropertyCount(); i++){
                maps.add(internalResult.getProperty(i).toString());
            }
            globalObject.setMaps(maps);

            Log.d(METHOD_GET_BUILDING_BY_ROOM, "SUCCESS");
            result=true;

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(METHOD_GET_BUILDING_BY_ROOM, "FAIL");
            result=false;
        }

        return result;

    }

    public void getRoomsByCampusFromWebService() {
        //Variables declaration
        Campus campus;
        //useful = new Useful(mContext);
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

    public void deleteImagesFromWebService() {
        //Variables declaration


        request = new SoapObject(NAMESPACE, METHOD_DELETE_IMAGE);

        request.addProperty("urls", globalObject.getMaps());

        getEnvelope(METHOD_DELETE_IMAGE);

        try {
            soapResult.getPropertyCount();
            Log.d(METHOD_DELETE_IMAGE, "SUCCESS");

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(METHOD_GET_ROOMS_BY_CAMPUS, "FAIL");
        }

    }

}

package codefactory.centralwayfinderproject.helpers;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.util.Hashtable;
import java.util.Map;
import java.util.TreeMap;

import codefactory.centralwayfinderproject.models.Campus;

/**
 * Created by Dillon on 4/11/2015.
 */
public class kSOAPManager extends AsyncTask<String, Void, Void>{
    private static final String NAMESPACE = "http://tempuri.org/";
    private static final String MAIN_REQUEST_URL = "http://student.mydesign.central.wa.edu.au/cf_Wayfinding_WebService/WF_Service.svc?wsdl";

    private static final String CHECKSERVICECONN_METHODNAME = "checkServiceConn";
    private static final String CHECKSERVICECONN_ACTION = "http://tempuri.org/WF_Service_Interface/checkServiceConn";

    private static final String CHECKDBCONN_METHODNAME = "checkDBConn";
    private static final String CHECKDBCONN_ACTION = "http://tempuri.org/WF_Service_Interface/checkDBConn";

    private static final String SEARCHCAMPUS_METHODNAME = "SearchCampus";
    private static final String SEARCHCAMPUS_ACTION = "http://tempuri.org/WF_Service_Interface/SearchCampus";

    private static final String CAMPUSVERSION_METHODNAME = "CampusVersion";
    private static final String CAMPUSVERSION_ACTION = "http://tempuri.org/WF_Service_Interface/CampusVersion";

    private static final String SEARCHROOMS_METHODNAME = "SearchRooms";
    private static final String SEARCHROOMS_ACTION = "http://tempuri.org/WF_Service_Interface/SearchRooms";

    private static final String SEARCHSERVICES_METHODNAME = "SearchMainRooms";
    private static final String SEARCHSERVICES_ACTION = "http://tempuri.org/WF_Service_Interface/SearchMainRooms";

    private static final String RESOLVEPATH_METHODNAME = "ResolvePath";
    private static final String RESOLVEPATH_ACTION = "http://tempuri.org/WF_Service_Interface/ResolvePath";

    private boolean lockOut = false;
    private String action;
    private SoapObject soapResult;
    private SoapObject request;

    // checkServiceConn Methods
    public boolean checkServiceConn() {
        action = CHECKSERVICECONN_ACTION;
        request = new SoapObject(NAMESPACE, CHECKSERVICECONN_METHODNAME);

        try
        {
            execute().get();
        }
        catch (Exception ex){}

        return checkServiceConnParse();
    }

    public void checkServiceConnAsync()
    {
        if(!lockOut)
        {
            action = CHECKSERVICECONN_ACTION;
            request = new SoapObject(NAMESPACE, CHECKSERVICECONN_METHODNAME);

            lockOut = true;
            execute();
        }
    }

    boolean checkServiceConnParse()
    {
        return soapResult.toString().equals("true");
    }

    // checkDbConn Methods
    public boolean checkDBConn() {
        action = CHECKDBCONN_ACTION;
        request = new SoapObject(NAMESPACE, CHECKDBCONN_METHODNAME);

        try
        {
            execute().get();
        }
        catch (Exception ex){}

        return checkDBConnParse();
    }

    public void checkDBConnAsync()
    {
        if(!lockOut)
        {
            action = CHECKDBCONN_ACTION;
            request = new SoapObject(NAMESPACE, CHECKDBCONN_METHODNAME);

            lockOut = true;
            execute();
        }
    }

    boolean checkDBConnParse()
    {
        return soapResult.toString().equals("true");
    }

    // SearchCampus Methods
    public Map<String, Campus> SearchCampus()
    {
        action = SEARCHCAMPUS_ACTION;
        request = new SoapObject(NAMESPACE, SEARCHCAMPUS_METHODNAME);

        try
        {
            execute().get();
        }
        catch (Exception ex){}

        return SearchCampusParse();
    }

    public void SearchCampusAsync()
    {
        if(!lockOut)
        {
            action = SEARCHCAMPUS_ACTION;
            request = new SoapObject(NAMESPACE, SEARCHCAMPUS_METHODNAME);

            lockOut = true;
            execute();
        }
    }

    Map<String, Campus> SearchCampusParse()
    {
        Map<String, Campus> result = new TreeMap<String, Campus>();
        for(int i = 0; i < soapResult.getPropertyCount(); i++)
        {
            SoapObject row = (SoapObject) soapResult.getProperty(i);
            String campusID = row.getPropertyAsString(0);
            String campusName = row.getPropertyAsString(1);
            double campusLat = Double.parseDouble(row.getPropertyAsString(2));
            double campusLong = Double.parseDouble(row.getPropertyAsString(3));
            double campusZoom = Double.parseDouble(row.getPropertyAsString(4));
            result.put(campusID, new Campus(campusID, campusName, campusLat, campusLong, campusZoom));
        }
        return result;
    }

    // CampusVersion Methods
    public int CampusVersion(String CampusID) {
        action = CAMPUSVERSION_ACTION;
        request = new SoapObject(NAMESPACE, CAMPUSVERSION_METHODNAME);
        request.addProperty("CampusID", CampusID);

        try
        {
            execute().get();
        }
        catch (Exception ex){}

        return CampusVersionParse();
    }

    public void CampusVersionAsync(String CampusID)
    {
        if(!lockOut)
        {
            action = CAMPUSVERSION_ACTION;
            request = new SoapObject(NAMESPACE, CAMPUSVERSION_METHODNAME);
            request.addProperty("CampusID", CampusID);

            lockOut = true;
            execute();
        }
    }

    int CampusVersionParse()
    {
        return Integer.parseInt(soapResult.toString());
    }

    //SearchRooms Methods
    public Map<Integer, String> SearchRooms(String CampusID)
    {
        action = SEARCHROOMS_ACTION;
        request = new SoapObject(NAMESPACE, SEARCHROOMS_METHODNAME);
        request.addProperty("CampusID", CampusID);

        try
        {
            execute().get();
        }
        catch (Exception ex){}

        return SearchRoomsParse();
    }

    public void SearchRoomsAsync(String CampusID)
    {
        if(!lockOut)
        {
            action = SEARCHROOMS_ACTION;
            request = new SoapObject(NAMESPACE, SEARCHROOMS_METHODNAME);
            request.addProperty("CampusID", CampusID);

            lockOut = true;
            execute();
        }
    }

    Map<Integer, String> SearchRoomsParse()
    {
        Map<Integer, String> result = new TreeMap<Integer, String>();
        for(int i = 0; i < soapResult.getPropertyCount(); i++)
        {
            SoapObject row = (SoapObject) soapResult.getProperty(i);
            int waypointID = Integer.parseInt(row.getPropertyAsString(0));
            String waypointName = row.getPropertyAsString(1);
            result.put(waypointID, waypointName);
        }
        return result;
    }

    //SearchServices Methods
    public Map<Integer, String> SearchServices(String CampusID)
    {
        action = SEARCHSERVICES_ACTION;
        request = new SoapObject(NAMESPACE, SEARCHSERVICES_METHODNAME);
        request.addProperty("CampusID", CampusID);

        try
        {
            execute().get();
        }
        catch (Exception ex){}

        return SearchServicesParse();
    }

    public void SearchServicesAsync(String CampusID)
    {
        if(!lockOut)
        {
            action = SEARCHSERVICES_ACTION;
            request = new SoapObject(NAMESPACE, SEARCHSERVICES_METHODNAME);
            request.addProperty("CampusID", CampusID);

            lockOut = true;
            execute();
        }
    }

    Map<Integer, String> SearchServicesParse()
    {
        Map<Integer, String> result = new TreeMap<Integer, String>();
        for(int i = 0; i < soapResult.getPropertyCount(); i++)
        {
            SoapObject row = (SoapObject) soapResult.getProperty(i);
            int waypointID = Integer.parseInt(row.getPropertyAsString(0));
            String waypointName = row.getPropertyAsString(1);
            result.put(waypointID, waypointName);
        }
        return result;
    }

    //ResolvePath Methods
    public Object ResolvePath(int WaypointID, boolean Disability)
    {
        action = RESOLVEPATH_ACTION;
        request = new SoapObject(NAMESPACE, RESOLVEPATH_METHODNAME);
        request.addProperty("WaypointID", WaypointID);
        request.addProperty("Disability", Disability);

        try
        {
            execute().get();
        }
        catch (Exception ex){}

        return ResolvePathParse();
    }

    public void ResolvePathAsync(int WaypointID, boolean Disability)
    {
        if(!lockOut)
        {
            action = RESOLVEPATH_ACTION;
            request = new SoapObject(NAMESPACE, RESOLVEPATH_METHODNAME);
            request.addProperty("WaypointID", WaypointID);
            request.addProperty("Disability", Disability);

            lockOut = true;
            execute();
        }
    }

    Object ResolvePathParse()
    {
        return null;
    }

    // Connection manager
    @Override
    protected Void doInBackground(String... params) {
        HttpTransportSE http = new HttpTransportSE(Proxy.NO_PROXY,MAIN_REQUEST_URL,60000);
        http.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.implicitTypes = true;
        envelope.setAddAdornments(false);
        envelope.setOutputSoapObject(request);

        try
        {
            http.call(action, envelope);
            soapResult = (SoapObject) envelope.getResponse();
        }
        catch (Exception ex){}
        return null;
    }

    @Override
    protected void onPostExecute(Void v)
    {
        if(lockOut)
        {
            Object result = null;
            switch (action)
            {
                case CHECKSERVICECONN_ACTION:
                {
                    result = checkServiceConnParse();
                }
                case CHECKDBCONN_ACTION:
                {
                    result = checkDBConnParse();
                }
                case SEARCHCAMPUS_ACTION:
                {
                    result = SearchCampusParse();
                }
                case CAMPUSVERSION_ACTION:
                {
                    result = CampusVersionParse();
                }
                case SEARCHROOMS_ACTION:
                {
                    result = SearchRoomsParse();
                }
                case SEARCHSERVICES_ACTION:
                {
                    result = SearchServicesParse();
                }
                case RESOLVEPATH_ACTION:
                {
                    result = ResolvePathParse();
                }
            }
            //RETURN result
        }
    }

    // HELPER CLASSES
    public class SOAP_ResolvePath
    {
        public SOAP_ResolvePath(double CampusToLat, double CampusToLong, String BuildingTitle, String BuildingImage, String[][] Maps)
        {
            campusTo = new double[] { CampusToLat, CampusToLong };
            buildingTitle = BuildingTitle;
            buildingImage = BuildingImage;
            maps = Maps;
        }
        public double[] campusTo;
        public String buildingTitle;
        public String buildingImage;
        public String[][] maps;
    }
}

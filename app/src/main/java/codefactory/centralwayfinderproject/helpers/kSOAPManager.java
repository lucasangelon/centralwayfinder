package codefactory.centralwayfinderproject.helpers;

import android.os.AsyncTask;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.net.Proxy;
import java.net.SocketTimeoutException;

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

    private static final String SEARCHROOMS_METHODNAME = "SearchRooms";
    private static final String SEARCHROOMS_ACTION = "http://tempuri.org/WF_Service_Interface/SearchRooms";

    private static final String SEARCHMAINROOMS_METHODNAME = "SearchMainRooms";
    private static final String SEARCHMAINROOMS_ACTION = "http://tempuri.org/WF_Service_Interface/SearchMainRooms";

    private static final String RESOLVEPATH_METHODNAME = "ResolvePath";
    private static final String RESOLVEPATH_ACTION = "http://tempuri.org/WF_Service_Interface/ResolvePath";

    private boolean lockOut = false;
    private String action;
    private Object result;

    private SoapObject request;

    public void checkServiceConn()
    {
        if(!lockOut)
        {
            action = CHECKSERVICECONN_ACTION;
            request = new SoapObject(NAMESPACE, CHECKSERVICECONN_METHODNAME);

            lockOut = true;
            execute();
        }
    }

    public void checkDBConn()
    {
        if(!lockOut)
        {
            action = CHECKDBCONN_ACTION;
            request = new SoapObject(NAMESPACE, CHECKDBCONN_METHODNAME);

            lockOut = true;
            execute();
        }
    }

    public void SearchCampus()
    {
        if(!lockOut)
        {
            action = SEARCHCAMPUS_ACTION;
            request = new SoapObject(NAMESPACE, SEARCHCAMPUS_METHODNAME);

            lockOut = true;
            execute();
        }
    }

    public void SearchRooms(String CampusID)
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

    public void SearchMainRooms(String CampusID)
    {
        if(!lockOut)
        {
            action = SEARCHMAINROOMS_ACTION;
            request = new SoapObject(NAMESPACE, SEARCHMAINROOMS_METHODNAME);
            request.addProperty("CampusID", CampusID);

            lockOut = true;
            execute();
        }
    }

    public void ResolvePath(int WaypointID, boolean Disability)
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

    // HELPER METHODS
    static SoapSerializationEnvelope getSoapEnvelope(SoapObject request) {
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.implicitTypes = true;
        envelope.setAddAdornments(false);
        envelope.setOutputSoapObject(request);

        return envelope;
    }

    @Override
    protected Void doInBackground(String... params) {
        HttpTransportSE http = new HttpTransportSE(Proxy.NO_PROXY,MAIN_REQUEST_URL,60000);
        http.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");
        SoapSerializationEnvelope envelope = getSoapEnvelope(request);
        try
        {
            http.call(action, envelope);
            SoapObject soapResult = (SoapObject) envelope.bodyIn;
            result = soapResult.toString();
        }
        catch (SocketTimeoutException t) {
            t.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (Exception q) {
            q.printStackTrace();
        }
        return null;
    }



    // HELPER CLASSES
    public class SOAP_SearchCampus
    {
        public SOAP_SearchCampus(String CampusID, String CampusName, double CampusLat, double CampusLong, float CampusZoom)
        {
            campusID = CampusID;
            campusName = CampusName;
            campusLat = CampusLat;
            campusLong = CampusLong;
            campusZoom = CampusZoom;
        }
        public String campusID;
        public String campusName;
        public double campusLat;
        public double campusLong;
        public float campusZoom;

    }

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

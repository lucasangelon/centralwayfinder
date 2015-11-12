package codefactory.centralwayfinderproject.activites;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import codefactory.centralwayfinderproject.R;
import codefactory.centralwayfinderproject.helpers.MultiTouch;

public class IndoorMapsActivity extends AppCompatActivity {
    private MultiTouch img;
    ArrayList<Bitmap> images;
    int currentMap;


    public void displayToast(String s){
        //shortcut for making toast, includes code to centre test
        Toast toast = Toast.makeText(this, s, Toast.LENGTH_SHORT);
        TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
        if( tv != null) tv.setGravity(Gravity.CENTER);
        toast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indoor_maps);

        //Initalise arraylist
        images = new ArrayList<>();

        // Changed in XML
        img = (MultiTouch) findViewById(R.id.mapMultiTouch);

        //img.setImageResource(R.drawable.drow);
        img.setMaxZoom(8f);

        // Create an object for subclass of AsyncTask
        GetXMLTask task = new GetXMLTask();
        // Execute the task
        //task.execute(new String[]{"http://student.mydesign.central.wa.edu.au/cf_Wayfinding_WebService/WF_Service.svc/getImage?path=/Img/drow.jpg"});

        /*
            Change 18/9, Multiple images
        */
        String[] imageURL = {"C:\\Inetpub\\vhosts\\student.mydesign.central.wa.edu.au\\httpdocs\\cf_Wayfinding_WebService\\/Img/TEST635828700889137027.png"};
        task.execute(imageURL);
        //string : C:\Inetpub\vhosts\student.mydesign.central.wa.edu.au\httpdocs\cf_Wayfinding_WebService\/Img/PE0301635824266396694365.png
        //img.setImageResource(R.mipmap.resultpath);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_overlay);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Button button = (Button) dialog.findViewById(R.id.Button01);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_indoor_maps, menu);
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

    /**
     *
     */
    private class GetXMLTask extends AsyncTask<String, Void, String> {
        @Override

        // herp derp downloads images
        protected String doInBackground(String... urls) {
            Bitmap map = null;
            for (String url : urls) {
                Log.d("Stuff",url);
                map = downloadImage(url);
                //Adds to array list of bitmaps
                images.add(map);
                Log.d("Stuff", "Download finished");
            }
            return "I couldnt work this out";
        }

        // Sets the Bitmap returned by doInBackground
        protected void onPostExecute(String success) {
            img.setImageBitmap(images.get(0));
            currentMap = 0;
            Log.d("onPost","Got here!");
        }

        // Creates Bitmap from InputStream and returns it
        private Bitmap downloadImage(String imgpath) {


            //initiales stuff
            Bitmap bitmap = null;
            InputStream stream = null;
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 1;




            try {

                String NAMESPACE = "http://tempuri.org/";
                String METHOD_NAME = "getImage";
                String SOAP_ACTION = "http://tempuri.org/WF_Service_Interface/"+METHOD_NAME;
                String WEB_SERVICE_URL ="http://student.mydesign.central.wa.edu.au/cf_Wayfinding_WebService/WF_Service.svc";


                // sets up soap objects
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("path",imgpath);
                SoapSerializationEnvelope Envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                Envelope.dotNet = true;
                Envelope.setOutputSoapObject(request);


                //calls the webservice
                HttpTransportSE transport = new HttpTransportSE(WEB_SERVICE_URL);
                transport.call(SOAP_ACTION, Envelope);
                SoapPrimitive response = (SoapPrimitive) Envelope.getResponse();


                //SoapObject response = (SoapObject)Envelope.bodyIn;

                //creates image from byte array response
                stream =  new ByteArrayInputStream( Base64.decode(response.toString(),Base64.DEFAULT));
                bitmap = BitmapFactory.
                        decodeStream(stream, null, bmOptions);


                stream.close();
            } catch (Exception e1) {
                e1.printStackTrace();

            }

            return bitmap;
        }


    }

    /*
        Back Button is clicked
     */
    public void imageBack(View v){

        if(currentMap>0){

            currentMap--;
            img.setImageBitmap(images.get(currentMap));

        }else{
            displayToast("No previous map");
        }

    }

    /*
        Back Button is clicked
     */
    public void imageFwd(View v){

        if(currentMap < images.size() -1){

            currentMap++;
            img.setImageBitmap(images.get(currentMap));

        }else{
            displayToast("No Next map");
        }

    }
}

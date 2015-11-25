package codefactory.centralwayfinderproject.activites;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import codefactory.centralwayfinderproject.models.GlobalObject;

public class IndoorMapsActivity extends AppCompatActivity {
    private MultiTouch img;
    ArrayList<Bitmap> images;
    int currentMap;
    private Toolbar toolbar;
    private GlobalObject globalObject;


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

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        //Initalise arraylist
        images = new ArrayList<>();

        img = (MultiTouch) findViewById(R.id.mapMultiTouch);
        img.setMaxZoom(8f);

        globalObject = (GlobalObject) getApplicationContext();
        GetXMLTask task = new GetXMLTask();

        breakString(globalObject.getMaps());

        String[] imageURL = new String[globalObject.getMaps().size()-1];
        int aux = 0;
        for (int x = 0; x < globalObject.getMaps().size(); x++){
            if(globalObject.getMaps().get(x).contains("C:")){
                imageURL[aux] = globalObject.getMaps().get(x);
                aux++;
            }
        }

        task.execute(imageURL);
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
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent;

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_icon1) {
            intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_icon2) {
            intent = new Intent(this, ServiceActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_icon3) {
            Uri uri = Uri.parse("http://central.wa.edu.au/Pages/default.aspx"); // missing 'http://' will cause crash
            intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_icon4) {
            intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
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

    public void breakString(ArrayList<String> text){
         ArrayList<String> tempResult = new ArrayList();
        ArrayList<String> result = new ArrayList();

        //String[] parts = text.split("=");
        for (int x = 0; x < text.size(); x++){
            for (String item : text.get(x).split("="))
            {
                for (String aux : item.split(";")){
                    tempResult.add(aux);
                }
            }
        }

        for (int x = 0; x < tempResult.size(); x++){
            String temp = tempResult.get(x);

            if(!temp.contains("{") && !temp.contains("}") && !temp.contains("string")){
                result.add(temp);
            }
        }

        globalObject.setMaps(result);

    }
}

package codefactory.centralwayfinderproject.activites;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import codefactory.centralwayfinderproject.R;
import codefactory.centralwayfinderproject.helpers.Useful;
import codefactory.centralwayfinderproject.models.GlobalObject;

public class BuildingViewActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private GlobalObject globalObject;
    private ImageView img;
    private Useful util;
    private TextView txtBuilding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buildingview);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        globalObject = (GlobalObject) getApplicationContext();
        util = new Useful(this);
        img = (ImageView)findViewById(R.id.imageView);
        txtBuilding = (TextView) findViewById(R.id.txtBuilding);

        if (!globalObject.getBuildingImage().equals(null) && !globalObject.getBuildingImage().equals("No Image")) {
            GetImageFromServer task = new GetImageFromServer();
            task.execute(globalObject.getBuildingImage());
        }

        txtBuilding.setText(globalObject.getBuildingName());

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

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.locationOption:

                Intent intent = new Intent(this, IndoorMapsActivity.class);
                startActivity(intent);
                break;
        }
    }

    private class GetImageFromServer extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            util.displayLoadingMessage();
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap image = null;
            Log.d("START",".....");
            image = downloadImage(urls[0]);
            /*try {
                image = BitmapFactory.decodeStream((InputStream) new URL(urls[0]).getContent());
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            Log.d("END",".....");
            return image;
        }

        // Sets the Bitmap returned by doInBackground
        protected void onPostExecute(Bitmap image) {
            util.disappearLoadingMessage();
            if(image != null){
                Drawable picture = new BitmapDrawable(getResources(), image);
                img.setBackgroundDrawable(picture);
            }
        }

        // Creates Bitmap from InputStream and returns it
        private Bitmap downloadImage(String imgpath) {

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
                stream =  new ByteArrayInputStream( Base64.decode(response.toString(), Base64.DEFAULT));
                bitmap = BitmapFactory.
                        decodeStream(stream, null, bmOptions);

                stream.close();
            } catch (Exception e1) {
                e1.printStackTrace();

            }

            return bitmap;
        }


    }
}
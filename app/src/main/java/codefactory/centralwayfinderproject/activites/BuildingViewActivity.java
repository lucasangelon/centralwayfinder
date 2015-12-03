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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

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
            task.execute(globalObject.getBuildingImage().substring(globalObject.getBuildingImage().lastIndexOf("\\")));
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
            try {
                image = BitmapFactory.decodeStream((InputStream) new URL("http://student.mydesign.central.wa.edu.au/cf_Wayfinding_WebService/" + urls[0]).getContent());
            } catch (IOException e) {
                e.printStackTrace();
            }
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
    }
}
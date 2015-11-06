package codefactory.centralwayfinderproject.activites;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import codefactory.centralwayfinderproject.R;

public class BuildingViewActivity extends ActionBarActivity implements View.OnClickListener {

    private Toolbar toolbar;

    Button btn_Location;
    Button btn_Search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buildingview);

        btn_Location = (Button) findViewById(R.id.btnLocation);
        btn_Search = (Button) findViewById(R.id.btnSearch);

        btn_Location.setOnClickListener(this);
        btn_Search.setOnClickListener(this);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_icon1) {
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_icon2) {
            Intent intent = new Intent(this, BuildingViewActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_icon3) {
            Uri uri = Uri.parse("http://central.wa.edu.au/Pages/default.aspx"); // missing 'http://' will cause crash
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_icon4) {
            Toast.makeText(getApplicationContext(), "Missing Implementation...",
                    Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLocation:
                //Go to Search Activity
                Intent intent = new Intent(this, IndoorMapsActivity.class);
                startActivity(intent);

                break;
            case R.id.btnSearch:
                intent = new Intent(this, MenuActivity.class);
                startActivity(intent);
        }
    }
}
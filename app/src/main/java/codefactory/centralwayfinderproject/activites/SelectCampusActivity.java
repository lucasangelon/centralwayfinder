package codefactory.centralwayfinderproject.activites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import codefactory.centralwayfinderproject.R;
import codefactory.centralwayfinderproject.dao.CampusDataSource;
import codefactory.centralwayfinderproject.helpers.WebServiceConnection;
import codefactory.centralwayfinderproject.models.Campus;

public class SelectCampusActivity extends AppCompatActivity{

    private ArrayList<Campus> campusesList = new ArrayList();
    private CampusDataSource dataSource;
    private ListView listViewCampus;
    private ArrayList<String> campusNames = new ArrayList();
    private SharedPreferences prefs;
    private WebServiceConnection webServiceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_campus);

        listViewCampus =(ListView) findViewById(R.id.listCampus);

        //Loading toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        //Get all Campus from Database
        dataSource = new CampusDataSource(this);
        campusesList = dataSource.getAllCampus();

        for (int x = 0; x < campusesList.size(); x++){
            campusNames.add(campusesList.get(x).getCampusName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, campusNames);

        listViewCampus.setAdapter(adapter);

        listViewCampus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
                // Get item name on the list
                String value = (String) adapter.getItemAtPosition(position);

                //Save default name campus on the preference file for future's checking
                prefs = getSharedPreferences("Settings", MODE_PRIVATE);
                prefs.edit().putString("defaultCampus", value).commit();
                prefs.edit().putBoolean("firstTime", false).commit();

                //Gets rooms from WebService and save on the database
                getRoomOnWebService();

                //Go to Menu Activity
                Intent intent = new Intent(v.getContext(), MenuActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Getting Room by Campus from WebService and saving those room in the local database
     */
    public void getRoomOnWebService(){

        webServiceConnection = new WebServiceConnection(this,2);
        webServiceConnection.checkServiceConnAST.execute();

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
            Toast.makeText(getApplicationContext(), "Missing Implementation1...",
                    Toast.LENGTH_LONG).show();
            return true;
        }
        if (id == R.id.action_icon3) {
            Uri uri = Uri.parse("http://central.wa.edu.au/Pages/default.aspx"); // missing 'http://' will cause crash
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_icon4) {
            Toast.makeText(getApplicationContext(), "Missing Implementation3...",
                    Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

package codefactory.centralwayfinderproject.activites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import codefactory.centralwayfinderproject.R;
import codefactory.centralwayfinderproject.dao.CampusDataSource;
import codefactory.centralwayfinderproject.helpers.Useful;
import codefactory.centralwayfinderproject.helpers.WebServiceConnection;
import codefactory.centralwayfinderproject.models.Campus;

public class SelectCampusActivity extends AppCompatActivity{

    private ArrayList<Campus> campusesList = new ArrayList();
    private CampusDataSource dataSource;
    private ListView listViewCampus;
    private ArrayList<String> campusNames = new ArrayList();
    private SharedPreferences prefs;
    private WebServiceConnection webServiceConnection;
    private Useful useful;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_campus);

        listViewCampus =(ListView) findViewById(R.id.listCampus);

        //Get all Campus from Database
        dataSource = new CampusDataSource(this);
        campusesList = dataSource.getAllCampus();

        //Populating arrayList with campus' name
        for (int x = 0; x < campusesList.size(); x++){
            campusNames.add(campusesList.get(x).getCampusName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.simple_list_item, campusNames);
        listViewCampus.setAdapter(adapter);

        //Getting the list item selected
        listViewCampus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
                // Get item name on the list
                String value = (String) adapter.getItemAtPosition(position);

                //Save campus on the preference file for future's checking, also set up first time to false
                prefs = getSharedPreferences("Settings", MODE_PRIVATE);
                prefs.edit().putBoolean("firstTime", false).commit();
                useful = new Useful(getApplicationContext());
                useful.setDefaultCampus(campusesList.get(position));

                //Getting rooms from WebService and save on the database
                getRoomOnWebService();

                //Getting services from WebService and save on the database
                //getServicesOnWebService();

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

    /**
     * Getting Services by Campus from WebService and saving those room in the local database
     */
    public void getServicesOnWebService(){

        webServiceConnection = new WebServiceConnection(this,4);
        webServiceConnection.checkServiceConnAST.execute();

    }

}

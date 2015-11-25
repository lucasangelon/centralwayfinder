package codefactory.centralwayfinderproject.activites;

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

        useful = new Useful(this);
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

                boolean isFirstTime = useful.getIsFirstTimeOption();

                if(isFirstTime){
                    firstTimeAction(campusesList.get(position));
                } else{
                    changeCampus(campusesList.get(position));
                }
            }
        });
    }

    /**
     * Getting Room by Campus from WebService and saving those room in the local database
     */
    public void getRoomOnWebService(){
        webServiceConnection = new WebServiceConnection(this,2);
        webServiceConnection.execute();

    }

    /**
     * Action if the app running for the first time
     */
    public void firstTimeAction(Campus selectedCampus){
        useful.setDefaultCampus(selectedCampus);
        getRoomOnWebService();
        useful.setIsFirstTimeOption(false);

    }

    /**
     * Action if the app running for the first time
     */
    public void changeCampus(Campus selectedCampus){
        Campus oldCampus = useful.getDefaultCampus();

        if(oldCampus.getCampusName() == selectedCampus.getCampusName()){
            //Update campus ONLY if version is different
            if(oldCampus.getCampusVersion() != selectedCampus.getCampusVersion()){
                dataSource.updateCampus(selectedCampus);
                useful.setDefaultCampus(selectedCampus);
                dataSource.dropTable();
                getRoomOnWebService();
            }
        }else{//Update if name IS different
            dataSource.updateCampus(selectedCampus);
            useful.setDefaultCampus(selectedCampus);
            dataSource.dropTable();
            getRoomOnWebService();
        }

    }

}

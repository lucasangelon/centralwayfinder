package codefactory.centralwayfinderproject.activites;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;

import codefactory.centralwayfinderproject.R;
import codefactory.centralwayfinderproject.dao.RoomDataSource;
import codefactory.centralwayfinderproject.database.HardCodeDB;
import codefactory.centralwayfinderproject.models.Building;
import codefactory.centralwayfinderproject.models.Room;

public class MenuActivity extends Activity implements OnClickListener {

    Button btn_services, btn_centralWeb, btn_settings;
    ImageButton btnImg_search;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnImg_search = (ImageButton) findViewById(R.id.btnSearch);
        btn_services = (Button) findViewById(R.id.button2);
        btn_centralWeb = (Button) findViewById(R.id.button3);
        btn_settings = (Button) findViewById(R.id.button4);

        btnImg_search.setOnClickListener(this);
        btn_services.setOnClickListener(this);
        btn_centralWeb.setOnClickListener(this);
        btn_settings.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btnSearch:
                if(true) {
                    intent = new Intent(this, GoogleMapActivity.class);
                    startActivity(intent);
                }else{
                    displayMessager();
                }

                break;
            case R.id.button2:
                intent = new Intent(this, ServiceActivity.class);
                startActivity(intent);

                break;
            case R.id.button3:
                Uri uri = Uri.parse("http://central.wa.edu.au/Pages/default.aspx");
                intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

                break;
            case R.id.button4:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);

                break;

        }

    }

    private void displayMessager() {
    }

    /**
     * Method to search destination place input
     */
    public boolean searchRoom() {
        boolean result = false;
        EditText txtSearch = (EditText) findViewById(R.id.txtSearch);
        String destination = txtSearch.getText().toString();
        isAvailableRoom(destination);

        return result;

    }

    /**
     * Verify if the user input is a valid room or not
     * if YES - get building details
     * if NO - Display messager
     */
    private boolean isAvailableRoom(String room){
        RoomDataSource roomDataSource = new RoomDataSource(this);
        ArrayList<Room> roomList;
        Building building;

        roomList = roomDataSource.getAllRooms();

        for(int index = 0; index < roomList.size(); index++){
            //Checking if user's input match with database rooms
            if (roomList.get(index).getRoomName().equalsIgnoreCase(room)) {

                //Getting building information
                getBuildingInformation(roomList.get(index));
                                //WebServiceConnection webServiceConnection = new WebServiceConnection(this,3,roomList.get(index).getRoomID());
                //webServiceConnection.checkServiceConnAST.execute();

                HardCodeDB hardCodeDB = new HardCodeDB();
                building = hardCodeDB.ResolvePath(roomList.get(index).getRoomID(),false);
                return true;
            }
        }
        return false;

    }

    private void getBuildingInformation(Room room) {

    }


}

package codefactory.centralwayfinderproject.activites;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import codefactory.centralwayfinderproject.dao.BuildingDataSource;
import codefactory.centralwayfinderproject.dao.RoomDataSource;
import codefactory.centralwayfinderproject.helpers.WebServiceConnection;
import codefactory.centralwayfinderproject.models.Building;
import codefactory.centralwayfinderproject.models.GlobalObject;
import codefactory.centralwayfinderproject.models.Room;

public class MenuActivity extends Activity implements OnClickListener {

    Button btn_services, btn_centralWeb, btn_settings;
    ImageButton btnImg_search;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btnSearch:
                if(isAvailableRoom()) {
                    intent = new Intent(this, GoogleMapActivity.class);
                    startActivity(intent);
                }else{
                    displayMessage();
                }

                break;
            case R.id.serviceOption:
                intent = new Intent(this, ServiceActivity.class);
                startActivity(intent);

                break;
            case R.id.centralWebOption:
                Uri uri = Uri.parse("http://central.wa.edu.au/Pages/default.aspx");
                intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

                break;
            case R.id.settingsOption:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);

                break;

        }

    }

    private void displayMessage() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Sorry, the destination is not a room or is not in this campus.")
            .setTitle("DESTINATION IS NOT AVAILABLE")
            .setCancelable(false)
            .setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Verify if the user input is a valid room or not
     */
    private boolean isAvailableRoom(){
        RoomDataSource roomDAO = new RoomDataSource(this);
        ArrayList<Room> roomList;
        EditText txtSearch;
        String roomNumber;

        txtSearch = (EditText) findViewById(R.id.txtSearch);
        roomNumber = txtSearch.getText().toString();

        if(null != roomNumber && !roomNumber.isEmpty()) {
            //Loading room list from database
            roomList = roomDAO.getAllRooms();

            for (int index = 0; index < roomList.size(); index++) {
                //Checking if user's input match with database rooms
                if (roomList.get(index).getRoomName().equalsIgnoreCase(roomNumber)) {
                    //Getting building information
                    getBuildingInformation(roomList.get(index));

                    return true;
                }
            }
        }
        return false;

    }

    /**
     * Method which gets building information from WebService or Internal Database.
     *
     */
    private void getBuildingInformation(Room room) {
        Building building;
        BuildingDataSource buildingDAO = new BuildingDataSource(this);

        building = buildingDAO.getBuilding(room.getBuildingID());

        //Get building detailing from webService if object is null
        if(building.equals(null)){
            WebServiceConnection webServiceConnection = new WebServiceConnection(this,3,room);
            webServiceConnection.checkServiceConnAST.execute();

            building = buildingDAO.getBuilding(room.getBuildingID());
        }

        // Calling Application class (see application tag in AndroidManifest.xml)
        final GlobalObject globalObject = (GlobalObject) getApplicationContext();

        //Coping building details into globalObject
        globalObject.setBuildingID(building.getId());
        globalObject.setBuildingImage(building.getImage());
        globalObject.setBuildingName(building.getName());
        globalObject.setBuildingLatitude(building.getLatitude());
        globalObject.setBuildingLongitude(building.getLongitude());

    }


}

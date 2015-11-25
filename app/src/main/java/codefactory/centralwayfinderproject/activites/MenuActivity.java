package codefactory.centralwayfinderproject.activites;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import java.util.ArrayList;

import codefactory.centralwayfinderproject.R;
import codefactory.centralwayfinderproject.dao.RoomDataSource;
import codefactory.centralwayfinderproject.helpers.WebServiceConnection;
import codefactory.centralwayfinderproject.models.GlobalObject;
import codefactory.centralwayfinderproject.models.Room;

public class MenuActivity extends Activity implements OnClickListener {

    private EditText txtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.activity_menu);

        txtSearch = (EditText) findViewById(R.id.txtSearch);

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btnSearch:
                if(!isAvailableRoom()) {
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
        String roomNumber;

        roomNumber = txtSearch.getText().toString();
        txtSearch.setText("");

        if(null != roomNumber && !roomNumber.isEmpty()) {
            //Loading room list from database
            roomList = roomDAO.getAllRooms();

            for (int index = 0; index < roomList.size(); index++) {
                //Checking if user's input match with database rooms
                if (roomList.get(index).getRoomName().equalsIgnoreCase(roomNumber)) {

                    //Getting building information
                    WebServiceConnection webServiceConnection = new WebServiceConnection(this,3,roomList.get(index));
                    webServiceConnection.execute();

                    // Calling Application class (see application tag in AndroidManifest.xml)
                    GlobalObject globalObject = (GlobalObject) getApplicationContext();

                    //Copying building details into globalObject
                    globalObject.setBuildingID(roomList.get(index).getBuildingID());
                    globalObject.setRoomName(roomList.get(index).getRoomName());
                    globalObject.setRoomID(roomList.get(index).getRoomID());
                    return true;
                }
            }
        }
        return false;

    }

}

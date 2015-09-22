package codefactory.centralwayfinderproject.activites;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
<<<<<<< HEAD
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
=======
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
>>>>>>> master

import codefactory.centralwayfinderproject.R;
import codefactory.centralwayfinderproject.models.Campus;

public class MenuActivity extends Activity implements OnClickListener {

    Button btn_button1, btn_button2, btn_button3, btn_button4;

    ArrayList<Campus> campusList;
    TextView tempTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
<<<<<<< HEAD
        //retrieve campus list from intent
        campusList = (ArrayList<Campus>)getIntent().getSerializableExtra("campuses");
        //debug
        tempTextView = (TextView)findViewById(R.id.tempTextView);
        if (campusList.size()>0) tempTextView.setText("Found "+String.valueOf(campusList.size())+ " campuses!");
    }
=======
>>>>>>> master

        btn_button1 = (Button) findViewById(R.id.button1);
        btn_button2 = (Button) findViewById(R.id.button2);
        btn_button3 = (Button) findViewById(R.id.button3);
        btn_button4 = (Button) findViewById(R.id.button4);

        btn_button1.setOnClickListener(this);
        btn_button2.setOnClickListener(this);
        btn_button3.setOnClickListener(this);
        btn_button4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                //Go to Search Activity
                Intent intent = new Intent(this, SelectCampusActivity.class);
                startActivity(intent);

                break;
            case R.id.button2:
                Toast.makeText(getApplicationContext(), "Missing Implementation...",
                        Toast.LENGTH_LONG).show();

                break;
            case R.id.button3:
                Uri uri = Uri.parse("http://central.wa.edu.au/Pages/default.aspx"); // missing 'http://' will cause crash
                intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

                break;
            case R.id.button4:

                break;

        }

    }
}

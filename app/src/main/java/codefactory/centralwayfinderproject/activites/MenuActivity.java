package codefactory.centralwayfinderproject.activites;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import codefactory.centralwayfinderproject.R;
import codefactory.centralwayfinderproject.models.Campus;

public class MenuActivity extends AppCompatActivity {

    ArrayList<Campus> campusList;
    TextView tempTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        //retrieve campus list from intent
        campusList = (ArrayList<Campus>)getIntent().getSerializableExtra("campuses");
        //debug
        tempTextView = (TextView)findViewById(R.id.tempTextView);
        if (campusList.size()>0) tempTextView.setText("Found "+String.valueOf(campusList.size())+ " campuses!");
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

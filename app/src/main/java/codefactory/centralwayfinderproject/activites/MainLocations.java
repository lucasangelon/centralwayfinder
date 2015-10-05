package codefactory.centralwayfinderproject.activites;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.graphics.drawable.Drawable;
import android.widget.GridView;

import java.util.Objects;

import codefactory.centralwayfinderproject.R;
import codefactory.centralwayfinderproject.adapters.MainLocationsAdapter;

public class MainLocations extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_locations);

        // REQ ATTN
        // REQUIRE THE INPUT FOR THE ADAPTER
        Object[] adapterInput = new Object[9];
        GridView adapterView = (GridView) findViewById(R.id.main_locations_list);
        adapterView.setAdapter(new MainLocationsAdapter(this, adapterInput));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_locations, menu);
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

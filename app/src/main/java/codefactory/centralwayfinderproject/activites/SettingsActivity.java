package codefactory.centralwayfinderproject.activites;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import codefactory.centralwayfinderproject.R;
import codefactory.centralwayfinderproject.helpers.Useful;

public class SettingsActivity extends ActionBarActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        final Useful util = new Useful(this);
        Switch switchAccessibility = (Switch) findViewById(R.id.Switch_Accessibility);
        switchAccessibility.setChecked(util.getAccessibilityOption());

        //Save the button event on the preference file
        switchAccessibility.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
               util.setAccessibilityOption(isChecked);
            }
        });
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

    public void handleOnClick(View view)
    {
        switch(view.getId()) {
            case R.id.blockAbout:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);

                break;

            case R.id.blockSelectCampus:
                intent = new Intent(this, SelectCampusActivity.class);
                startActivity(intent);

                break;

            case R.id.blockPrivacyPolicy:
                intent = new Intent(this, CopyrightActivity.class);
                startActivity(intent);

                break;

            case R.id.blockTermsService:
                intent = new Intent(this, TermsActivity.class);
                startActivity(intent);

                break;
        }
    }
}

package codefactory.centralwayfinderproject.activites;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import codefactory.centralwayfinderproject.R;

public class MenuActivity extends Activity implements OnClickListener {

    Button btn_button1, btn_button2, btn_button3, btn_button4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

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
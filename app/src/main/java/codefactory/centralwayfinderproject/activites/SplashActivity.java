package codefactory.centralwayfinderproject.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import codefactory.centralwayfinderproject.R;

public class SplashActivity extends Activity implements OnClickListener {

    private Button firstTimeClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Initialise Button and add listener
        firstTimeClick = (Button) findViewById(R.id.btnFirstClick);
        firstTimeClick.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.btnFirstClick){
                //Go to Menu
                Intent intent = new Intent(this, MenuActivity.class);
                startActivity(intent);
            }

        }

}

package codefactory.centralwayfinderproject.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;

import codefactory.centralwayfinderproject.R;

/**
 * Created by Dillon on 5/10/2015.
 */
public class MainLocationsAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;

    Drawable studentServicesIcon;
    Drawable libraryIcon;
    Drawable cafeIcon;
    Drawable gymIcon;

    //REQUIRES ATTENTION
    Object[] locations;

    public MainLocationsAdapter(Context Context, Object[] InputLocations) {
        context = Context;
        locations = InputLocations;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Set each icon to their relevant icon
        studentServicesIcon = context.getResources().getDrawable(R.drawable.placeholder);
        libraryIcon = context.getResources().getDrawable(R.drawable.placeholder);
        cafeIcon = context.getResources().getDrawable(R.drawable.placeholder);
        gymIcon = context.getResources().getDrawable(R.drawable.placeholder);
    }

    @Override
    public int getCount() {
        return locations.length;
    }

    @Override
    public Object getItem(int position) {
        return locations[position];
    }

    @Override
    public long getItemId(int position) {
        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_main_locations, null);
        }

        ImageButton imgbtn = (ImageButton) convertView.findViewById(R.id.main_location_imgbutton);
        Button txtbtn = (Button) convertView.findViewById(R.id.main_location_txtbutton);
        View.OnClickListener action = new View.OnClickListener() {
            public void onClick(View v) {
                // REQ ATTN
                // Put the outcome to the actions here
            }};

        // REQ ATTN
        // UPDATE THE BUTTONS HERE
        // NOTE: item_main_locations.main_locations.imgbutton should be the "unknown" icon as a standard (currently a placeholder though)
        //          Hence when changing the icon, only change/set it if it SHOULD be changed, dont bother trying to set it to itself

        imgbtn.setOnClickListener(action);
        txtbtn.setOnClickListener(action);

        return convertView;
    }
}

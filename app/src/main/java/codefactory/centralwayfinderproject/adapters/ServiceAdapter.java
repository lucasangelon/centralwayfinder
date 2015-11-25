package codefactory.centralwayfinderproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import codefactory.centralwayfinderproject.R;
import codefactory.centralwayfinderproject.helpers.WebServiceConnection;
import codefactory.centralwayfinderproject.models.GlobalObject;
import codefactory.centralwayfinderproject.models.Room;

/**
 * Created by Gustavo on 9/11/2015.
 */
public class ServiceAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    ArrayList<Room> serviceList = new ArrayList();

    public ServiceAdapter(Context mContext, ArrayList<Room> list) {
        context = mContext;
        serviceList = list;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * How many items are in the data set represented by this Adapter.
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return serviceList.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     * @param position Position of the item whose data we want within the adapter's data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return position;
    }

    /**
     * Get the row id associated with the specified position in the list.
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Get a View that displays the data at the specified position in the data set
     * @param position
     * @param convertView The old view to reuse, if possible.
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.item_service,null);
        }

        final ViewHolder viewHolder = new ViewHolder();
        viewHolder.icon = (ImageView)view.findViewById(R.id.service_icon);
        viewHolder.text = (TextView)view.findViewById(R.id.service_title);

        viewHolder.text.setText(serviceList.get(position).getRoomName());

        //CHANGE TO CHECK IMAGE NAME INSTEAD OF NAME
        switch (serviceList.get(position).getRoomImage()){
            case "Ca":
                viewHolder.icon.setImageResource(R.mipmap.ic_cafe);
                break;
            case "Gy":
                viewHolder.icon.setImageResource(R.mipmap.ic_gym);
                break;
            case "Is":
                viewHolder.icon.setImageResource(R.mipmap.ic_international_student);
                break;
            case "Bo":
                viewHolder.icon.setImageResource(R.mipmap.ic_bookshop);
                break;
            case "Li":
                viewHolder.icon.setImageResource(R.mipmap.ic_library);
                break;
            case "Ko":
                viewHolder.icon.setImageResource(R.mipmap.ic_koolark);
                break;
            case "Ss":
                viewHolder.icon.setImageResource(R.mipmap.ic_central_web);
                break;
            default:
                viewHolder.icon.setImageResource(R.mipmap.ic_default);
                break;
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Getting building information
                WebServiceConnection webServiceConnection = new WebServiceConnection(context,3,serviceList.get(position));
                webServiceConnection.execute();

                // Calling Application class (see application tag in AndroidManifest.xml)
                GlobalObject globalObject = (GlobalObject) context.getApplicationContext();

                //Copying building details into globalObject
                globalObject.setBuildingID(serviceList.get(position).getBuildingID());
                globalObject.setRoomName(serviceList.get(position).getRoomName());
                globalObject.setRoomID(serviceList.get(position).getRoomID());
            }
        });

        return view;
    }

    static class ViewHolder {
        protected ImageView icon;
        protected TextView text;
    }
}

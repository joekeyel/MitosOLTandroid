package my.com.tm.moapps.mitosolt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by joe on 7/24/2016.
 */
public class phoneadaptor extends ArrayAdapter {

    private LayoutInflater layoutInflator;
    private Integer resource;
    private List<phonelistmodel> phone;


    public phoneadaptor(Context context, int resource, List<phonelistmodel> objects) {
        super(context, resource, objects);

        this.resource = resource;
        phone = objects;
        layoutInflator = (LayoutInflater)getContext().getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public void add(Object object) {
        super.add(object);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



        if (convertView == null) {

            convertView = layoutInflator.inflate(resource, null);
        }

        TextView phonebuilding = (TextView)convertView.findViewById(R.id.phonebuilding);
        TextView regid = (TextView)convertView.findViewById(R.id.regidphone);
        TextView delbutton = (TextView)convertView.findViewById(R.id.deletebuilding);

        phonebuilding.setText(phone.get(position).getBuilding());
        regid.setText(phone.get(position).getRegid());
        delbutton.setTag(position);

        return convertView;

    }



}

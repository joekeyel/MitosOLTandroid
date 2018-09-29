package my.com.tm.moapps.mitosolt;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.arturogutierrez.Badges;
import com.github.arturogutierrez.BadgesNotSupportedException;

import java.util.List;

/**
 * Created by joe on 3/5/2016.
 */
public class summaryadaptor extends ArrayAdapter {

    private List<summarymodel> summary;
    private LayoutInflater layoutinflator;
    private Integer resource;
    private Integer badge = 0;


    public summaryadaptor(Context context, int resource, List<summarymodel> Object) {
        super(context, resource,Object );

        this.resource = resource;
        summary = Object;
        layoutinflator = (LayoutInflater)getContext().getSystemService(context.LAYOUT_INFLATER_SERVICE);


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){

            convertView = layoutinflator.inflate(resource,null);
        }

        TextView tvbuilding = (TextView)convertView.findViewById(R.id.tvbuilding3);
        TextView tvbasket = (TextView)convertView.findViewById(R.id.tvbasket3);
        TextView tvtotal = (TextView)convertView.findViewById(R.id.tvtotal3);

        LinearLayout empty = (LinearLayout)convertView.findViewById(R.id.emptyspace);
        tvbuilding.setText(summary.get(position).getBuildingid());
        tvbasket.setText(summary.get(position).getBasket());
        tvtotal.setText(summary.get(position).getTotal());

        if(summary.get(position).getBasket().contains("fiberhome")){

//            remarktv.setBackgroundColor(0xFF00FF00);
            convertView.setBackgroundColor(Color.RED);
            tvbuilding.setBackgroundColor(Color.RED);
            tvbasket.setBackgroundColor(Color.RED);
            tvtotal.setBackgroundColor(Color.RED);

            empty.setBackgroundColor(Color.RED);






        }

        else{
//            remarktv.setBackgroundColor(Color.WHITE);
            convertView.setBackgroundColor(Color.GRAY);
            tvbuilding.setBackgroundColor(Color.GRAY);
            tvbasket.setBackgroundColor(Color.GRAY);
            tvtotal.setBackgroundColor(Color.GRAY);
            empty.setBackgroundColor(Color.GRAY);


        }




        return convertView;
    }
}

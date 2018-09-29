package my.com.tm.moapps.mitosolt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by joe on 8/29/2016.
 */
public class agingadaptor extends ArrayAdapter {

    private List<agingmodel> summary;
    private LayoutInflater layoutinflator;
    private Integer resource;


    public agingadaptor(Context context, int resource, List<agingmodel> Object) {
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

        TextView tvexchange = (TextView)convertView.findViewById(R.id.tvexchange);
        TextView tvmigratesub = (TextView)convertView.findViewById(R.id.tvmsubvalue);
        TextView tvtotaltt = (TextView)convertView.findViewById(R.id.tvtotalsubvalue);
        TextView tvaging1days = (TextView)convertView.findViewById(R.id.tvagingonedayvalue);
        TextView tvaging2days = (TextView)convertView.findViewById(R.id.tvaging2daysvalue);
        TextView tvaging3days = (TextView)convertView.findViewById(R.id.tvaging3daysvalue);
        TextView tvtotalaging = (TextView)convertView.findViewById(R.id.tvtotalagingvalue);

        tvexchange.setText(summary.get(position).getExchange());
        tvmigratesub.setText(summary.get(position).getMigratesub());
        tvtotaltt.setText(summary.get(position).getTotaltt());
        tvaging1days.setText(summary.get(position).getOnedays());
        tvaging2days.setText(summary.get(position).getTwodays());
        tvaging3days.setText(summary.get(position).getThreedays());
        tvtotalaging.setText(summary.get(position).getTotalaging());





        return convertView;
    }
}

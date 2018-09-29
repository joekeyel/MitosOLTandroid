package my.com.tm.moapps.mitosolt;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joe on 8/29/2016.
 */
public class adslportadaptor extends ArrayAdapter {

    private List<adslportsatemodel> summary;
    private LayoutInflater layoutinflator;
    private Integer resource,selectedIndex;
    private List<Integer> select;
    private List<adslportsatemodel> orig;


    public adslportadaptor(Context context, int resource, List<adslportsatemodel> Object) {
        super(context, resource,Object );

        this.resource = resource;
        summary = Object;
        layoutinflator = (LayoutInflater)getContext().getSystemService(context.LAYOUT_INFLATER_SERVICE);

        selectedIndex = -1;
        select = new ArrayList<>();

        select.add(-1);
    }

    public void setSelection1(int position,String data) {

        //change the remark text view

        summary.get(position).setPortstatestatusand(data);

        // this is to remove item from the position
        //       verifymodelList.remove(position);

        selectedIndex = position;
        select.add(position);

// this will update the view
        notifyDataSetChanged();


    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<adslportsatemodel> results = new ArrayList<adslportsatemodel>();
                if (orig == null)
                    orig = summary;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final adslportsatemodel g : orig) {
                            if (g.getServicenumber().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);
                            if (g.getPortstatestatus().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);
                            if (g.getPortstatestatusand().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);


                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          Filter.FilterResults results) {
                summary = (ArrayList<adslportsatemodel>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return summary.size();
    }

    @Override
    public Object getItem(int position) {
        return summary.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){

            convertView = layoutinflator.inflate(resource,null);
        }

         TextView tvservicenumber = (TextView)convertView.findViewById(R.id.tvservicenumberadsl);
         TextView tvtargetlen = (TextView)convertView.findViewById(R.id.tvtargetlenvalue);
         TextView tvtargetdside = (TextView)convertView.findViewById(R.id.tvtargetdsidevalue);
         TextView tvtactualdisde = (TextView)convertView.findViewById(R.id.tvactualdsidevalue);
         TextView tvtargetpotsout = (TextView)convertView.findViewById(R.id.tvtargetpotsoutvalue);
         TextView tvtargetdslout = (TextView)convertView.findViewById(R.id.targetdsloutvalue);
         TextView tvportstatevalue = (TextView)convertView.findViewById(R.id.tvportstatusvalue);
        TextView tvadslstatusand = (TextView)convertView.findViewById(R.id.tvadslstatusand);
        TextView tvadslloginid = (TextView)convertView.findViewById(R.id.tvportloginidvalue);

        tvservicenumber.setText(summary.get(position).getServicenumber());
        tvtargetlen.setText(summary.get(position).getTargetlen());
        tvtargetdside.setText(summary.get(position).getTargetdside());
        tvtactualdisde.setText(summary.get(position).getActualdside());
        tvtargetpotsout.setText(summary.get(position).getTargetpotsout());
        tvtargetdslout.setText(summary.get(position).getTargetdslout());
        tvportstatevalue.setText(summary.get(position).getPortstatestatus());
        tvadslstatusand.setText(summary.get(position).getPortstatestatusand());
        tvadslloginid.setText(summary.get(position).getLoginid());


        if(select.contains(position)){

//            remarktv.setBackgroundColor(0xFF00FF00);
            convertView.setBackgroundColor(Color.DKGRAY);



        }

        else{
//            remarktv.setBackgroundColor(Color.WHITE);
            convertView.setBackgroundColor(Color.GRAY);

        }


        return convertView;
    }
}

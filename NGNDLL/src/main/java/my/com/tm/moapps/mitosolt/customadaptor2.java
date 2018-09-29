package my.com.tm.moapps.mitosolt;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.List;

/**
 * Created by joe on 1/25/2016.
 */
public class customadaptor2 extends ArrayAdapter implements Filterable {

    private List<ttmodel> ttmodellist;
    private List<ttmodel> orig;
    private int resource;
    private LayoutInflater inflator;

    public customadaptor2(Context context, int resource, List<ttmodel> objects) {
        super(context, resource, objects);

        ttmodellist = objects;
        this.resource = resource;
        inflator = (LayoutInflater)getContext().getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<ttmodel> results = new ArrayList<ttmodel>();
                if (orig == null)
                    orig = ttmodellist;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final ttmodel g : orig) {
                            if (g.getCabinetid().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);
                            if (g.getServiceNo().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);
                            if (g.getReferencenumber().toLowerCase()
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
                ttmodellist = (ArrayList<ttmodel>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return ttmodellist.size();
    }

    @Override
    public Object getItem(int position) {
        return ttmodellist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){

            convertView = inflator.inflate(resource,null);
        }


        TextView ttno;
        TextView servicenumber;
        TextView priority,repeat,building,referencenumber,createddate,symtomcode,reasoncode,exchange,cabinetid,customername,customermobile,remark,aging;

        ttno = (TextView) convertView.findViewById(R.id.tvTTno);
        servicenumber = (TextView) convertView.findViewById(R.id.tvservicenumber);
        building = (TextView) convertView.findViewById(R.id.tvbuildingid);
        referencenumber = (TextView) convertView.findViewById(R.id.tvreferencenumber);
        createddate = (TextView) convertView.findViewById(R.id.tvcreateddate);
        symtomcode = (TextView) convertView.findViewById(R.id.tvsymtomcode);
        reasoncode = (TextView) convertView.findViewById(R.id.tvreasoncode);
        exchange = (TextView) convertView.findViewById(R.id.tvexchange);
        cabinetid = (TextView) convertView.findViewById(R.id.tvcabinetid);
        customername = (TextView) convertView.findViewById(R.id.tvcustomername);
        customermobile = (TextView) convertView.findViewById(R.id.tvcustomermobile);
        remark = (TextView) convertView.findViewById(R.id.tvremark);
        repeat = (TextView) convertView.findViewById(R.id.tvrepeat);
        priority = (TextView) convertView.findViewById(R.id.tvpriorityvalue);
        aging = (TextView)convertView.findViewById(R.id.tvagingvalue);

        priority.setText(ttmodellist.get(position).getPriority());
        repeat.setText(ttmodellist.get(position).getRepeat());
        ttno.setText(ttmodellist.get(position).getTTno());
        servicenumber.setText(ttmodellist.get(position).getServiceNo());
        building.setText(ttmodellist.get(position).getBuildingid());
        referencenumber.setText(ttmodellist.get(position).getReferencenumber());
        createddate.setText(ttmodellist.get(position).getCreated_date());
        symtomcode.setText(ttmodellist.get(position).getSymtomcode());
        reasoncode.setText(ttmodellist.get(position).getReasoncode());
        exchange.setText(ttmodellist.get(position).getExchange());
        cabinetid.setText(ttmodellist.get(position).getCabinetid());
        customername.setText(ttmodellist.get(position).getCustomername());
        customermobile.setText(ttmodellist.get(position).getCustomermobile());
        remark.setText(ttmodellist.get(position).getRemark());
        aging.setText(ttmodellist.get(position).getAgingstr());

        if(ttmodellist.get(position).getAgingint()>24){

            convertView.setBackgroundColor(Color.RED);
        }else{

            convertView.setBackgroundColor(Color.DKGRAY);
        }

        return convertView;
    }
}

package my.com.tm.moapps.mitosolt;

import android.content.Context;
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
public class adslportsummaryadaptor extends ArrayAdapter {

    private List<adslportsatesummarymodel> summary;
    private LayoutInflater layoutinflator;
    private Integer resource;
    private List<adslportsatesummarymodel> orig;


    public adslportsummaryadaptor(Context context, int resource, List<adslportsatesummarymodel> Object) {
        super(context, resource,Object );

        this.resource = resource;
        summary = Object;
        layoutinflator = (LayoutInflater)getContext().getSystemService(context.LAYOUT_INFLATER_SERVICE);


    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<adslportsatesummarymodel> results = new ArrayList<adslportsatesummarymodel>();
                if (orig == null)
                    orig = summary;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final adslportsatesummarymodel g : orig) {
                            if (g.getTargetcabinet().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);
                            if (g.getAdslportstate().toLowerCase()
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
                summary = (ArrayList<adslportsatesummarymodel>) results.values;
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

         TextView tvcabinetid = (TextView)convertView.findViewById(R.id.tvcabinetidadsl);
         TextView tvportstate = (TextView)convertView.findViewById(R.id.tvadslportvalue);
         TextView tvtotaladsl = (TextView)convertView.findViewById(R.id.totaladslport);


        tvcabinetid.setText(summary.get(position).getTargetcabinet());
        tvportstate.setText(summary.get(position).getAdslportstate());
        tvtotaladsl.setText(summary.get(position).getTotal());


        return convertView;
    }
}

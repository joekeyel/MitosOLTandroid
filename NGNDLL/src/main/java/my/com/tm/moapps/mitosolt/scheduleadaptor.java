package my.com.tm.moapps.mitosolt;

import android.content.Context;
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
 * Created by joe on 8/29/2016.
 */
public class scheduleadaptor extends ArrayAdapter implements Filterable {

    private List<schedulemodel> summary;
    private List<schedulemodel> orig;
    private LayoutInflater layoutinflator;
    private Integer resource;


    public scheduleadaptor(Context context, int resource, List<schedulemodel> Object) {
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
                final ArrayList<schedulemodel> results = new ArrayList<schedulemodel>();
                if (orig == null)
                    orig = summary;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final schedulemodel g : orig) {
                            if (g.getTargetcabinet().toLowerCase()
                                    .contains(constraint.toString()))
                            {results.add(g);}

                            if (g.getState().toLowerCase()
                                    .contains(constraint.toString()))
                            {results.add(g);}


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
                summary = (ArrayList<schedulemodel>) results.values;
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

        TextView tvcabinetid = (TextView)convertView.findViewById(R.id.tvtargetcabinet);
        TextView tvmigrationdate = (TextView)convertView.findViewById(R.id.tvmigrationdate);
        TextView tvstopdate = (TextView)convertView.findViewById(R.id.tvstopdate);
        TextView tvstate = (TextView)convertView.findViewById(R.id.tvstate);
        TextView tvpmwno = (TextView)convertView.findViewById(R.id.tvpmwno);
        TextView tvckc1 = (TextView)convertView.findViewById(R.id.tvckc1);
        TextView tvckc2 = (TextView)convertView.findViewById(R.id.tvckc2);
        TextView tvoldcabinet = (TextView)convertView.findViewById(R.id.tvoldcabinet);
        TextView tvstatus = (TextView)convertView.findViewById(R.id.tvstatuscabinet);
        TextView projecttv = (TextView)convertView.findViewById(R.id.projecttypetv);
        TextView migrationstatustv = (TextView)convertView.findViewById(R.id.monitoringstatus);

        tvcabinetid.setText(summary.get(position).getTargetcabinet());
        tvmigrationdate.setText(summary.get(position).getMigrationdate());
        tvstopdate.setText(summary.get(position).getStopdate());
        tvstate.setText(summary.get(position).getState());
        tvpmwno.setText(summary.get(position).getPmwno());
        tvckc1.setText(summary.get(position).getCkc1());
        tvckc2.setText(summary.get(position).getCkc2());
        tvoldcabinet.setText(summary.get(position).getOldcabinet());
        tvstatus.setText(summary.get(position).getStatuscabinet());
        projecttv.setText(summary.get(position).getProjecttype());
        migrationstatustv.setText(summary.get(position).getMigrationstatus());




        return convertView;
    }
}

package my.com.tm.moapps.mitosolt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 public class summaryadaptor extends ArrayAdapter {

 private List<summarymodel> summary;
 private LayoutInflater layoutinflator;
 private Integer resource;


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

 tvbuilding.setText(summary.get(position).getBuildingid());
 tvbasket.setText(summary.get(position).getBasket());
 tvtotal.setText(summary.get(position).getTotal());





 return convertView;
 }
 }
 */
public class auditadaptor extends ArrayAdapter {

    private List<auditmodel> audit;
    private LayoutInflater layoutinflator;
    private Integer resource;

    public auditadaptor(Context context, int resource, List<auditmodel> Object) {
        super(context, resource,Object );

        this.resource = resource;
        audit = Object;
        layoutinflator = (LayoutInflater)getContext().getSystemService(context.LAYOUT_INFLATER_SERVICE);


    }



    @Override
    public void add(Object object) {
        super.add(object);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {

            convertView = layoutinflator.inflate(resource, null);
        }


        TextView remarkaudit = (TextView)convertView.findViewById(R.id.remarkaudit);
        TextView updatebyaudit = (TextView)convertView.findViewById(R.id.auditupdate);

        remarkaudit.setText(audit.get(position).getRemark());
        updatebyaudit.setText(audit.get(position).getUpdateby());






        return convertView;

    }
}

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
 * Created by joe on 6/5/2016.
 */
public class verificationadaptor extends ArrayAdapter {

    private List<verifymodel> verifymodelList;
    private List<verifymodel> orig;
    private List<Integer> select;
    private List<updatemodel> updatemodels;

    private int resource;
    private LayoutInflater inflator;

    private String remark;

    private int selectedIndex;

    public verificationadaptor(Context context, int resource, List<verifymodel> objects) {
        super(context, resource, objects);


        verifymodelList = objects;
        this.resource = resource;
        inflator = (LayoutInflater)getContext().getSystemService(context.LAYOUT_INFLATER_SERVICE);
        selectedIndex = -1;
        select = new ArrayList<>();
        updatemodels = new ArrayList<>();

        select.add(-1);
        remark = "";
    }

    public void setSelection1(int position,String data) {

     //change the remark text view

        verifymodelList.get(position).setRemark(data);

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
                final ArrayList<verifymodel> results = new ArrayList<verifymodel>();
                if (orig == null)
                    orig = verifymodelList;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final verifymodel g : orig) {
                            if (g.getCabinetdsidepair().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);
                            if (g.getService_num().toLowerCase()
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
                verifymodelList = (ArrayList<verifymodel>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return verifymodelList.size();
    }

    @Override
    public Object getItem(int position) {
        return verifymodelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if(convertView==null){



            convertView = inflator.inflate(resource, null);


        }

        String row = String.valueOf(position);

        TextView idverificationtv,cabinetdsidepairtv,service_numbertv,service_typetv,remarktv;

        idverificationtv = (TextView)convertView.findViewById(R.id.tvidverification);
        cabinetdsidepairtv = (TextView)convertView.findViewById(R.id.tvdsidepair);
        service_numbertv = (TextView)convertView.findViewById(R.id.tvservice_number);
        service_typetv = (TextView)convertView.findViewById(R.id.tvservice_type);
        remarktv = (TextView)convertView.findViewById(R.id.tvremarkverify);


        idverificationtv.setText(row);
        cabinetdsidepairtv.setText(verifymodelList.get(position).getCabinetdsidepair());
        service_numbertv.setText(verifymodelList.get(position).getService_num());
        service_typetv.setText(verifymodelList.get(position).getService_type());


        remarktv.setText(verifymodelList.get(position).getRemark());

//        if(selectedIndex!= -1 && position == selectedIndex)
//        {
//            remarktv.setBackgroundColor(0xFF00FF00);
//        }
//        else{
//
//            remarktv.setBackgroundColor(Color.WHITE);
//        }

        if(select.contains(position)){

//            remarktv.setBackgroundColor(0xFF00FF00);
            convertView.setBackgroundColor(0xFF00FF00);



        }

        else{
//            remarktv.setBackgroundColor(Color.WHITE);
            convertView.setBackgroundColor(Color.WHITE);

        }




//
//        for (Integer item : select){
//            if(item==position && item!= -1){
//
//                remarktv.setBackgroundColor(0xFF00FF00);
//            }
//            else{
//                remarktv.setBackgroundColor(Color.WHITE);
//            }
//
//        }






        return convertView;
    }

}

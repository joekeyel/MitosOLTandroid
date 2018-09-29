package my.com.tm.moapps.mitosolt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by joe on 2/9/2016.
 */
public class customadaptor extends ArrayAdapter<String> {

    public customadaptor(Context context, ArrayList result) {
        super(context, R.layout.rowcustom,result);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflator = LayoutInflater.from(getContext());

        View customview = inflator.inflate(R.layout.rowcustom, parent, false);

        String details = getItem(position);
        TextView ttview = (TextView)customview.findViewById(R.id.tvItem);

        ttview.setText(details);

        return  customview;
    }

    @Override
    public void add(String object) {
        super.add(object);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}

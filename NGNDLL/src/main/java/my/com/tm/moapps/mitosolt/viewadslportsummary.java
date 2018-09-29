package my.com.tm.moapps.mitosolt;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

public class viewadslportsummary extends AppCompatActivity {

    public static viewadslportsummary summaryactivityadslport;

    String uuid;
    String adslportstate,targetcabinet;
    Integer positionlv;

    public String building,basket,total;
    ArrayList<adslportsatesummarymodel> summarylist;
    AlertDialog.Builder alertDialogprogress;
    AlertDialog alertprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewadslport_summary);

        summaryactivityadslport = this;



        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        uuid = telephonyManager.getDeviceId();

        progressdialogshow();

        new adslportsummaryasynctask().execute(Config.APP_SERVER_URL23,uuid);



    }



    public void clicksearch(ArrayList<adslportsatesummarymodel> result){

        final adslportsummaryadaptor sanoAdapter = new adslportsummaryadaptor(getApplicationContext(), R.layout.adslsummarycustomrow, result);
        final ListView sanoview = (ListView) findViewById(R.id.lvadslportsummary);
        sanoview.setAdapter(sanoAdapter);

        SearchView sv = (SearchView) findViewById(R.id.searchViewcabinet);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                sanoAdapter.getFilter().filter(newText);
                return false;
            }
        });

    }

    public static viewadslportsummary getInstance() {
        return summaryactivityadslport;
    }

    public void updatelv(final ArrayList<adslportsatesummarymodel> adslportstatesummary){

        this.runOnUiThread(new Runnable() {
            public void run() {

                summarylist = adslportstatesummary;

                final adslportsummaryadaptor sanoladaptor2 = new adslportsummaryadaptor(getApplicationContext(), R.layout.adslsummarycustomrow, summarylist);
                final ListView lvsummary = (ListView) findViewById(R.id.lvadslportsummary);
                lvsummary.setAdapter(sanoladaptor2);

                lvsummary.setOnItemClickListener(

                        new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                adslportsatesummarymodel obj = (adslportsatesummarymodel) lvsummary.getAdapter().getItem(position);
                                positionlv = position;
                                targetcabinet = obj.getTargetcabinet();
                                String adslportstate = obj.getAdslportstate();
                                String totalstate = obj.getTotal();


                                Intent i = new Intent(getApplicationContext(),
                                        viewadslport.class);


                                i.putExtra("targetcabinet", targetcabinet);



                                startActivity(i);


                            }

                        });

            }


        });
    }



    public void showtoast(String msg){

        Toast.makeText(getApplicationContext(), msg ,
                Toast.LENGTH_LONG).show();
    }

    public void progressdialogshow(){


        alertDialogprogress = new AlertDialog.Builder(viewadslportsummary.this);

        alertprogress = alertDialogprogress.create();

        alertprogress.setTitle("Loading");

        LayoutInflater inflater = getLayoutInflater();

        // inflate the custom popup layout
        final View convertView = inflater.inflate(R.layout.progressdialog, null);
        // find the ListView in the popup layout
        final ProgressBar progressBar = (ProgressBar)convertView.findViewById(R.id.progressBar4);

        // setSimpleList(listView, comment);

        alertprogress.setView(convertView);

        alertprogress.show();

    }

    public void progressbarhide(){

        alertprogress.dismiss();

    }


    public void updateviewlist(int index,String status){

        //find the listview
        ListView lv = (ListView)findViewById(R.id.lvadslport);
        // find the custome adaptor
        adslportadaptor sanadaptor = (adslportadaptor) lv.getAdapter();
        // sent variable to the custome adaptor to change the view
        sanadaptor.setSelection1(index,status);

//
//        View V = lv.getChildAt(index -
//                lv.getFirstVisiblePosition());
//
//        if(V == null)
//            return;
//
//        TextView remark1 = (TextView) V.findViewById(R.id.tvremarkverify);
//        remark1.setText("Saved");



    }
}

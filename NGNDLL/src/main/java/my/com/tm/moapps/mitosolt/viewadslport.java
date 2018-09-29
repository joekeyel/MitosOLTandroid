package my.com.tm.moapps.mitosolt;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class viewadslport extends AppCompatActivity {

    public static viewadslport summaryactivityadslport;

    String uuid;
    String cabinetidstring,servicenumbermain;
    Integer positionlv;

    public String building,basket,total;
    ArrayList<adslportsatemodel> summarylist;
    android.app.AlertDialog.Builder alertDialogprogress;
    android.app.AlertDialog alertprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewadslport);

        summaryactivityadslport = this;


        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        uuid = telephonyManager.getDeviceId();

        progressdialogshow();

        cabinetidstring = getIntent().getStringExtra("targetcabinet");
        new adslportstateasynctask().execute(Config.APP_SERVER_URL22,uuid,cabinetidstring);



    }

    public void clicksearch2(ArrayList<adslportsatemodel> result){

        final adslportadaptor sanoAdapter = new adslportadaptor(getApplicationContext(), R.layout.adslcustomrow, result);
        final ListView sanoview = (ListView) findViewById(R.id.lvadslport);
        sanoview.setAdapter(sanoAdapter);

        SearchView sv = (SearchView) findViewById(R.id.svfiltermr);
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




    public static viewadslport getInstance() {
        return summaryactivityadslport;
    }

    public void updatelv(final ArrayList<adslportsatemodel> adslportstatesummary){

        this.runOnUiThread(new Runnable() {
            public void run() {

                summarylist = adslportstatesummary;

                final adslportadaptor sanoladaptor2 = new adslportadaptor(getApplicationContext(), R.layout.adslcustomrow, summarylist);
                final ListView lvsummary = (ListView) findViewById(R.id.lvadslport);
                lvsummary.setAdapter(sanoladaptor2);

                lvsummary.setOnItemClickListener(

                        new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                adslportsatemodel obj = (adslportsatemodel) lvsummary.getAdapter().getItem(position);
                                positionlv = position;
                                servicenumbermain = obj.getServicenumber();
                                String targetpotsout = obj.getTargetpotsout();
                                String targetdslout = obj.getTargetdslout();
                                String portstate = obj.getPortstatestatus();

                                onShowPopup2(obj);

                                Log.d("MyLog", "Value is: " + servicenumbermain);

//                                Intent i = new Intent(getApplicationContext(),
//                                        Main_Activity.class);
//
//
//                                i.putExtra("servicenumber", servicenumber);
//                                i.putExtra("targetpotsout", targetpotsout);
//                                i.putExtra("targetdslout", targetdslout);
//                                i.putExtra("portstate", portstate);
//
//
//                                startActivity(i);


                            }

                        });

            }


        });
    }

    public void onShowPopup2(final adslportsatemodel adslport){


        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(viewadslport.this);

        final AlertDialog alert = alertDialog.create();



        alert.setTitle("Update ADSL Port Status");

        LayoutInflater inflater = getLayoutInflater();

        // inflate the custom popup layout
        final View convertView = inflater.inflate(R.layout.adslportupdatepopup, null);
        // find the ListView in the popup layout

        // setSimpleList(listView, comment);

        final TextView servicenumber = (TextView)convertView.findViewById(R.id.tvservicenumberadslport);
        TextView targetportsout = (TextView)convertView.findViewById(R.id.tvtargetpotsoutupdate);
        TextView targetdslout = (TextView)convertView.findViewById(R.id.targetdsloutupdate);
        TextView adslportstate = (TextView)convertView.findViewById(R.id.tvadslportstateupdate);

        servicenumber.setText(adslport.getServicenumber());
        targetportsout.setText(adslport.getTargetpotsout());
        targetdslout.setText(adslport.getTargetdslout());
        adslportstate.setText(adslport.getPortstatestatus());

        Spinner spinner = (Spinner) convertView.findViewById(R.id.spinneradsl);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.adslstatus, android.R.layout.simple_spinner_dropdown_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);



        alert.setView(convertView);

        alert.show();




        final Button btnaddcoment = (Button)convertView.findViewById(R.id.adslportstatusand);



        btnaddcoment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Spinner statusandport = (Spinner)convertView.findViewById(R.id.spinneradsl);
                String sattusand = statusandport.getSelectedItem().toString();

                // Perform action on click
                TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
                uuid = telephonyManager.getDeviceId();

                //update the database for and status on adsl port

                updatemrstatusand update = new updatemrstatusand(getApplicationContext());

                update.execute(servicenumbermain, String.valueOf(positionlv),sattusand);

//                showtoast(uuid);
//                showtoast(positionlv.toString());
                //find the listview
//                ListView lv = (ListView)findViewById(R.id.lvadslport);
//                // find the custome adaptor
//                adslportadaptor sanadaptor = (adslportadaptor) lv.getAdapter();
//                // sent variable to the custome adaptor to change the view
//                sanadaptor.setSelection1(positionlv,sattusand);




            }
        });






    }

    public void showtoast(String msg){

        Toast.makeText(getApplicationContext(), msg ,
                Toast.LENGTH_LONG).show();
    }

    public void progressdialogshow(){


        alertDialogprogress = new android.app.AlertDialog.Builder(viewadslport.this);

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

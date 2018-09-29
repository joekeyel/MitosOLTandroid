package my.com.tm.moapps.mitosolt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;

import java.util.ArrayList;

/**
 * Created by joe on 8/29/2016.
 */
public class schedule extends AppCompatActivity {

    public static schedule scheduleactivity;
    private Context ctc ;
    public int iClickedItem = 0;
    public String building,basket,total;
    ArrayList<schedulemodel> summarylist;
    android.app.AlertDialog.Builder alertDialogprogress;
    android.app.AlertDialog alertprogress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule);



        scheduleactivity = this;


//        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
//        String uuid = telephonyManager.getDeviceId();
//        //String uuid="phonekhairiahndsb";

//        BluetoothAdapter m_BluetoothAdapter	= null; // Local Bluetooth adapter
//        m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        String uuid = m_BluetoothAdapter.getAddress();

//        String uuid = UUID.randomUUID().toString();


        progressdialogshow();

        new scheduleasynctask().execute(Config.APP_SERVER_URL19);


    }

    public static schedule getInstance() {
        return scheduleactivity;
    }

    public void updatelv(final ArrayList<schedulemodel> agingsummary){

        this.runOnUiThread(new Runnable() {
            public void run() {

                summarylist = agingsummary;

                final scheduleadaptor sanoladaptor2 = new scheduleadaptor(getApplicationContext(), R.layout.schedulerow, summarylist);


                final ListView lvsummary = (ListView) findViewById(R.id.lvschedule);
                lvsummary.setAdapter(sanoladaptor2);


                SearchView sv = (SearchView) findViewById(R.id.svschedule);
                sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {

                        sanoladaptor2.getFilter().filter(newText);
                        return false;
                    }
                });

                lvsummary.setOnItemClickListener(

                        new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                schedulemodel obj = (schedulemodel) lvsummary.getAdapter().getItem(position);
                                String targetcabinet = obj.getTargetcabinet();
                                String migrationdate = obj.getMigrationdate();
                                String stopdate = obj.getStopdate();
                                String state = obj.getState();
                                String oldcabinet = obj.getOldcabinet();
                                String statussite = obj.getStatuscabinet();
                                String ckc1 = obj.getCkc1();
                                String ckc2 = obj.getCkc2();
                                String pmwno = obj.getPmwno();
                                String building = obj.getAbbr();
                                String sitename = obj.getSitename();
                                String projecttype = obj.getProjecttype();


                                Log.d("MyLog", "Value is: " + targetcabinet);

//                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://58.27.84.166/mcconline/MCC%20Online%20V3/ipmsan_android_graph.php?building=GS&stopdate=20-Nov-16&migrationdate=14-Nov-16&targetcabinet=GS_VC5003&oldcabinet=GS_008"));
//                                startActivity(browserIntent);

                                Intent i = new Intent(getApplicationContext(),
                                        graph.class);

                                i.putExtra("targetcabinet", targetcabinet);
                                i.putExtra("migrationdate", migrationdate);
                                i.putExtra("stopdate", stopdate);
                                i.putExtra("state", state);
                                i.putExtra("oldcabinet", oldcabinet);
                                i.putExtra("statussite", statussite);
                                i.putExtra("ckc1", ckc1);
                                i.putExtra("ckc2", ckc2);
                                i.putExtra("pmwno", pmwno);
                                i.putExtra("building", building);
                                i.putExtra("sitename", sitename);
                                i.putExtra("projecttype", projecttype);


                                startActivity(i);


                            }

                        });





            }


        });
    }

    public void progressdialogshow(){


        alertDialogprogress = new android.app.AlertDialog.Builder(schedule.this);

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
}

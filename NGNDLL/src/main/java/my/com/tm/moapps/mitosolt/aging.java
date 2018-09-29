package my.com.tm.moapps.mitosolt;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;


import java.util.ArrayList;

/**
 * Created by joe on 8/29/2016.
 */
public class aging extends AppCompatActivity {

    public static aging summaryactivityaging;
    private Context ctc ;
    public int iClickedItem = 0;
    public String building,basket,total;
    ArrayList<agingmodel> summarylist;
    android.app.AlertDialog.Builder alertDialogprogress;
    android.app.AlertDialog alertprogress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aging);



        summaryactivityaging = this;
//huhuggg

//        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
//        String uuid = telephonyManager.getDeviceId();
//        //String uuid="phonekhairiahndsb";

//        BluetoothAdapter m_BluetoothAdapter	= null; // Local Bluetooth adapter
//        m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        String uuid = m_BluetoothAdapter.getAddress();

//        String uuid = UUID.randomUUID().toString();


        progressdialogshow();

        new agingasynctask().execute(Config.APP_SERVER_URL15);


    }

    public static aging getInstance() {
        return summaryactivityaging;
    }

    public void updatelv(final ArrayList<agingmodel> agingsummary){

        this.runOnUiThread(new Runnable() {
            public void run() {

                summarylist = agingsummary;

                final agingadaptor sanoladaptor2 = new agingadaptor(getApplicationContext(), R.layout.agingrow, summarylist);
                final ListView lvsummary = (ListView) findViewById(R.id.lvaging);
                lvsummary.setAdapter(sanoladaptor2);





            }


        });
    }

    public void progressdialogshow(){


        alertDialogprogress = new android.app.AlertDialog.Builder(aging.this);

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

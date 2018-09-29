package my.com.tm.moapps.mitosolt;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.arturogutierrez.Badges;
import com.github.arturogutierrez.BadgesNotSupportedException;

import java.util.ArrayList;

import me.leolin.shortcutbadger.ShortcutBadger;

public class summary extends AppCompatActivity {

    public static summary summaryactivity;
    private Button btnload;

    private Context ctc ;
    public int iClickedItem = 0;
    private String building,basket,total;
    ArrayList<summarymodel> summarylist;
    android.app.AlertDialog.Builder alertDialogprogress;
    android.app.AlertDialog alertprogress;
    String uuid;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS =1;

    ArrayList<String> cabinetlist = new ArrayList<String>();




    private int badge = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary);



        summaryactivity = this;

        if (updatereasoncode.getInstance() != null) {
            updatereasoncode.getInstance().finish();
        }

        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        uuid = telephonyManager.getDeviceId();

        //permission for getting location

        //for googlemap api view generate map on fragment id map and allow the location



        cabinetlist.add("TGK_VB5001");

       // start alarm for location

       // scheduleAlarm();
        //String uuid="phonekhairiahndsb";




//        if(!SocketIOIntentService2.IS_RUNNING) {
//            sendBroadcast(new Intent(this, MyAlarmReceiver2.class).addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY));
//        }


//        BluetoothAdapter m_BluetoothAdapter	= null; // Local Bluetooth adapter
//        m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        String uuid = m_BluetoothAdapter.getAddress();

//        String uuid = UUID.randomUUID().toString();


      progressdialogshow();

        new summaryasynctask().execute(Config.APP_SERVER_URL26,uuid);

//        btnload = (Button) findViewById(R.id.button2);
//        btnload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                Intent i = new Intent(getApplicationContext(),
//                        LoadTT.class);
//
//
//                startActivity(i);
//                summaryactivity.finish();
//            }
//        });

//        start update location every 5 minutes
//        startService(new Intent(summary.this, locationupdateservice.class));



    }



    public static summary getInstance() {
        return summaryactivity;
    }

    public void updatelv(final ArrayList<summarymodel> summary){

        for(int i=0; i<summary.size(); i++) {
            summarymodel currentX = summary.get(i);
            // Do something with the value
            if(currentX.getBasket().equals("fiberhome"))
            badge = badge+Integer.parseInt(currentX.getTotal());
        }

        ShortcutBadger.applyCount(getApplicationContext(), badge);
//        try {
//            Log.d("BADGE count", String.valueOf(badge));
//            Badges.setBadge(getApplicationContext(), badge);
//        } catch (BadgesNotSupportedException badgesNotSupportedException) {
//            Log.d("BADGE ERROR", badgesNotSupportedException.getMessage());
//        }

        this.runOnUiThread(new Runnable() {
            public void run() {

                summarylist = summary;

                final summaryadaptor sanoladaptor2 = new summaryadaptor(getApplicationContext(), R.layout.customrow3, summarylist);
                final ListView lvsummary = (ListView) findViewById(R.id.lvsummary);
                lvsummary.setAdapter(sanoladaptor2);

                //this part to sent friendly reminder notification to mobile apps
                lvsummary.setOnItemClickListener(


                        new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                iClickedItem = position;



                                summarymodel summ = (summarymodel) lvsummary.getAdapter().getItem(position);
                                building = summ.getBuildingid();
                                basket = summ.getBasket();


                                total = summ.getTotal();
                                badge = badge+Integer.parseInt(total);


                                Intent cabinet = new Intent(getApplicationContext(),
                                        LoadTT.class);
                                cabinet.putExtra("exchange",building);
                                cabinet.putExtra("basket",basket);

                                startActivity(cabinet);




                            }

                        });


            }


        });
   }


    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                   new notifyuser().execute(building, basket, total);

                    Intent cabinet = new Intent(getApplicationContext(),
                            summary_cabinet.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    cabinet.putExtra("building",building);
                    cabinet.putExtra("basket",basket);
                    startActivity(cabinet);
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //Nothing
                    Intent cabinet2 = new Intent(getApplicationContext(),
                            summary_cabinet.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    cabinet2.putExtra("building",building);
                    cabinet2.putExtra("basket",basket);
                    startActivity(cabinet2);
                    break;
            }
        }
    };



    //whatsapp to fwd to whatsapp

    public void onClickWhatsApp() {

        PackageManager pm=getPackageManager();
        try {

            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");


            PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            //Check if package exists or not. If not then code
            //in catch block will be called
            waIntent.setPackage("com.whatsapp");

            StringBuffer buffer = new StringBuffer();

            String summaryarray= "";
            for (int i =0;i<summarylist.size();i++) {
                if(summarylist.get(i).getBasket().equals("ipmsanvendor"))

                buffer.append("Building:"+summarylist.get(i).getBuildingid()  + " Pending:"+summarylist.get(i).getTotal() + "\n\n");
            }

            summaryarray = buffer.toString();

            waIntent.putExtra(Intent.EXTRA_TEXT, "http//:www.mcc.summary"+
                    "\n" + summaryarray );

            startActivity(Intent.createChooser(waIntent, "Share link using"));

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                    .show();
        }

    }

    // request permission for android 6.0



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mymenu2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.verification:
                Intent i = new Intent(getApplicationContext(),
                        verification.class);


                startActivity(i);
                return true;
            case R.id.registration:
                Intent r = new Intent(getApplicationContext(),
                        MyActivity.class);


                startActivity(r);
                return true;

            case R.id.summary:
                Intent j = new Intent(getApplicationContext(),
                        summary.class);


                startActivity(j);


                return true;

            case R.id.listtt:
                Intent k = new Intent(getApplicationContext(),
                        LoadTT.class);


                startActivity(k);
                return true;

            case R.id.agingtt:
                Intent ag = new Intent(getApplicationContext(),
                        aging.class);


                startActivity(ag);
                return true;

            case R.id.schedule:
                Intent sh = new Intent(getApplicationContext(),
                        schedule.class);


                startActivity(sh);
                return true;

            case R.id.icpfiles:
                Intent icp = new Intent(getApplicationContext(),
                        icpbatchfiles.class);


                startActivity(icp);
                return true;

            case R.id.whatsapp:
               onClickWhatsApp();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    public void progressdialogshow(){


        alertDialogprogress = new android.app.AlertDialog.Builder(summary.this);

        alertprogress = alertDialogprogress.create();

        alertprogress.setTitle("Loading");

        LayoutInflater inflater = getLayoutInflater();

        // inflate the custom popup layout
        final View convertView = inflater.inflate(R.layout.progressdialog, null);
        // find the ListView in the popup layout
        final ProgressBar progressBar = (ProgressBar)convertView.findViewById(R.id.progressBar4);

        // setSimpleList(listView, comment);

        alertprogress.setView(convertView);
        alertprogress.setCanceledOnTouchOutside(false);
        alertprogress.show();

    }

    public void progressbarhide(){

        alertprogress.dismiss();

    }





}

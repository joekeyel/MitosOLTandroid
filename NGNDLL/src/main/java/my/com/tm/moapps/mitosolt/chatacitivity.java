package my.com.tm.moapps.mitosolt;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.PowerManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.socket.client.IO;
import io.socket.client.Socket;

public class chatacitivity extends AppCompatActivity {

    private static final String TAG = "" ;
    public static boolean VISIBLE = true;
    String msgstr,uuid;
    ListView chatmsg;
    static chatacitivity chat;
    auditadaptor sanoAdapter;
    ArrayList<auditmodel> msgmodel = new ArrayList<>() ;
    auditmodel msg = new auditmodel();
    public static String USERNAME3;
    private Socket mSocket;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    private PowerManager.WakeLock wl;

    {
        try {
            mSocket = IO.socket("http://58.27.84.188/");
        } catch (URISyntaxException e) {}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        moveTaskToBack(true);
        setContentView(R.layout.activity_chatacitivity);

        PowerManager pm = (PowerManager)getApplicationContext().getSystemService(Context.POWER_SERVICE);
        this.wl = pm.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE,TAG);
        wl.acquire();


        chat = this;



        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        uuid = telephonyManager.getDeviceId();

        getuser userobject = new getuser();
        userobject.execute(Config.APP_SERVER_URL6,uuid);


        msgstr = getIntent().getStringExtra("Notif");

        if(msgstr!="" || msgstr!=null){
        msg.setRemark(msgstr);

        Date c = Calendar.getInstance().getTime();
        String datetime = String.valueOf(c);

        msg.setUpdateby(datetime);

        // add to the array

        msgmodel.add(msg);



        chatmsg = (ListView) findViewById(R.id.listViewchat);
        sanoAdapter = new auditadaptor(getApplicationContext(), R.layout.auditrow,msgmodel);


        chatmsg.setAdapter(sanoAdapter);

        sanoAdapter.add(msg);
        sanoAdapter.notifyDataSetChanged();


        }

        if (savedInstanceState != null) {


            msgmodel = savedInstanceState.getParcelableArrayList("msgmodel");
            chatmsg = (ListView) findViewById(R.id.listViewchat);
            sanoAdapter = new auditadaptor(getApplicationContext(), R.layout.auditrow,msgmodel);


            chatmsg.setAdapter(sanoAdapter);
        }





}

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("msgmodel", msgmodel);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        msgmodel = savedInstanceState.getParcelableArrayList("msgmodel");


    }

    public static chatacitivity getinstance(){

     return chat;
    }

    public void addmesage(final String msgstr2){



        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                auditmodel msgmodel = new auditmodel();

                Date c = Calendar.getInstance().getTime();


                String datetime = String.valueOf(c);

                msgmodel.setUpdateby(datetime);

                msgmodel.setRemark(msgstr2);

                sanoAdapter.add(msgmodel);

               sanoAdapter.notifyDataSetChanged();

               scrollMyListViewToBottom();



            }
        });



    }

    private void scrollMyListViewToBottom() {
        chatmsg.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                chatmsg.setSelection(sanoAdapter.getCount() - 1);
            }
        });
    }
    @Override
    public void onBackPressed() {



        moveTaskToBack(true);
    }

    public void sentchat(View view){

        EditText etchat = (EditText)findViewById(R.id.etsubmit);
        String chatstr = etchat.getText().toString();

        if(chatstr!=null || chatstr!="") {

            mSocket.connect();
            mSocket.emit("chat message", USERNAME3 + ':' + chatstr);
            etchat.setText("");
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        VISIBLE = false;
    }

    @Override
    protected void onResume() {
        super.onPause();
        VISIBLE = false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
                    uuid = telephonyManager.getDeviceId();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onDestroy() {
        wl.release();
    }
}

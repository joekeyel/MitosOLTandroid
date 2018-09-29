package my.com.tm.moapps.mitosolt;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;

import static java.lang.Long.parseLong;


public class Main_Activity extends AppCompatActivity {

    private PopupWindow popWindow;
    private Toolbar mToolbar;
    private TextView tvData;
    private  TextView tvCreated;
    private TextView tvTTno;
    private ListView lvMovies;
    String ttno,No,createddate,servicenumber,ttnoactual,remark,status,reasoncode,referencenumber,cusmobile,basket,uuid,addressobject,agingstr;
    long agingint;
    public ProgressBar progressBar2;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    ArrayList<String> moviearray = new ArrayList<>();
    public static Main_Activity mainactivity2;
    private String username,basketuser;

    public static List<LatLng> LIST;

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://58.27.84.188/");
        } catch (URISyntaxException e) {}
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



//        BluetoothAdapter m_BluetoothAdapter	= null; // Local Bluetooth adapter
//        m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        uuid = m_BluetoothAdapter.getAddress();
//        uuid = UUID.randomUUID().toString();yuuujjjjj




        mainactivity2 = this;

        ttno = getIntent().getStringExtra("ttno");
        agingint = getIntent().getLongExtra("agingint",1);
        agingstr = getIntent().getStringExtra("agingstr");
        cusmobile = getIntent().getStringExtra("cusmobile");

        Uri data = getIntent().getData();

        if(data!=null){

        String ttno2 = data.getQueryParameter("ttno");


            ttno = ttno2;
        }


        mSocket.connect();

        lvMovies = (ListView)findViewById(R.id.lvttdetails);

        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar2.setMax(10);

        new JSONTask().execute(Config.APP_SERVER_URL2);

        //for android 5.0 getting the uuid

        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        uuid = telephonyManager.getDeviceId();


        //get username and basket and group from uuid using get user class
        new getuser().execute(Config.APP_SERVER_URL6, uuid);


        //to have popup of audit trail appear

//        audittrail audit = new audittrail(getApplicationContext());
//        audit.execute(Config.APP_SERVER_URL12, ttno);

        //to add comment in audit trail trail popup



    }

    public static Main_Activity getInstance(){

        return mainactivity2;
    }


    public class JSONTask extends AsyncTask<String, Integer, ArrayList<String>> {


        @Override
        protected ArrayList<String> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            String query = "?ttno="+ttno;


            try {
                URL url = new URL(params[0]+query);

                connection = (HttpURLConnection) url.openConnection();




                connection.connect();



                InputStream stream = connection.getInputStream();

                

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();


                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "");
                }

                String finalJson = buffer.toString();

                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("tt_info");
                JSONArray parentArray2 = parentObject.getJSONArray("masterlist");





                for (int i =0;i<parentArray.length();i++) {

                    JSONObject finalObject2 = parentArray2.getJSONObject(i);
                    JSONObject finalObject = parentArray.getJSONObject(i);

                    moviearray.add("Service Number:"+finalObject.getString("ServiceNo"));
                    moviearray.add("Login Id:"+finalObject.getString("referencenumber"));
                    moviearray.add("TT Number:"+finalObject.getString("TTno"));
                    moviearray.add("Symtom Code:"+finalObject.getString("symtomcode"));
                    moviearray.add("Created Date:"+finalObject.getString("Created_date"));
                    moviearray.add("Status Cabinet:"+finalObject.getString("statusmdf"));
                    moviearray.add("Basket:"+finalObject.getString("basket"));
                    moviearray.add("Cabinet ID:"+finalObject.getString("cabinetid"));
                    moviearray.add("Escalation Time:"+finalObject.getString("timeescalate"));
                    moviearray.add("Exchange ID:"+finalObject.getString("exchange"));
                    moviearray.add("Building:"+finalObject.getString("buildingid"));
                    moviearray.add("E-Side:"+finalObject2.getString("TARGET CABINET ESIDE"));
                    moviearray.add("D-Side:"+finalObject2.getString("TARGET CABINET DSIDE"));
                    moviearray.add("DSL-IN:"+finalObject2.getString("TARGET DSL IN"));
                    moviearray.add("DSL-OUT:"+finalObject2.getString("TARGET DSL OUT"));
                    moviearray.add("DSLAMID:"+finalObject2.getString("TARGET DSLAM ID"));
                    moviearray.add("LEN:"+finalObject2.getString("TARGET LEN"));
                    moviearray.add("Correct Pair:"+finalObject2.getString("CORRECT DSIDE PAIR"));
                    moviearray.add("SPEED:"+finalObject2.getString("SPEED"));


                    No = finalObject.getString("No");
                    createddate = finalObject.getString("Created_date");
                    servicenumber = finalObject.getString("ServiceNo");
                    referencenumber = finalObject.getString("referencenumber");
                    ttnoactual = finalObject.getString("TTno");
                    remark = finalObject.getString("remark");
                    status = finalObject.getString("statusmdf");
                    reasoncode = finalObject.getString("reasoncode");
                    basket = finalObject.getString("basket");

                }
                addressobject = parentObject.getString("address");
                moviearray.add("Address:"+addressobject);







                return moviearray;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            super.onPostExecute(result);

            if(result!=null) {

                ArrayList<String> sanosuke = result;


                ListAdapter sanoAdapter = new customadaptor(getApplicationContext(), sanosuke);
                ListView sanoview = (ListView) findViewById(R.id.lvttdetails);
                sanoview.setAdapter(sanoAdapter);


                sanoview.setOnItemClickListener(

                        new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {





                                audittrail audit = new audittrail(getApplicationContext());
                                audit.execute(Config.APP_SERVER_URL12, ttno);





                            }

                        });

                progressBar2.setVisibility(View.GONE);
            }

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar2.setProgress(values[0]);
        }
    }


    //whatsapp sharing

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

            String ttarray= "";
            for (int i =0;i<moviearray.size();i++) {

                buffer.append(moviearray.get(i) + "\n\n");
            }

            ttarray = buffer.toString();

            waIntent.putExtra(Intent.EXTRA_TEXT, "http//:www.mcc.app/ttno?ttno=" + ttno +
                    "\n" + ttarray );

            startActivity(Intent.createChooser(waIntent, "Share link using"));

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                    .show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.whatsapp, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.whatsapp:
                onClickWhatsApp();
                return true;

            case R.id.audit2:
                audittrail audit = new audittrail(getApplicationContext());
                audit.execute(Config.APP_SERVER_URL12, ttno);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }


    }

    // call this method when required to show popup


    public void onShowPopup2(final ArrayList<auditmodel> comment){


        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Main_Activity.this);

        final AlertDialog alert = alertDialog.create();



        alert.setTitle("Audit Trail");



        LayoutInflater inflater = getLayoutInflater();

        // inflate the custom popup layout
        final View convertView = inflater.inflate(R.layout.custom, null);
        // find the ListView in the popup layout
        final ListView listView = (ListView)convertView.findViewById(R.id.listView1);

        final auditadaptor trail = new auditadaptor(getApplicationContext(), R.layout.auditrow, comment);
        listView.setAdapter(trail);

       // setSimpleList(listView, comment);

        alert.setView(convertView);

        alert.show();




        final Button btnaddcoment = (Button)convertView.findViewById(R.id.commentadd);



        btnaddcoment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
                uuid = telephonyManager.getDeviceId();

                EditText etcommentaw = (EditText) convertView.findViewById(R.id.etcomment2);
                String commentss = etcommentaw.getText().toString();


                Toast.makeText(getBaseContext(), commentss, Toast.LENGTH_SHORT)
                        .show();
                UpdateTT update = new UpdateTT(getApplicationContext());

                update.execute(username, ttnoactual, status, commentss, No, servicenumber, createddate, uuid);
                String messagesc = username + " has update status " + status + " Remark:" + commentss;

                //Broadcast to socket io server
                mSocket.emit("chat message", messagesc);

                auditmodel newcomment = new auditmodel();

                newcomment.setRemark(commentss);
                newcomment.setUpdateby(username);
                //add new item to the listview
                trail.add(newcomment);
                trail.notifyDataSetChanged();
                //set to scroll to the latest position of the listview
                listView.post(new Runnable() {
                    @Override
                    public void run() {
                        // Select the last row so it will scroll into view...
                        listView.setSelection(trail.getCount() - 1);
                    }
                });

//                alert.dismiss();
//
//                audittrail audit = new audittrail(getApplicationContext());
//                audit.execute(Config.APP_SERVER_URL12, ttno);




            }
        });






    }

    void setSimpleList(ListView listView,ArrayList<auditmodel> comment){


        listView.setAdapter(new auditadaptor(getApplicationContext(), R.layout.auditrow, comment));
    }

    public void setuser(String usergroup,String b){

        //set the username and baskt of the user....this function was call from get user post execute class
        username = usergroup;
        basketuser = b;

    }


}


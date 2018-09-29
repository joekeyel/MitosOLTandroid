package my.com.tm.moapps.mitosolt;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;



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

import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by joe on 8/22/2016.
 */
public class LoadTTall extends AppCompatActivity {


    public static LoadTTall loadttall;

    public SearchView sv;
    String uuid,username, ttnoactual, status,No, servicenumber, createddate,basketuser;
    AlertDialog.Builder alertDialogprogress;
    AlertDialog alertprogress;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;



    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://58.27.84.188/");
        } catch (URISyntaxException e) {}
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loadtt);

        if (updatereasoncode.getInstance() != null) {
            updatereasoncode.getInstance().finish();
        }

        if (loadttall.getInstance() != null) {
            loadttall.getInstance().finish();
        }

        if (summary.getInstance() != null) {
            summary.getInstance().finish();
        }




        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        uuid = telephonyManager.getDeviceId();
        //   uuid="phonekhairiahndsb";

//        uuid = UUID.randomUUID().toString();

        progressdialogshow();

        new LoadTTall.JSONTask().execute(Config.APP_SERVER_URL14);
        new getuser().execute(Config.APP_SERVER_URL6, uuid);


        loadttall = this;

        mSocket.connect();





    }

    public static LoadTTall getInstance() {
        return loadttall;
    }

    public class JSONTask extends AsyncTask<String, Integer, ArrayList<ttmodel>> {


        @Override
        protected ArrayList<ttmodel>  doInBackground(String... params) {
            HttpURLConnection conn = null;
            BufferedReader reader = null;

            String query = "?uuid=" + uuid;


            try {
                URL url = new URL(params[0] + query);

                conn = (HttpURLConnection) url.openConnection();


                conn.connect();

                InputStream stream = conn.getInputStream();


                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();


                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "");
                }

                String finalJson = buffer.toString();

                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("listtt");



                ArrayList<String> moviearray = new ArrayList<>();
                ArrayList<ttmodel> ttmodellist = new ArrayList<>();






                for (int i = 0; i < parentArray.length(); i++) {

                    ttmodel ttmodelarray = new ttmodel();
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    moviearray.add(finalObject.getString("TTno"));
                    ttmodelarray.setTTno(finalObject.getString("TTno"));
                    ttmodelarray.setNo(finalObject.getString("No"));
                    ttmodelarray.setBasket(finalObject.getString("basket"));
                    ttmodelarray.setStatusmdf(finalObject.getString("statusmdf"));
                    ttmodelarray.setBuildingid(finalObject.getString("buildingid"));
                    ttmodelarray.setCreated_date(finalObject.getString("Created_date"));
                    ttmodelarray.setExchange(finalObject.getString("exchange"));
                    ttmodelarray.setServiceNo(finalObject.getString("ServiceNo"));
                    ttmodelarray.setReferencenumber(finalObject.getString("referencenumber"));
                    ttmodelarray.setReasoncode(finalObject.getString("reasoncode"));
                    ttmodelarray.setCabinetid(finalObject.getString("cabinetid"));
                    ttmodelarray.setCustomername(finalObject.getString("customer_name"));
                    ttmodelarray.setCustomermobile(finalObject.getString("customer_mobile_no"));
                    ttmodelarray.setRemark(finalObject.getString("remark"));
                    ttmodelarray.setSymtomcode(finalObject.getString("symtomcode"));
                    ttmodelarray.setRepeat(finalObject.getString("repeat"));
                    ttmodelarray.setPriority(finalObject.getString("priority"));



                    //add to tt model object array list
                    ttmodellist.add(ttmodelarray);

                }


                //        String[] sanostring = {"Hasanul","Rohani"};
                return ttmodellist;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.disconnect();
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
        protected void onPostExecute(ArrayList<ttmodel> result ) {
            super.onPostExecute(result);


            if (result != null) {

                ArrayList<ttmodel> sanosuke = result;


                final customadaptor2 sanoAdapter = new customadaptor2(getApplicationContext(), R.layout.customrow2, sanosuke);
                final ListView sanoview = (ListView) findViewById(R.id.lvttlist);
                sanoview.setAdapter(sanoAdapter);

                sv = (SearchView) findViewById(R.id.searchView);
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



                sanoview.setOnItemClickListener(

                        new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                                progressdialogshow();

                                ttmodel obj = (ttmodel) sanoview.getAdapter().getItem(position);
                                ttnoactual = obj.getTTno();
                                servicenumber = obj.getServiceNo();
                                createddate = obj.getCreated_date();
                                No = obj.getNo();
                                status = obj.getStatusmdf();



                                audittrail2 audit = new audittrail2(getApplicationContext());
                                audit.execute(Config.APP_SERVER_URL12, ttnoactual);



                            }

                        });

                progressbarhide();




            }


        }




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mymenu, menu);
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

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    public void progressdialogshow(){


        alertDialogprogress = new AlertDialog.Builder(LoadTTall.this);

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



    public void onShowPopup2(final ArrayList<auditmodel> comment){


        final AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(LoadTTall.this);

        final AlertDialog alert2 = alertDialog2.create();



        alert2.setTitle("Audit Trail");

        LayoutInflater inflater = getLayoutInflater();

        // inflate the custom popup layout
        final View convertView = inflater.inflate(R.layout.custom, null);
        // find the ListView in the popup layout
        final ListView listView = (ListView)convertView.findViewById(R.id.listView1);

        final auditadaptor trail = new auditadaptor(getApplicationContext(), R.layout.auditrow, comment);
        listView.setAdapter(trail);

        // setSimpleList(listView, comment);

        alert2.setView(convertView);

        alert2.show();



        final Button btnaddcoment = (Button)convertView.findViewById(R.id.commentadd);



        btnaddcoment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click


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

    public void setuser(String usergroup,String b){

        //set the username and baskt of the user....this function was call from get user post execute class
        username = usergroup;
        basketuser = b;

    }



}

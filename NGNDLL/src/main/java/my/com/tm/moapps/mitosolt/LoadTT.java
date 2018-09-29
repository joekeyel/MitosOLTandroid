package my.com.tm.moapps.mitosolt;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by joe on 1/24/2016.
 */
public class LoadTT extends AppCompatActivity {

    public static LoadTT loadtt;

    public SearchView sv;
    String uuid,ttno;
    AlertDialog.Builder alertDialogprogress;
    AlertDialog alertprogress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loadtt);




        Intent i = getIntent();
        String exchange = i.getStringExtra("exchange");
        String basket = i.getStringExtra("basket");

        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        uuid = telephonyManager.getDeviceId();
         //   uuid="phonekhairiahndsb";

//        uuid = UUID.randomUUID().toString();

        progressdialogshow();

        new JSONTask().execute(Config.APP_SERVER_URL27,exchange,basket);


        loadtt = this;




    }

    public static LoadTT getInstance() {
        return loadtt;
    }

    public class JSONTask extends AsyncTask<String, Integer, ArrayList<ttmodel>> {


        @Override
        protected ArrayList<ttmodel>  doInBackground(String... params) {
            HttpURLConnection conn = null;
            BufferedReader reader = null;

            params[1] = params[1].replaceAll(" ", "%20");
            String query = "?exchange=" + params[1]+"&basket="+params[2];


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



                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");

                    try {

                        Calendar c = Calendar.getInstance();
                       // System.out.println("Current time => " + c.getTime());

                        SimpleDateFormat df = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");
                        String formattedDate = df.format(c.getTime());

                        Date date1 = simpleDateFormat.parse(formattedDate);
                        //Date date1 = simpleDateFormat.parse("10/10/2013 11:30:10");
                        Date date2 = simpleDateFormat.parse(finalObject.getString("Created_date"));


                        ttmodelarray.setAgingstr( printDifference(date2, date1));
                        ttmodelarray.setAgingint(printDifferenceint(date2,date1));

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

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

                                ttmodel obj = (ttmodel) sanoview.getAdapter().getItem(position);
                                String tTno = obj.getTTno();
                                ttno = obj.getTTno();
                                String servicenumber = obj.getServiceNo();
                                String createddate = obj.getCreated_date();
                                String No = obj.getNo();
                                String remark = obj.getRemark();
                                String status = obj.getStatusmdf();
                                String reasoncode = obj.getReasoncode();
                                String cusmobile = obj.getCustomermobile();
                                long agingint = obj.getAgingint();
                                String agingstr = obj.getAgingstr();


                                Log.d("MyLog", "Value is: " + tTno);

                                Intent i = new Intent(getApplicationContext(),
                                        Main_Activity.class);

                                i.putExtra("ttno", tTno);
                                i.putExtra("servicenumber", servicenumber);
                                i.putExtra("createddate", createddate);
                                i.putExtra("No", No);
                                i.putExtra("Remark", remark);
                                i.putExtra("statusmdf", status);
                                i.putExtra("reasoncode", reasoncode);
                                i.putExtra("cusmobile", cusmobile);
                                i.putExtra("agingint", agingint);
                                i.putExtra("agingstr", agingstr);


                                startActivity(i);


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

            case R.id.audittrail:

                audittrail audit = new audittrail(getApplicationContext());
                audit.execute(Config.APP_SERVER_URL12, ttno);


                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void progressdialogshow(){


        alertDialogprogress = new AlertDialog.Builder(LoadTT.this);

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

    public String printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;


        String result =
                elapsedDays+ " days, "+elapsedHours+" hours " + elapsedMinutes+" minutes";

//        System.out.println(result);

//        long mills = endDate.getTime() - startDate.getTime();
//        int hours = (int) (mills/(1000 * 60 * 60));
//        int mins = (int) ((mills/(1000*60)) % 60);
//        int days = (int) ((mills/(1000*60)) % 60);
//
//        String diff = hours + " Hours:" + mins +" Minutes";
        return result;
    }

    public long printDifferenceint(Date startDate, Date endDate) {
        //milliseconds
        long mills = endDate.getTime() - startDate.getTime();
        int hours = (int) (mills/(1000 * 60 * 60));

        return hours;
    }



  }


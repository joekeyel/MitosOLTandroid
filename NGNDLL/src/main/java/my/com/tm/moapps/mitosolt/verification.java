package my.com.tm.moapps.mitosolt;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

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
import java.util.ArrayList;

/**
 * Created by joe on 6/5/2016.
 */
public class verification extends AppCompatActivity {

    private ProgressBar progressBar;
    private Spinner spinner;
    private String exchangeid;
    public static verification verificationact;
    public static verificationadaptor sanoAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verification);

        progressBar = (ProgressBar) findViewById(R.id.progressBar3);

        new JSONTask3().execute(Config.APP_SERVER_URL7);

        verificationact = this;



    }

    public static verification getInstance(){
        return   verificationact;
    }

    public void updatewhenclick2(){

        progressBar.setVisibility(View.VISIBLE);

        Spinner mySpinner=(Spinner) findViewById(R.id.exchangespinner);


        String exchange = mySpinner.getSelectedItem().toString();
        new JSONTask4().execute(Config.APP_SERVER_URL8,exchange);



    }

    public void updatewhenclick3(){

        progressBar.setVisibility(View.VISIBLE);

        Spinner mySpinner=(Spinner) findViewById(R.id.exchangespinner);


        String exchange = mySpinner.getSelectedItem().toString();
        new JSONTask4().execute(Config.APP_SERVER_URL9,exchange);



    }

    public void updatewhenclick4(){

        progressBar.setVisibility(View.VISIBLE);

        Spinner mySpinner=(Spinner) findViewById(R.id.exchangespinner);


        String exchange = mySpinner.getSelectedItem().toString();
        new JSONTask4().execute(Config.APP_SERVER_URL10,exchange);



    }

    public void updatewhenclick5(){

        progressBar.setVisibility(View.VISIBLE);

        Spinner mySpinner=(Spinner) findViewById(R.id.exchangespinner);


        String exchange = mySpinner.getSelectedItem().toString();
        new JSONTask4().execute(Config.APP_SERVER_URL11,exchange);



    }

    public void updateviewlist(int index,String remark){

        //find the listview
        ListView lv = (ListView)findViewById(R.id.lvverify);
       // find the custome adaptor
        verificationadaptor sanadaptor = (verificationadaptor) lv.getAdapter();
        // sent variable to the custome adaptor to change the view
        sanadaptor.setSelection1(index,remark);

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



    public class JSONTask3 extends AsyncTask<String, Integer, ArrayList<String>> {


        @Override
        protected ArrayList<String> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;


            try {
                URL url = new URL(params[0]);

                connection = (HttpURLConnection) url.openConnection();

                connection.connect();

                ArrayList<String> closedcodearray = new ArrayList<>();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();


                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "");
                }

                String finalJson = buffer.toString();

                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("exchangeid");

                for (int i = 0; i < parentArray.length(); i++) {


                    JSONObject finalObject = parentArray.getJSONObject(i);

                    closedcodearray.add(finalObject.getString("exchangeid"));


                }


                return closedcodearray;

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


            ArrayList<String> sanosuke = result;

            if (sanosuke != null) {

                spinner = (Spinner) findViewById(R.id.exchangespinner);

                ArrayAdapter adapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_2, android.R.id.text2, sanosuke);

                spinner.setAdapter(adapter);


            }


            progressBar.setVisibility(View.GONE);

        }



        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }


    }

    // asynctask to load verify list


    public class JSONTask4 extends AsyncTask<String, Integer, ArrayList<verifymodel>> {
     private String total;

        @Override
        protected ArrayList<verifymodel> doInBackground(String... params) {
            HttpURLConnection conn = null;
            BufferedReader reader = null;

            String query = "?exchange="+params[1];


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
                JSONArray parentArray = parentObject.getJSONArray("masterlist");


                ArrayList<verifymodel> verifymodellist = new ArrayList<>();


                for (int i = 0; i < parentArray.length(); i++) {



                    verifymodel verifymodelarray = new verifymodel();
                    JSONObject finalObject = parentArray.getJSONObject(i);

                    verifymodelarray.setIdverification(finalObject.getString("idverification"));
                    verifymodelarray.setExchangeid(finalObject.getString("exchangeid"));
                    verifymodelarray.setCabinetdsidepair(finalObject.getString("cabinetdsidepair"));
                    verifymodelarray.setService_num(finalObject.getString("service_num"));
                    verifymodelarray.setService_type(finalObject.getString("service_type"));
                    verifymodelarray.setRemark(finalObject.getString("remark"));


                    //add to tt model object array list
                    verifymodellist.add(verifymodelarray);

                    total = String.valueOf(i);

                }


                //        String[] sanostring = {"Hasanul","Rohani"};
                return verifymodellist;



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
        protected void onPostExecute(ArrayList<verifymodel> result ) {
            super.onPostExecute(result);




            if (result != null) {

                ArrayList<verifymodel> sanosuke = result;


                 sanoAdapter = new verificationadaptor(getApplicationContext(), R.layout.verificationlayout, sanosuke);
                final ListView sanoview = (ListView) findViewById(R.id.lvverify);
                sanoview.setAdapter(sanoAdapter);


               SearchView sv = (SearchView) findViewById(R.id.searchView2);
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

                TextView summarytv = (TextView) findViewById(R.id.summarytv);
                summarytv.setText(total);



                sanoview.setOnItemClickListener(

                        new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                verifymodel obj = (verifymodel) sanoview.getAdapter().getItem(position);
                                String exchangeid = obj.getExchangeid();
                                String servicenumber = obj.getService_num();
                                String dsidepair = obj.getCabinetdsidepair();
                                String servicetype = obj.getService_type();
                                String remark = obj.getRemark();
                                String idverification = obj.getIdverification();
                                String positionstr = String.valueOf(position);


                                Intent i = new Intent(getApplicationContext(),
                                        verify_sub.class);

                                i.putExtra("exchangeid", exchangeid);
                                i.putExtra("servicenumber", servicenumber);
                                i.putExtra("dsidepair", dsidepair);
                                i.putExtra("servicetype", servicetype);
                                i.putExtra("remark", remark);
                                i.putExtra("position", positionstr);
                                i.putExtra("idverification", idverification);




                                startActivity(i);




                            }

                        });




            }
            progressBar.setVisibility(View.GONE);

        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }



    }


        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.verifiction_menu, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle item selection
            switch (item.getItemId()) {
                case R.id.capacity:
                    updatewhenclick2();
                    return true;
                case R.id.workingsub:
                    updatewhenclick3();
                    return true;
                case R.id.pending:
                    updatewhenclick4();
                    return true;
                case R.id.completed:
                    updatewhenclick5();
                    return true;

                default:
                    return super.onOptionsItemSelected(item);
            }
        }





}

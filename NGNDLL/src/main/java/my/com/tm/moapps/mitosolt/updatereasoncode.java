package my.com.tm.moapps.mitosolt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;

import android.widget.Spinner;
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


public class updatereasoncode extends AppCompatActivity{


    public static updatereasoncode activity2;
     String ttno,username,statusmdf,remarks,Nombor,servicenum,createddate,uuid,reasoncode,reasoncode2,messagesc,basketuser,baskettt;
     Spinner reasoncodespinner;

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://58.27.84.188/");
        } catch (URISyntaxException e) {}
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updatereasoncode);

       mSocket.connect();

        ttno = getIntent().getStringExtra("ttno");
        Nombor = getIntent().getStringExtra("No");
        servicenum = getIntent().getStringExtra("servicenumber");
        createddate= getIntent().getStringExtra("createddate");
        remarks = getIntent().getStringExtra("Remark");
        statusmdf = getIntent().getStringExtra("statusmdf");
        uuid = getIntent().getStringExtra("uuid");
        reasoncode2 = getIntent().getStringExtra("reasoncode");
        baskettt = getIntent().getStringExtra("basket");

        username = "AND SB";

        new getuser().execute(Config.APP_SERVER_URL6,uuid);




        messagesc = "Mobile: "+username + " Has Submit " + ttno + " to MCC";

                new JSONTask2().execute(Config.APP_SERVER_URL4);

        activity2 = this;
    }

    public static updatereasoncode getInstance(){
        return   activity2;
    }

    public void updatereason(View view){

        if(baskettt.equals(basketuser)){

            new AlertDialog.Builder(this)
                    .setTitle("Submit to MCC")
                    .setMessage("Do you want to Submit this TT?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {

                            reasoncodespinner = (Spinner) findViewById(R.id.spinnerreasoncode);


                            reasoncode = reasoncodespinner.getSelectedItem().toString();

                            UpdateTT2 update = new UpdateTT2(getApplicationContext());
                            update.execute(ttno, username, statusmdf, remarks, Nombor, servicenum, createddate, uuid, reasoncode);

                            Intent i = new Intent(getApplicationContext(), summary.class);

                            i.putExtra("ttno", ttno);
                            i.putExtra("reasoncode", reasoncode);
                            i.putExtra("remarks", remarks);

                            startActivity(i);

                            Main.getInstance().finish();
                            LoadTT.getInstance().finish();
                            finish();


                            Toast.makeText(updatereasoncode.this, "Submitted to MCC", Toast.LENGTH_SHORT).show();
                            mSocket.emit("chat message", messagesc);
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();


        }



    }


    public class JSONTask2 extends AsyncTask<String, String, ArrayList<String>> {


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
                JSONArray parentArray = parentObject.getJSONArray("reasoncode");

                for (int i =0;i<parentArray.length();i++) {


                    JSONObject finalObject = parentArray.getJSONObject(i);

                    closedcodearray.add(finalObject.getString("closed_code"));


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

            if(sanosuke != null) {

                Spinner spinner = (Spinner) findViewById(R.id.spinnerreasoncode);

                ArrayAdapter adapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_2, android.R.id.text2, sanosuke);

                spinner.setAdapter(adapter);

                spinner.setSelection(adapter.getPosition(reasoncode2));

            }





        }
    }

    public void setuserupdate(String usergroup,String b){

        username = usergroup;
        basketuser = b;


    }




}


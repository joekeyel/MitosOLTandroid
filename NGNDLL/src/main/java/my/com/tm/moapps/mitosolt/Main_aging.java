package my.com.tm.moapps.mitosolt;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
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
import java.net.URLEncoder;
import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;


public class Main_aging extends AppCompatActivity {

    String agingstr;
    String cusmobile;
    String ttno;
    String No;
    String servicenumber;
    String remark;
    String statusmdf;
    String reasoncode;
    String referencenumber;
    String basket;
    String address;
    String agingint;
    String created;
    String username;
    String uuid;
    String delayreasonsel = "No reason";


    private static Main_aging agingactivity;

    EditText delayreason;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_aging);

        agingactivity = this;

        agingstr = getIntent().getStringExtra("agingstr");
        cusmobile = getIntent().getStringExtra("cusmobile");
        ttno = getIntent().getStringExtra("ttno");
        No = getIntent().getStringExtra("No");
        servicenumber = getIntent().getStringExtra("servicenumber");
        created = getIntent().getStringExtra("createddate");
        remark = getIntent().getStringExtra("Remark");
        statusmdf = getIntent().getStringExtra("statusmdf");
        reasoncode = getIntent().getStringExtra("reasoncode");
        referencenumber = getIntent().getStringExtra("referencenumber");
        basket = getIntent().getStringExtra("basket");
        address = getIntent().getStringExtra("address");
        agingint = getIntent().getStringExtra("agingint");


        TextView ttnotv = (TextView) findViewById(R.id.tvttnoaging);
        TextView servicenumbertv = (TextView) findViewById(R.id.tvservicenumaging);
        TextView customermobiletv = (TextView) findViewById(R.id.tvcusmobileaging);
        TextView aging = (TextView) findViewById(R.id.tvagingstr);
        delayreason = (EditText) findViewById(R.id.remarkTextaging);


        ttnotv.setText(ttno);
        servicenumbertv.setText(servicenumber);
        customermobiletv.setText(referencenumber);
        aging.setText(agingstr);

        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);


        uuid = telephonyManager.getDeviceId();


//get username and basket and group from uuid using get user class
        new getuser().execute(Config.APP_SERVER_URL6, uuid);


        new JSONTask2().execute(Config.APP_SERVER_URL25);

    }

    public void updatedelayreason(View view) {
        EditText delayet = (EditText) findViewById(R.id.remarkTextaging);
        String delayreason = delayet.getText().toString();



        Spinner spinner = (Spinner) findViewById(R.id.delayreasonselect);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                delayreasonsel = parent.getItemAtPosition(pos).toString();

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });




        if (!delayreason.isEmpty() && !delayreasonsel.equals("No reason")) {

            Updatedelayreasontt update = new Updatedelayreasontt(getApplicationContext());

            update.execute(No, ttno, delayreason,username,servicenumber,delayreasonsel);



        }
        else{

            Toast.makeText(getApplicationContext(), "Pls Insert Delay Reason remark and Select Delay Reason  ",
                    Toast.LENGTH_LONG).show();
        }
    }



    public void setuser(String usergroup,String b){

        //set the username and baskt of the user....this function was call from get user post execute class
        username = usergroup;
        basket = b;


    }
    public void toupdatemain(){

        Intent i = new Intent(getApplicationContext(), updatereasoncode.class);


        i.putExtra("ttno", ttno);
        i.putExtra("servicenumber", servicenumber);
        i.putExtra("createddate", created);
        i.putExtra("No", No);
        i.putExtra("Remark", remark);
        i.putExtra("statusmdf", statusmdf);
        i.putExtra("uuid", uuid);
        i.putExtra("reasoncode", reasoncode);
        i.putExtra("basket", basket);

        startActivity(i);

    }

    public static Main_aging getInstance(){

        return agingactivity;
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

                Spinner spinner = (Spinner) findViewById(R.id.delayreasonselect);

                ArrayAdapter adapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_2, android.R.id.text2, sanosuke);

                spinner.setAdapter(adapter);

               // spinner.setSelection(adapter.getPosition(reasoncode2));

            }





        }
    }
}

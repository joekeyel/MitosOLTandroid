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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.socket.client.IO;
import io.socket.client.Socket;


public class Main extends AppCompatActivity implements View.OnClickListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private static final String TAG = "APP KEY";


    public static final String APP_KEY = "app_key";
    public static String PHONECALLER = "";
    private String PHONECALLEE = "";
    private String latstr2, lngstr2;

    private PopupWindow popWindow;
    TextView servicenumber, servicenumber2;
    ImageButton sessionbutton1, sessionbutton2, imagebuttoncall2, imageButtoncall1;
    EditText remark;
    private static Main mainactivity;
    String ttno, indicator, username, statusstr, remark1, Nombor, servicenum, createdstr, uuid, reasoncodestr, remark2, statusstr2, referencenumber, cusmobilestr, basketuser, baskettt, address;
    String newaddress1;
    long agingint;
    String agingstr;


    PlaceAutocompleteFragment autocompleteFragment;
    AlertDialog alert;

    private SpeechRecognizer sr;

    private Socket mSocket;

    {
        try {
            mSocket = IO.socket("http://58.27.84.188/");
        } catch (URISyntaxException e) {
        }
    }

    AsyncTask<Void, Void, JSONObject> getlatlng;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        mainactivity = this;

        mSocket.connect();


        //uuid="phonekhairiahndsb";

//        BluetoothAdapter m_BluetoothAdapter	= null; // Local Bluetooth adapter
//        m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        uuid = m_BluetoothAdapter.getAddress();

//        uuid = UUID.randomUUID().toString();


        username = "AND SB";
        ttno = getIntent().getStringExtra("ttno");
        Nombor = getIntent().getStringExtra("No");
        servicenum = getIntent().getStringExtra("servicenum");
        createdstr = getIntent().getStringExtra("created");
        remark1 = getIntent().getStringExtra("Remark");
        statusstr = getIntent().getStringExtra("statusmdf");
        reasoncodestr = getIntent().getStringExtra("reasoncode");
        referencenumber = getIntent().getStringExtra("referencenumber");
        cusmobilestr = getIntent().getStringExtra("cusmobile");
        baskettt = getIntent().getStringExtra("basket");
        agingint = getIntent().getLongExtra("agingint", 1);
        agingstr = getIntent().getStringExtra("agingstr");


        address = getIntent().getStringExtra("address");

        //get latlng base on address
        final Context context = this;


        servicenumber = (TextView) findViewById(R.id.tvservicenum);
        servicenumber2 = (TextView) findViewById(R.id.tvttno);

        sessionbutton1 = (ImageButton) findViewById(R.id.sessiontest1);
        sessionbutton2 = (ImageButton) findViewById(R.id.sessiontest2);

        imagebuttoncall2 = (ImageButton) findViewById(R.id.imageButton2);
        imageButtoncall1 = (ImageButton) findViewById(R.id.imageButton);


        //this to make the button test radius appear or not appear


        if (TextUtils.isDigitsOnly(referencenumber)) {
            System.out.println("Matches" + referencenumber);
            sessionbutton1.setVisibility(View.INVISIBLE);
        } else {
            System.out.println("No Match" + referencenumber);
            sessionbutton1.setVisibility(View.VISIBLE);
            imagebuttoncall2.setVisibility(View.INVISIBLE);

        }

        if (TextUtils.isDigitsOnly(servicenum)) {
            System.out.println("Matches" + servicenum);
            sessionbutton2.setVisibility(View.INVISIBLE);
        } else {
            System.out.println("No Match" + servicenum);
            sessionbutton2.setVisibility(View.VISIBLE);
            imageButtoncall1.setVisibility(View.INVISIBLE);
        }


        final TextView nameuser = (TextView) findViewById(R.id.tvttno);
        final TextView phonenum = (TextView) findViewById(R.id.tvservicenum);
        final TextView cusmobile = (TextView) findViewById(R.id.tvcusmobile);
        final EditText remark = (EditText) findViewById(R.id.remarkText);


        nameuser.setText(referencenumber);
        phonenum.setText(servicenum);
        cusmobile.setText(cusmobilestr);

        remark.setText(remark1);


        Spinner spinner = (Spinner) findViewById(R.id.statusmdfspinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Status, android.R.layout.simple_spinner_dropdown_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setSelection(adapter.getPosition(statusstr));

        //for android 5 and above need to request without asking permission

        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        uuid = telephonyManager.getDeviceId();
//get username and basket and group from uuid using get user class
        new getuser().execute(Config.APP_SERVER_URL6, uuid);


        address.trim();

        if (TextUtils.isEmpty(address) || TextUtils.equals(address, "") || TextUtils.equals(address, null)) {

            address = "add0,add1,add2,add3,add4,add5,add6,add7,add8,add9";

            // showeditaddress(address);
        }


        Button speakButton = (Button) findViewById(R.id.Speak);

        speakButton.setOnClickListener(this);

        sr = SpeechRecognizer.createSpeechRecognizer(this);
        sr.setRecognitionListener(new listener());


    }
    //for speech recognizer

    class listener implements RecognitionListener {

        final TextView mText = (TextView) findViewById(R.id.tvtestcallresult);
        final TextView remark = (EditText) findViewById(R.id.remarkText);

        public void onReadyForSpeech(Bundle params) {
            Log.d(TAG, "onReadyForSpeech");
        }

        public void onBeginningOfSpeech() {
            Log.d(TAG, "onBeginningOfSpeech");
        }

        public void onRmsChanged(float rmsdB) {
            Log.d(TAG, "onRmsChanged");
        }

        public void onBufferReceived(byte[] buffer) {
            Log.d(TAG, "onBufferReceived");
        }

        public void onEndOfSpeech() {
            Log.d(TAG, "onEndofSpeech");
        }

        public void onError(int error) {
            Log.d(TAG, "error " + error);
            mText.setText("error " + error);
        }

        public void onResults(Bundle results) {
            String str = new String();
            Log.d(TAG, "onResults " + results);
            ArrayList data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            for (int i = 0; i < data.size(); i++) {
                Log.d(TAG, "result " + data.get(i));
                str = (String) data.get(0);


            }

            String mapstr = "TT no:" + ttno + "<br>Serviceid:" + servicenum + "<br>Serviceid:" + referencenumber + "<br>" + str;
            remark.append(str);


            //broadcast to realtime server

            mSocket.emit("chat message", MyActivity.USERNAME1 + "Speak:" + str);
            if (ShareExternalServer.DATA != "" || !ShareExternalServer.DATA.equals(null)) {
                mSocket.emit("send:coords", MyActivity.UUIDLATLNG + "," + mapstr);
            }
        }

        public void onPartialResults(Bundle partialResults) {
            Log.d(TAG, "onPartialResults");
        }

        public void onEvent(int eventType, Bundle params) {
            Log.d(TAG, "onEvent " + eventType);
        }


    }


    public static Main getInstance() {

        return mainactivity;
    }

    public void updateTheTextView(final String t) {
        this.runOnUiThread(new Runnable() {
            public void run() {

                String incoming = t;


                remark = (EditText) findViewById(R.id.remarkText);
                remark.append("\nSystem generated Test call " + incoming + " by " + username);


                TextView textV1 = (TextView) findViewById(R.id.tvtestcallresult);
                textV1.setText("Dialed/Receive Number is " + incoming);
                mSocket.emit("chat message", "System generated Test call " + incoming + " by " + username);

            }
        });
    }

    public void updateTheTextViewoms(final omsresponsemodel t) {
        this.runOnUiThread(new Runnable() {
            public void run() {

                TextView textV11 = (TextView) findViewById(R.id.tvtestcallresult);


                String resultcode = t.getResultcode();


                textV11.setText("Result Code:" + resultcode);


                String accesstoken = t.getAccess_token();
                String refreshtoken = t.getRefresh_token();
                Integer expiresin = t.getExpires_in();


                textV11.setText("Access Token:" + accesstoken);
                textV11.setText("Refresh Token:" + refreshtoken);
                textV11.setText("Epire" + expiresin);

                Log.e("OMS RESPONSE", "accesstoken:" + accesstoken);
                Log.e("OMS RESPONSE", "refreshtoken:" + refreshtoken);
                Log.e("OMS RESPONSE", "expiresin:" + expiresin);
                Log.e("OMS RESPONSE", "Result Code:" + resultcode);


                Log.e("APP KEY", "Get App Key:" + APP_KEY);


                String callerno = "+6" + PHONECALLER;

                Log.e("CALLEE:", PHONECALLEE);
                Log.e("CALLER", callerno);

                callomsasynctask2 callcaas = new callomsasynctask2();
                callcaas.execute(Config.APP_SERVER_URL21, APP_KEY, PHONECALLEE, callerno);


//                mSocket.emit("chat message", "System generated Test call "+incoming +" by "+username);

            }
        });
    }


    public void updateTheTextViewoms2(final String t) {
        this.runOnUiThread(new Runnable() {
            public void run() {

                TextView textV11 = (TextView) findViewById(R.id.tvtestcallresult);


                String resultcode = t;
                textV11.setText("Result Code:" + resultcode);
                Log.e("OMS RESPONSE", "accesstoken 2nd cycle:" + t);


//                mSocket.emit("chat message", "System generated Test call "+incoming +" by "+username);

            }
        });
    }

    public long printDifferenceint(Date startDate, Date endDate) {
        //milliseconds
        long mills = endDate.getTime() - startDate.getTime();
        int hours = (int) (mills/(1000 * 60 * 60));

        return hours;
    }

    public void updatewhenclick(View view) {

        //if basket of the user same as basket of the tt ...the tt can be edited
        
        //get time diff
        
        long aginginhour = 0;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");

        try {

            Calendar c = Calendar.getInstance();
            // System.out.println("Current time => " + c.getTime());

            SimpleDateFormat df = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");
            String formattedDate = df.format(c.getTime());

            Date date1 = simpleDateFormat.parse(formattedDate);
            //Date date1 = simpleDateFormat.parse("10/10/2013 11:30:10");
            Date date2 = simpleDateFormat.parse(createdstr);


            aginginhour =  printDifferenceint(date2, date1);
           

        } catch (ParseException e) {
            e.printStackTrace();
        }


        if (baskettt.equals(basketuser)) {
            if (aginginhour > 24) {
                Spinner mySpinner = (Spinner) findViewById(R.id.statusmdfspinner);
                EditText myremark = (EditText) findViewById(R.id.remarkText);


                statusstr2 = mySpinner.getSelectedItem().toString();
                remark2 = myremark.getText().toString();
                indicator = "update status";

                UpdateTT update = new UpdateTT(getApplicationContext());

                update.execute(username, ttno, statusstr2, remark2, Nombor, servicenum, createdstr, uuid);
                String messagesc = username + " has update status " + statusstr2 + " Remark:" + remark2;

                //Broadcast to socket io server
                mSocket.emit("chat message", messagesc);

                Intent i = new Intent(getApplicationContext(), Main_aging.class);


                i.putExtra("ttno", ttno);
                i.putExtra("servicenumber", servicenum);
                i.putExtra("createddate", createdstr);
                i.putExtra("No", Nombor);
                i.putExtra("Remark", remark2);
                i.putExtra("statusmdf", statusstr2);
                i.putExtra("uuid", uuid);
                i.putExtra("reasoncode", reasoncodestr);
                i.putExtra("basket", baskettt);
                i.putExtra("agingstr", agingstr);
                i.putExtra("agingint", agingint);

                startActivity(i);
            } else {

                Spinner mySpinner = (Spinner) findViewById(R.id.statusmdfspinner);
                EditText myremark = (EditText) findViewById(R.id.remarkText);


                statusstr2 = mySpinner.getSelectedItem().toString();
                remark2 = myremark.getText().toString();
                indicator = "update status";

                UpdateTT update = new UpdateTT(getApplicationContext());

                update.execute(username, ttno, statusstr2, remark2, Nombor, servicenum, createdstr, uuid);
                String messagesc = username + " has update status " + statusstr2 + " Remark:" + remark2;

                //Broadcast to socket io server
                mSocket.emit("chat message", messagesc);

                Intent i = new Intent(getApplicationContext(), updatereasoncode.class);


                i.putExtra("ttno", ttno);
                i.putExtra("servicenumber", servicenum);
                i.putExtra("createddate", createdstr);
                i.putExtra("No", Nombor);
                i.putExtra("Remark", remark2);
                i.putExtra("statusmdf", statusstr2);
                i.putExtra("uuid", uuid);
                i.putExtra("reasoncode", reasoncodestr);
                i.putExtra("basket", baskettt);

                startActivity(i);

            }

        }

    }

    public void callnumber(View v) {


        String phone_no = servicenumber.getText().toString();
        StringBuilder phone = new StringBuilder(phone_no);

        if(phone_no.startsWith("3")){

            phone.insert(0, "0");
        }else {
            phone.deleteCharAt(1);
            phone.insert(0, "0");
        }

        //  PHONECALLEE = "+6"+phone.toString();
//
//
//
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + phone));



            startActivity(callIntent);



//        callomsasynctask calloms = new callomsasynctask();
//        calloms.execute(Config.APP_SERVER_URL20,"CALL2");

        // teststreamx(phone_no);

    }

    public void teststreamx1(View v) {
        String phone_no = servicenumber2.getText().toString();

        callradiustest radisutest = new callradiustest();
        radisutest.execute(phone_no);
    }

    public void teststreamx2(View v) {
        String phone_no2 = servicenumber.getText().toString();

        callradiustest radisutest = new callradiustest();
        radisutest.execute(phone_no2);
    }


    public void callnumber2(View v) {


        String phone_no2 = servicenumber2.getText().toString();
        StringBuilder phone2 = new StringBuilder(phone_no2);

        if(phone_no2.startsWith("3")){

            phone2.insert(0, "0");
        }else {
            phone2.deleteCharAt(1);
            phone2.insert(0, "0");
        }
        // PHONECALLEE = "+6"+phone2.toString();

        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + phone2));


           startActivity(callIntent);


        //  teststreamx(phone_no2);

//        callomsasynctask calloms = new callomsasynctask();
//        calloms.execute(Config.APP_SERVER_URL20,"CALL1");


    }

    public void callnumber3(View v) {


        String phone_no3 = cusmobilestr;
        StringBuilder phone3 = new StringBuilder(phone_no3);

        phone3.insert(0, "0");

        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + phone3));

             PHONECALLEE = "+6"+phone3.toString();


           startActivity(callIntent);
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)



//        callomsasynctask calloms = new callomsasynctask();
//        calloms.execute(Config.APP_SERVER_URL20,"CALL3");


    }

    public void setuser(String usergroup,String b){

        //set the username and baskt of the user....this function was call from get user post execute class
        username = usergroup;
        basketuser = b;

    }


    // this to query latlng base on address given


    public void showeditaddress(final String addressfinal){

         String state;
        final String streetno;
        final String poskod;
        final String suburb;
        final String city;
        final String streetname;
        final String result;
        String[] splitaddress;



        splitaddress = addressfinal.split(",");

        state = splitaddress[3];
        streetno = splitaddress[4];
        poskod = splitaddress[5];
        suburb = splitaddress[6];
        city = splitaddress[7];
        streetname = splitaddress[8]+" "+splitaddress[9];

        if(state.equals("JH")){

            state="Johor";
        }

        if(state.equals("PK")){

            state="Perak";
        }

        if(state.equals("MK")){

            state="Melaka";
        }

        if(state.equals("KN")){

            state="Kelantan";
        }
        if(state.equals("PH")){

            state="Pahang";
        }
        if(state.equals("KD")){

            state="Kedah";
        }

        if(state.equals("PP")){

            state="Pulau Pinang";
        }

        if(state.equals("SW")){

            state="Sarawak";
        }

        if(state.equals("WP")){

            state="";
        }



        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Main.this);

        alert = alertDialog.create();

        alert.setTitle("Search Address");


        LayoutInflater inflater = getLayoutInflater();

        // inflate the custom popup layout
        final View convertView = inflater.inflate(R.layout.addressedit, null);
        // find the ListView in the popup layout
        final EditText addresset = (EditText) convertView.findViewById(R.id.addresset);
        final TextView addresstv = (TextView) convertView.findViewById(R.id.tvaddress);


        addresstv.setText(address);
        addresset.setText(streetname+" "+streetno+" "+suburb+" "+city+" "+poskod+" "+state+" Malaysia");


        //put autocomplete fragment

        String newaddress = streetname+" "+streetno+" "+suburb+" "+city+" "+poskod+" "+state+" Malaysia";


        newaddress = newaddress.replace("NIL","").trim();

            autocompleteFragment = (PlaceAutocompleteFragment)
                    getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment2);

            autocompleteFragment.setText(newaddress);


            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                    .build();
            autocompleteFragment.setFilter(typeFilter);

            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {


                @Override
                public void onPlaceSelected(Place place) {
                    // TODO: Get info about the selected place.
                    Log.i(TAG, "Place: " + place.getName());//get place details here
                    LatLng latlng = place.getLatLng();

                    String lat = String.valueOf(latlng.latitude);
                    String lng = String.valueOf(latlng.longitude);

                    String strcoordinate = "customer,"+lat+","+lng;

                    showmap(strcoordinate+","+ttno);


                }

                @Override
                public void onError(Status status) {
                    // TODO: Handle the error.
                    Log.i(TAG, "An error occurred: " + status);
                }



            });




        alert.setView(convertView);

        alert.show();




        final Button btnsearch = (Button)convertView.findViewById(R.id.btngeocoding);
        final Button btncancel = (Button)convertView.findViewById(R.id.cancelsearch);




        btnsearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click



                String newaddress = String.valueOf(addresset.getText());


                newaddress1 = newaddress.replace("NIL","").trim();

                final Context context = getApplicationContext();

                getlatlng = new AsyncTask<Void, Void, JSONObject>() {
                    @Override
                    protected JSONObject doInBackground(Void... params) {

                    JSONObject result = getLocationInfo(newaddress1);
                    return result;

                    }

                    @Override
                    protected void onCancelled() {
                        Toast.makeText(getApplicationContext(), "No network ",
                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    protected void onPostExecute(JSONObject result) {
                        getlatlng = null;

//                String latstr = String.valueOf(result.latitude);
//                String lngstr =String.valueOf(result.longitude);

//                        if(result != null) {
//
//                            String[] splitresult = result.split(",");
//
//                            latstr2 = splitresult[0];
//                            lngstr2 = splitresult[1];
//                            String addresssplit = splitresult[2];
//
//
//                            Toast.makeText(getApplicationContext(),result,
//                                    Toast.LENGTH_LONG).show();
//
//                            Log.e("LATLNG", "Lat:" + latstr2 + "Lng:" + lngstr2+","+ttno);
//
//

//                        }
                        if(result != null) {

                            JSONObject location;
                            String location_string;
                            try {
                                location = result.getJSONArray("results").getJSONObject(0);
                                location_string = location.getString("formatted_address");
                                JSONObject geometry = location.getJSONObject("geometry");
                                JSONObject locationlatlng = geometry.getJSONObject("location");

                                String lat = locationlatlng.getString("lat");
                                String lng = locationlatlng.getString("lng");
                                String strcoordinate = "customer,"+lat+","+lng;

                                Log.d("test", "formattted address:" + location_string);

                                Toast.makeText(getApplicationContext(), "Location Found "+strcoordinate,
                                        Toast.LENGTH_LONG).show();

                                mSocket.connect();
                                mSocket.emit("send:coords", strcoordinate+","+ttno);
                                mSocket.emit("chat message", strcoordinate +","+ttno);
                                showmap(strcoordinate+","+ttno);

                            } catch (JSONException e1) {
                                e1.printStackTrace();

                                String resultstr = String.valueOf(result);

                                Toast.makeText(getApplicationContext(), "Not Found "+resultstr ,
                                        Toast.LENGTH_LONG).show();

                            }


                        }

                        if(result==null){

                            Toast.makeText(getApplicationContext(), "Geocoding return null result" ,
                                    Toast.LENGTH_LONG).show();
                        }




                    }

                };


                getlatlng.execute();
                alert.hide();






            }
        });


        btncancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                alert.hide();

            }
        });




    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mapsmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.lvmaps:

                Intent i = new Intent(getApplicationContext(),livemap.class);
                startActivity(i);

                return true;

            case R.id.address:

                if(alert!=null) {
                    alert.show();
                }else{

                    showeditaddress(address);
                }
                return true;



            default:
                return super.onOptionsItemSelected(item);
        }


    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.Speak)
        {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ms-MY");
            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,"com.androidbegin.remoteandroid");

            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
            sr.startListening(intent);
            Log.i("111111","11111111");
        }

    }

    public JSONObject getLocationInfo(String address) {

        HttpURLConnection conn = null;
        BufferedReader reader = null;


        URL url = null;
        try {

            String query = URLEncoder.encode(address, "utf-8");


            url = new URL("http://maps.google.com/maps/api/geocode/json?address="+query+"&sensor=true");
            conn = (HttpURLConnection) url.openConnection();
            Log.e("TEST_MAPS", "req line:  http://maps.google.com/maps/api/geocode/json?address="+query+"&sensor=true");


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
//
//
//            JSONArray parentArray2= parentObject.getJSONArray("results");
//
//
//
//            JSONObject finalObject = parentArray2.getJSONObject(0);
//            JSONObject geometry = finalObject.getJSONObject("geometry");
//            JSONObject location = geometry.getJSONObject("location");
//
//            String lat = location.getString("lat");
//            String lng = location.getString("lng");
//            String result = lat+","+lng;



            return parentObject;


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
         catch (JSONException e) {
            e.printStackTrace();
        }


        return null;

}

    private void showmap(String datalist){

        if(livemap.getInstance()== null || livemap.getInstance().equals("")) {



            Intent in = new Intent(this, livemap.class);
            in.putExtra("datalist",datalist);


            startActivity(in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

        }

        if(livemap.getInstance()!=null){

            //livemap.getInstance().addmesage(msg);
            // ShowToastInIntentService(datalist);

            Intent i = new Intent(this, livemap.class);
            i.setAction(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            startActivity(i);

            livemap.getInstance().receivercoords(datalist);

        }


    }



}

package my.com.tm.moapps.mitosolt;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;


public class MyActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {


    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    Button Reg, btnAppShare, btnload;
    String regId;
    EditText phone, name, building;
    String phoneNum, uuid;
    String nameuser, buildingstr;
    String item;
    public static MyActivity myactivitymain;
    int position2;
    phoneadaptor subcribe2;
    ArrayList<phonelistmodel> comment2;
    public static String USERNAME1 = "";
    public static String UUIDLATLNG="";
    public static String token="";
    TextView resulttv;


    String cabinetidstrnameimage = "";
    static final int CAM_REQUEST = 1;
    Button btntagwithimage = null;
    ImageView imagecaptured = null;
    String elementstr = "Cabinet";
    Spinner elementtype;
    String cabinetidstr;
    MarkerOptions markercabinet;
    Marker marker;
    AlertDialog alert;
    boolean not_first_time_showing_info_window;
    boolean clearmap = true;


    //for googlemapview

    private GoogleMap mMap;

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;

    //free drawing maps
    Projection projection;
    Boolean Is_MAP_Moveable = false;
    Double latitude;
    Double longitude;
    Polyline polygon;
    Polyline poliline;
    PolylineOptions rectOptions;
    ArrayList<LatLng> val = new ArrayList<LatLng>();
    Float distance = (float) 0.0;;
    Object val2 = new ArrayList<String>();




    GoogleCloudMessaging gcm;
    public static final String REG_ID = "regId";
    Context context;
    static final String TAG = "Register Activity";
    ShareExternalServer appUtil, appUtil2;
    AsyncTask<Void, Void, String> shareRegidTask, updatelatlng;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        //for android 6.0

        ActivityCompat.requestPermissions(MyActivity.this,
                new String[]{Manifest.permission.READ_PHONE_STATE},
                1);

        //for android 5.0


        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        uuid = telephonyManager.getDeviceId();






        myactivitymain = this;


        resulttv = (TextView) findViewById(R.id.resultTv);

        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Group, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                item = parent.getItemAtPosition(pos).toString();

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });





        btnAppShare = (Button) findViewById(R.id.btnAppShare);
        btnAppShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                actionregister();

            }
        });


        btnload = (Button) findViewById(R.id.buttonLoad);

        btnload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(getApplicationContext(),
                        summary.class);

                startActivity(k);
            }
        });

        final Button togglemap = (Button) findViewById(R.id.toggle);
        togglemap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideshowmap();

                if(clearmap == false){

                    btnload = (Button) findViewById(R.id.buttonLoad);
                    btnload.setText("Enable Draw Maps");

                    btnload.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            if (Is_MAP_Moveable != true && val.size()<=0) {
                                Is_MAP_Moveable = true;
                                Button btnsave = (Button)findViewById(R.id.buttonLoad);
                                btnsave.setText("Disable Maps Sketch");

                            }
                            else if(Is_MAP_Moveable != true && val.size()>0){


                                savepolyline(val);



                            } else if(Is_MAP_Moveable == true ){
                                Is_MAP_Moveable = false;

                                Button btnsave = (Button)findViewById(R.id.buttonLoad);
                                btnsave.setText("Save Sketch");
                            }
                        }
                    });


                    Button btnAppSketch = (Button) findViewById(R.id.btnAppShare);
                    btnAppSketch.setText("Reload Sketch");

                    btnAppShare.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            loadfirebase();
                        }
                    });

                }

                else // if toggle map not selected
                {


                    //change to button reload the maps
                    btnAppShare = (Button)findViewById(R.id.btnAppShare);
                    btnAppShare.setText("Register To Server");

                    btnAppShare.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            actionregister();

                        }
                    });



                    btnload = (Button) findViewById(R.id.buttonLoad);
                    btnload.setText("Load TT");

                    btnload.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent k = new Intent(getApplicationContext(),
                                    summary.class);


                            startActivity(k);
                        }
                    });
                }

            }
        });


        //for googlemap api view generate map on fragment id map and allow the location

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //start update location every 5 minutes

       // startService(new Intent(MyActivity.this, locationupdateservice.class));
       // to get userid for given uuid of the phone from server

        getuser userobject = new getuser();
        userobject.execute(Config.APP_SERVER_URL6,uuid);

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        Toast.makeText(getApplicationContext(), "Refreshed token: " + refreshedToken,
                Toast.LENGTH_LONG).show();

        actionregister();
    }




    public static MyActivity getInstance() {

        return myactivitymain;
    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub


        super.onDestroy();


    }

    //function to register building id
    public void actionregister(){
        phone = (EditText) findViewById(R.id.phoneNumber);
        phoneNum = "OLTUSER";

        name = (EditText) findViewById(R.id.nameTv);
        nameuser = "OLTUSER";

        building = (EditText) findViewById(R.id.buildingidTv);
        buildingstr = "OLTSITE";

        final String refreshedToken = FirebaseInstanceId.getInstance().getToken();


        if (TextUtils.isEmpty(refreshedToken)) {
            Toast.makeText(getApplicationContext(), "Refresh Token is empty!",
                    Toast.LENGTH_LONG).show();
        } else if (phoneNum.matches("")) {
            Toast.makeText(getApplicationContext(), "Phone is empty!",
                    Toast.LENGTH_LONG).show();
        } else if (nameuser.matches("")) {
            Toast.makeText(getApplicationContext(), "Name is empty!",
                    Toast.LENGTH_LONG).show();

        } else if (buildingstr.matches("")) {
            Toast.makeText(getApplicationContext(), "Building ID is empty!",
                    Toast.LENGTH_LONG).show();

        } else {



            appUtil = new ShareExternalServer();

            final Context context = getApplicationContext();
            shareRegidTask = new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    String result = appUtil.shareRegIdWithAppServer(context, refreshedToken, phoneNum, nameuser, item, buildingstr);
                    return result;
                }

                @Override
                protected void onPostExecute(String result) {
                    shareRegidTask = null;
                    Toast.makeText(getApplicationContext(), result,
                            Toast.LENGTH_LONG).show();

                    resulttv.setText(result);
                }

            };
            shareRegidTask.execute(null, null, null);


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.myactivitymenu, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.aging:
                Intent i = new Intent(getApplicationContext(),
                        aging.class);


                startActivity(i);
                return true;

            case R.id.registration:

                phonelistasynctask subcribe = new phonelistasynctask(getApplicationContext());
                subcribe.execute(Config.APP_SERVER_URL13, uuid);


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

            case R.id.Adslportstate:
                Intent n = new Intent(getApplicationContext(),
                        viewadslportsummary.class);


                startActivity(n);
                return true;

            case R.id.agingtt:

                phonelocation markphone = new phonelocation(getApplicationContext());
                markphone.execute(Config.APP_SERVER_URL17);
                return true;

            case R.id.alltt:
                Intent l = new Intent(getApplicationContext(),
                        chatacitivity.class).addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);


                startActivity(l);
                return true;

            case R.id.livemap:
                Intent m = new Intent(getApplicationContext(),
                        livemap.class).addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);


                startActivity(m);
                return true;

            case R.id.hideshowmap:
               togglemaptype();
                return true;

            case R.id.SignOut:
                FirebaseAuth.getInstance().signOut();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }




    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {


            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(final Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);


        //update server on latlng position
        appUtil2 = new ShareExternalServer();
        final String lat = String.valueOf(location.getLatitude());
        final String lng = String.valueOf(location.getLongitude());

        final Context context = getApplicationContext();
        updatelatlng = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String result = appUtil2.updateltglng(context, lat, lng);
                return result;
            }

            @Override
            protected void onCancelled() {
                Toast.makeText(getApplicationContext(), "No network ",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            protected void onPostExecute(String result) {
                updatelatlng = null;
//                Toast.makeText(getApplicationContext(), result,
//                        Toast.LENGTH_LONG).show();




            }

        };
        updatelatlng.execute(null, null, null);

        //move map camera
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));




        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }


    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        //put marker of other mobile on the map when map is ready

        phonelocation markphone = new phonelocation(getApplicationContext());
        markphone.execute(Config.APP_SERVER_URL17);


        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {



            public void onMapLongClick(LatLng latLng) {

                opentagcabinet(latLng);


            }

        });



        FrameLayout fram_map = (FrameLayout) findViewById(R.id.fram_map);


        fram_map.setBackgroundColor(20);



        fram_map.setOnTouchListener(new View.OnTouchListener() {     @Override
        public boolean onTouch(View v, MotionEvent event) {

            ArrayList<LatLng> val2 = new ArrayList<>();
            if (Is_MAP_Moveable == true) {
                float x = event.getX();
                float y = event.getY();

                int x_co = Math.round(x);
                int y_co = Math.round(y);

                projection = mMap.getProjection();
                Point x_y_points = new Point(x_co, y_co);

                LatLng latLng = mMap.getProjection().fromScreenLocation(x_y_points);
                latitude = latLng.latitude;

                longitude = latLng.longitude;




                int eventaction = event.getAction();
                switch (eventaction) {
                    case MotionEvent.ACTION_DOWN:
                        // finger touches the screen
                        val.add(new LatLng(latitude, longitude));



                        //  Log.e("draw", String.valueOf(latLng));

                    case MotionEvent.ACTION_MOVE:
                        // finger moves on the screen
                        val.add(new LatLng(latitude, longitude));
                        //Log.e("draw", String.valueOf(latLng));

                      case MotionEvent.ACTION_UP:
//                         finger leaves the scree
//
                       Draw_Map(val);

                        break;
                }

            //    Draw_Map(val2);

                return true;

            } else {
                return false;
            }
        }
        });

     mMap.setInfoWindowAdapter(new infowindowsadaptor());



        loadfirebase();

    }

    //permission for phone to use location

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }


    public void Draw_Map(ArrayList<LatLng> vallat) {



        rectOptions = new PolylineOptions();
        rectOptions.addAll(vallat);
       rectOptions.color(Color.BLUE);
//        rectOptions.strokeWidth(7);
//        rectOptions.fillColor(Color.CYAN);
        polygon = mMap.addPolyline(rectOptions);












    }

    public void Draw_map2(ArrayList<LatLng> vallat, String polylinename){

        PolylineOptions polyoption = new PolylineOptions();
        polyoption.addAll(vallat);
        polyoption.color(Color.BLUE);

        poliline = mMap.addPolyline(polyoption);


        //add marker to tag the drawing name
        MarkerOptions markerdrawing = new MarkerOptions();
        markerdrawing.title(polylinename);
        markerdrawing.position(vallat.get(vallat.size()-1));
        markerdrawing.icon(BitmapDescriptorFactory.fromResource(R.drawable.flagpole));

        mMap.addMarker(markerdrawing);

       vallat.clear();


    }

//function to hide or show  form

    public void hideshowmap(){

        if(clearmap == true ) {

            EditText phoneet = (EditText) findViewById(R.id.phoneNumber);
            phoneet.setVisibility(View.INVISIBLE);

            EditText nameet = (EditText) findViewById(R.id.nameTv);
            nameet.setVisibility(View.INVISIBLE);

            EditText buildinget = (EditText) findViewById(R.id.buildingidTv);
            buildinget.setVisibility(View.INVISIBLE);


            Spinner andselect = (Spinner)findViewById(R.id.spinner);
            andselect.setVisibility(View.INVISIBLE);

            clearmap = !clearmap;
        }
        else{
            EditText phoneet = (EditText) findViewById(R.id.phoneNumber);
            phoneet.setVisibility(View.VISIBLE);

            EditText nameet = (EditText) findViewById(R.id.nameTv);
            nameet.setVisibility(View.VISIBLE);

            EditText buildinget = (EditText) findViewById(R.id.buildingidTv);
            buildinget.setVisibility(View.VISIBLE);


            Spinner andselect = (Spinner)findViewById(R.id.spinner);
            andselect.setVisibility(View.VISIBLE);

            clearmap = !clearmap;
        }

    }


    //function to togglemaps type
    Boolean type2 = true;

    public void togglemaptype(){

        if(type2 == true ) {

           mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            type2 = !type2;
        }
        else{

            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            type2 = !type2;

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
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


    //function to put marker on maps

    public void putmarkermap(final ArrayList<phonelistmodel> phoneloc) {




        for (int j = 0; j < phoneloc.size(); j++) {

            double latitude = Double.parseDouble(phoneloc.get(j).getLatitude());
            double longitude = Double.parseDouble(phoneloc.get(j).getLongitude());
            LatLng latLng = new LatLng(latitude, longitude);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);


            String imei = phoneloc.get(j).getUuid();

            if(imei.equals(uuid)){

                UUIDLATLNG = uuid+","+phoneloc.get(j).getLatitude()+","+phoneloc.get(j).getLongitude();

//                Toast.makeText(getApplicationContext(), UUIDLATLNG,
//                        Toast.LENGTH_LONG).show();
            }




            //to hide master phone name in map
            if(phoneloc.get(j).getNameuser().equals(USERNAME1)){
            markerOptions.title("Master Phone");}
            else {

                    markerOptions.title(phoneloc.get(j).getNameuser());
            }

            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            mCurrLocationMarker = mMap.addMarker(markerOptions);
            //mCurrLocationMarker.showInfoWindow();

            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(8));

        }

    }

 //function to save the polyline
 public void savepolyline( ArrayList<LatLng> arraylat) {

     ArrayList<LatLng> arraylatlng = new ArrayList<>();

     arraylatlng = arraylat;



     final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

     alert = alertDialog.create();

     alert.setTitle("Save Sketch");


     LayoutInflater inflater = getLayoutInflater();

     // inflate the custom popup layout
     final View convertView = inflater.inflate(R.layout.polylinesave_layout, null);



     alert.setView(convertView);

     alert.show();





      Button btnsavesktch = (Button)convertView.findViewById(R.id.btnsavesketch);



     final ArrayList<LatLng> finalArraylatlng = arraylatlng;
     final ArrayList<LatLng> finalArraylatlng1 = arraylatlng;
     btnsavesktch.setOnClickListener(new View.OnClickListener() {
         public void onClick(View v) {


             for(Integer i =0;i < finalArraylatlng.size()-1;){

                 Location L1 = new Location("Start");
                 Location L2 = new Location("End");

                 L1.setLatitude(finalArraylatlng.get(i).latitude);
                 L1.setLongitude(finalArraylatlng.get(i).longitude);

                 L2.setLatitude(finalArraylatlng.get(i+1).latitude);
                 L2.setLongitude(finalArraylatlng.get(i+1).longitude);



                 distance = distance + L1.distanceTo(L2);
                 i++;



             }
             Log.e("Distance", String.valueOf(distance));


             // Perform action on click
             EditText sketchname = (EditText)convertView.findViewById(R.id.sketchname);

             String sketchnamestr = sketchname.getText().toString();



             if(sketchnamestr.equals("") || sketchname.equals(null)){

                 alert.hide();
             }
             else{

                 //save to realtime database
                 FirebaseDatabase database = FirebaseDatabase.getInstance();
                 DatabaseReference myRef = database.getReference();

                 String distancestr = distance.toString().replace(".","_");

                 FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                 if (user != null) {
                     // User is signed in

                     //this will make the current sketch only on....new sketch will be added

                     myRef.child("currentsketch").removeValue();

                     for (int i = 0; i < finalArraylatlng.size(); i++) {
                         //System.out.println(val.get(i));
                         myRef.child("currentsketch/"+user.getUid()).child(sketchnamestr+":Distance"+distancestr+"m").child("Lat").child(String.valueOf(i)).setValue(finalArraylatlng.get(i).latitude+","+finalArraylatlng.get(i).longitude);
                         //  myRef.child("sketch/"+user.getUid()).child(sketchnamestr).child("Lng").child(String.valueOf(i)).setValue(finalArraylatlng.get(i).longitude);

                     }

                     //this save  will be used to loaded initial sketch...will load all sketches from firebase

                     for (int i = 0; i < finalArraylatlng.size(); i++) {
                         //System.out.println(val.get(i));
                         myRef.child("sketch/"+user.getUid()).child(sketchnamestr+":Distance:"+distancestr+"m").child("Lat").child(String.valueOf(i)).setValue(finalArraylatlng.get(i).latitude+","+finalArraylatlng.get(i).longitude);
                       //  myRef.child("sketch/"+user.getUid()).child(sketchnamestr).child("Lng").child(String.valueOf(i)).setValue(finalArraylatlng.get(i).longitude);

                     }

                  finalArraylatlng1.clear();
                     distance = (float)0.0;

                 } else {
                     // No user is signed in
                 }
                 alert.dismiss();



             }

         }
     });

     Button btnsave = (Button)findViewById(R.id.buttonLoad);
     btnsave.setText("Enable Draw Sketch");

     Button btncancel = (Button)convertView.findViewById(R.id.cancelsavesketch);

     btncancel.setOnClickListener(new View.OnClickListener() {
         public void onClick(View v) {

             polygon.remove();
         }


     });


 }

 public void loadfirebase(){



   //first initial load

     FirebaseDatabase databasefirebase2 = FirebaseDatabase.getInstance();
     final DatabaseReference myRefdatabase2 = databasefirebase2.getReference("sketch");


     myRefdatabase2.addListenerForSingleValueEvent(new ValueEventListener() {
         @Override
         public void onDataChange(DataSnapshot dataSnapshot) {


             ArrayList<String> listpolylat2 = new ArrayList<>();
             String polylinename = "";

             LatLng lastcoordinates = new LatLng(0, 0);

             for (DataSnapshot child: dataSnapshot.getChildren()) {


                 for (DataSnapshot child2 : child.getChildren()) {

                     polylinename = child2.getKey();


                     listpolylat2 = (ArrayList<String>) child2.child("Lat").getValue();


                     ArrayList<LatLng> vallatlng = new ArrayList<>();

                     for (int i = 0; i < listpolylat2.size(); i++) {

                         String lat = String.valueOf(listpolylat2.get(i));
                         String[] separated = lat.split(",");


                         Double latdbl = Double.valueOf(separated[0]);
                         Double lngdbl = Double.valueOf(separated[1]);

                         LatLng coords = new LatLng(latdbl, lngdbl);
                        lastcoordinates = coords;

                         vallatlng.add(coords);
                     }

                     Draw_map2(vallatlng,polylinename);



                 }

             }


         }

         @Override
         public void onCancelled(DatabaseError databaseError) {
             // Getting Post failed, log a message
             Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
             // ...
         }
     });

//on new added sketch

//     FirebaseDatabase databasefirebasecurrent = FirebaseDatabase.getInstance();
//     final DatabaseReference myRefdatabasecurrent = databasefirebasecurrent.getReference("currentsketch");
//
//
//     myRefdatabasecurrent.addValueEventListener(new ValueEventListener() {
//         @Override
//         public void onDataChange(DataSnapshot dataSnapshot) {
//
//
//             ArrayList<String> listpolylat2 = new ArrayList<>();
//             String polylinename = "";
//
//             LatLng lastcoordinates = new LatLng(0, 0);
//
//             for (DataSnapshot child: dataSnapshot.getChildren()) {
//
//
//                 for (DataSnapshot child2 : child.getChildren()) {
//
//                     polylinename = child2.getKey();
//
//
//                     listpolylat2 = (ArrayList<String>) child2.child("Lat").getValue();
//
//
//                     ArrayList<LatLng> vallatlng = new ArrayList<>();
//
//                     for (int i = 0; i < listpolylat2.size(); i++) {
//
//                         String lat = String.valueOf(listpolylat2.get(i));
//                         String[] separated = lat.split(",");
//
//
//                         Double latdbl = Double.valueOf(separated[0]);
//                         Double lngdbl = Double.valueOf(separated[1]);
//
//                         LatLng coords = new LatLng(latdbl, lngdbl);
//                         lastcoordinates = coords;
//
//                         vallatlng.add(coords);
//                     }
//
//                     Draw_map2(vallatlng, polylinename);
//
//                 }
//
//
//
//
//
//             }
//
//
//
//
//         }
//
//         @Override
//         public void onCancelled(DatabaseError databaseError) {
//             // Getting Post failed, log a message
//             Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
//             // ...
//         }
//     });



  //initial load once when loading

     FirebaseDatabase databasefirebaseinitial = FirebaseDatabase.getInstance();
     final DatabaseReference myRefdatabaseinitial = databasefirebaseinitial.getReference("photomarker");

     myRefdatabaseinitial.addListenerForSingleValueEvent(new ValueEventListener() {
         @Override
         public void onDataChange(DataSnapshot dataSnapshot) {

             Double latphoto = null;
             Double lngphoto = null;


                 String markername =  "";

                 for (DataSnapshot child: dataSnapshot.getChildren()) {
                       markername = child.getKey();

                       for (DataSnapshot child2 : child.getChildren()) {

                         if (child2.getKey().toString().equals("lat")) {
//                     Log.i("MARKERPHOTOLat", String.valueOf(markerlist));
//                     Log.i("MARKERPHOTOLng", String.valueOf(dataSnapshot.child("lng").getValue()));
//                     Log.i("MARKERPHOTOname", markername);
                             latphoto = (Double) child2.getValue();


                         }
                         if (child2.getKey().toString().equals("lng")) {
//                     Log.i("MARKERPHOTOLat", String.valueOf(markerlist));
//                     Log.i("MARKERPHOTOLng", String.valueOf(dataSnapshot.child("lng").getValue()));
//                     Log.i("MARKERPHOTOname", markername);
                             lngphoto = (Double) child2.getValue();


                         }


                     }


                     if (!latphoto.equals(null)) {

                         LatLng coords = new LatLng(latphoto, lngphoto);


                         MarkerOptions markerphotooption = new MarkerOptions();
                         markerphotooption.position(coords);

                         if (markername.contains("Cabinet")) {
                             markerphotooption.title(markername);
                             markerphotooption.icon(BitmapDescriptorFactory.fromResource(R.drawable.cabinet));
                         }
                         if (markername.contains("Main Hole")) {
                             markerphotooption.title(markername);
                             markerphotooption.icon(BitmapDescriptorFactory.fromResource(R.drawable.mainholeicon));
                         }
                         if (markername.contains("DP")) {
                             markerphotooption.title(markername);
                             markerphotooption.icon(BitmapDescriptorFactory.fromResource(R.drawable.dppole));
                         }

                         Marker markernew;
                         markernew = mMap.addMarker(markerphotooption);

                     }
                 }
             // ...
         }

         @Override
         public void onCancelled(DatabaseError databaseError) {
             // Getting Post failed, log a message
             Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
             // ...
         }
     });

//listening for new photo...if added will put icon on map base on element type

     FirebaseDatabase databasefirebase3 = FirebaseDatabase.getInstance();
     final DatabaseReference myRefdatabase3 = databasefirebase3.getReference("currentphotomarker");


     myRefdatabase3.addChildEventListener(new ChildEventListener() {





         @Override
         public void onChildAdded(DataSnapshot dataSnapshot, String s) {

             Double latphoto = null;
             Double lngphoto = null;

             String markername =  dataSnapshot.getKey();


             for (DataSnapshot child: dataSnapshot.getChildren()) {



                 if(child.getKey().toString().equals("lat")){
//                     Log.i("MARKERPHOTOLat", String.valueOf(markerlist));
//                     Log.i("MARKERPHOTOLng", String.valueOf(dataSnapshot.child("lng").getValue()));
//                     Log.i("MARKERPHOTOname", markername);
                 latphoto = (Double) child.getValue();


                 }
                 if(child.getKey().toString().equals("lng")){
//                     Log.i("MARKERPHOTOLat", String.valueOf(markerlist));
//                     Log.i("MARKERPHOTOLng", String.valueOf(dataSnapshot.child("lng").getValue()));
//                     Log.i("MARKERPHOTOname", markername);
                     lngphoto = (Double) child.getValue();



                 }




             }

            if(latphoto != null && lngphoto != null) {

                LatLng coords = new LatLng(latphoto, lngphoto);


                MarkerOptions markerphotooption = new MarkerOptions();
                markerphotooption.position(coords);

                if (markername.contains("Cabinet")) {
                    markerphotooption.title(markername);
                    markerphotooption.icon(BitmapDescriptorFactory.fromResource(R.drawable.cabinet));
                }
                if (markername.contains("Main Hole")) {
                    markerphotooption.title(markername);
                    markerphotooption.icon(BitmapDescriptorFactory.fromResource(R.drawable.mainholeicon));
                }
                if (markername.contains("DP")) {
                    markerphotooption.title(markername);
                    markerphotooption.icon(BitmapDescriptorFactory.fromResource(R.drawable.dppole));
                }

                Marker markernew ;
                markernew = mMap.addMarker(markerphotooption);

            }

         }

         @Override
         public void onChildChanged(DataSnapshot dataSnapshot, String s) {



         }

         @Override
         public void onChildRemoved(DataSnapshot dataSnapshot) {

         }

         @Override
         public void onChildMoved(DataSnapshot dataSnapshot, String s) {

         }

         @Override
         public void onCancelled(DatabaseError databaseError) {

         }
     });

 }





// function popup when long click on maps

    public void opentagcabinet(final LatLng latlng){



        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alert = alertDialog.create();

        alert.setTitle("Tag Location");


        LayoutInflater inflater = getLayoutInflater();

        // inflate the custom popup layout
        final View convertView = inflater.inflate(R.layout.tagcabinet_layout, null);
        // find the ListView in the popup layout



        alert.setView(convertView);

        alert.show();


        final Button btntag = (Button)convertView.findViewById(R.id.btntagcabinetdpmainhole);
        final Button btncancel = (Button)convertView.findViewById(R.id.canceltagcabinetdpmainhole);
        final Button btncaptureimage = (Button)convertView.findViewById(R.id.capturebutton);
        final Button btncaptureimagegalerry = (Button)convertView.findViewById(R.id.selectgalerybutton);
        imagecaptured = (ImageView)convertView.findViewById(R.id.imagecaptureid);
        EditText cabinetidname = (EditText)convertView.findViewById(R.id.cabinetidtaglocation);
        cabinetidstr = String.valueOf(cabinetidname.getText());

        elementtype = (Spinner) convertView.findViewById(R.id.spinnerelementtype);
        btntagwithimage = (Button)convertView.findViewById(R.id.btntagcabinetdpmainhole);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.elementtype, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        elementtype.setAdapter(adapter);

        elementtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                elementstr = parent.getItemAtPosition(pos).toString();




            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });






        btncancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                if(marker != null){
                marker.remove();
                }
                alert.hide();

            }
        });

        btntag.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                EditText cabinetidtotag = (EditText)convertView.findViewById(R.id.cabinetidtaglocation);

                String cabinetstr = elementstr+cabinetidtotag.getText().toString();



                if(cabinetstr.equals("") || cabinetstr.equals(null)){
                    marker.remove();
                    alert.hide();
                }
                else{
                    marker.setTitle(cabinetstr);
                    alert.hide();


                }

            }
        });




        btncaptureimage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {



                EditText cabinetidtotag = (EditText)convertView.findViewById(R.id.cabinetidtaglocation);
                String cabinet_name = cabinetidtotag.getText().toString();
                cabinetidstrnameimage = elementstr+"_"+cabinet_name;





                if(cabinet_name.equals("")) {
//                    marker.remove();
                    alert.hide();

                    Toast.makeText(MyActivity.this, "Pls Insert Text ID", Toast.LENGTH_SHORT)
                            .show();

                }else
                {

                    markercabinet = new MarkerOptions();
                    markercabinet.position(latlng);
                    markercabinet.title(cabinetidstrnameimage);


                   // marker.setTitle(cabinetidstrnameimage);

                    if(cabinetidstrnameimage.contains("Cabinet")) {
                        markercabinet.icon(BitmapDescriptorFactory.fromResource(R.drawable.cabinet));
                    }

                    if(cabinetidstrnameimage.contains("Main Hole")) {
                        markercabinet.icon(BitmapDescriptorFactory.fromResource(R.drawable.mainholeicon));
                    }
                    if(cabinetidstrnameimage.contains("DP")) {
                        markercabinet.icon(BitmapDescriptorFactory.fromResource(R.drawable.dppole));
                    }



                    marker = mMap.addMarker(markercabinet);









                    // Perform action on click
                    Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


                    File file = getFile(cabinetidstrnameimage);
//                    Uri photoURI = FileProvider.getUriForFile(MyActivity.this,
                     //       "my.com.tm.moapps.remoteandroid.fileprovider",
                    //        file);

                    Uri apkURI = FileProvider.getUriForFile(
                            MyActivity.this,
                            MyActivity.this.getApplicationContext()
                                    .getPackageName() + ".provider", file);


                    camera_intent.putExtra(MediaStore.EXTRA_OUTPUT,

                            //photoURI
                          apkURI

                    );


                    camera_intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    startActivityForResult(camera_intent, 0);


                    //put marker


                }




            }
        });


        btncaptureimagegalerry.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText cabinetidtotag = (EditText)convertView.findViewById(R.id.cabinetidtaglocation);
                String cabinet_name = cabinetidtotag.getText().toString();
                cabinetidstrnameimage = elementstr+"_"+cabinet_name;




                if(cabinet_name.equals("")) {
                    //marker.remove();
                    alert.hide();

                    Toast.makeText(MyActivity.this, "Pls Insert Text ID", Toast.LENGTH_SHORT)
                            .show();

                }else
                {

                    markercabinet = new MarkerOptions();
                    markercabinet.position(latlng);
                    markercabinet.title(cabinetidstrnameimage);


                    // marker.setTitle(cabinetidstrnameimage);

                    if(cabinetidstrnameimage.contains("Cabinet")) {
                        markercabinet.icon(BitmapDescriptorFactory.fromResource(R.drawable.cabinet));
                    }

                    if(cabinetidstrnameimage.contains("Main Hole")) {
                        markercabinet.icon(BitmapDescriptorFactory.fromResource(R.drawable.mainholeicon));
                    }
                    if(cabinetidstrnameimage.contains("DP")) {
                        markercabinet.icon(BitmapDescriptorFactory.fromResource(R.drawable.dppole));
                    }



                    marker = mMap.addMarker(markercabinet);




                    // Perform action on click
                    Intent camera_intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    File file = getFile(cabinetidstrnameimage);

                    Uri apkURI = FileProvider.getUriForFile(
                            MyActivity.this,
                            MyActivity.this.getApplicationContext()
                                    .getPackageName() + ".provider", file);


                    camera_intent.putExtra(MediaStore.EXTRA_OUTPUT,

                            //photoURI
                            apkURI

                    );

                    startActivityForResult(camera_intent,1);




                    //put marker


                }




            }
        });



    }

    //create a file and folder for the image capture method

    private File getFile(String filename){


        File Folder = new File(Environment.getExternalStorageDirectory() +
                                File.separator +"camera_remote");

        if(!Folder.exists()){

            Folder.mkdir();
        }

        File image_file = new File(Folder,filename+".jpg");

        return image_file;


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

         if(requestCode == 0) {

             final String path = Environment.getExternalStorageDirectory() +
                     File.separator + "camera_remote/" + cabinetidstrnameimage + ".jpg";

             Bitmap bmp = BitmapFactory.decodeFile(path);
             Bitmap photo = Bitmap.createScaledBitmap(bmp, 300, 300, true);

             ByteArrayOutputStream bytes = new ByteArrayOutputStream();

             photo.compress(Bitmap.CompressFormat.JPEG, 40, bytes);

             File f = new File(Environment.getExternalStorageDirectory()
                     + File.separator + "camera_remote/" + cabinetidstrnameimage + ".jpg");
             try {
                 f.createNewFile();
             } catch (IOException e) {
                 e.printStackTrace();
             }
             FileOutputStream fo = null;
             try {
                 fo = new FileOutputStream(f);
             } catch (FileNotFoundException e) {
                 e.printStackTrace();
             }
             try {
                 fo.write(bytes.toByteArray());
             } catch (IOException e) {
                 e.printStackTrace();
             }
             try {
                 fo.close();
             } catch (IOException e) {
                 e.printStackTrace();
             }

             imagecaptured.setImageDrawable(Drawable.createFromPath(path));
         }

         if(requestCode == 1){
             if (data != null) {
                 Uri contentURI = data.getData();


                 final String path = Environment.getExternalStorageDirectory() +
                         File.separator + "camera_remote/" + cabinetidstrnameimage + ".jpg";

                 Bitmap bmp = null;
                 try {
                     bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                 } catch (IOException e) {
                     e.printStackTrace();
                     Log.e("Error image Galery", e.toString());
                 }
                 Bitmap photo = Bitmap.createScaledBitmap(bmp, 300, 300, true);

                 ByteArrayOutputStream bytes = new ByteArrayOutputStream();

                 photo.compress(Bitmap.CompressFormat.JPEG, 40, bytes);

                 File f = new File(Environment.getExternalStorageDirectory()
                         + File.separator + "camera_remote/" + cabinetidstrnameimage + ".jpg");
                 try {
                     f.createNewFile();
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
                 FileOutputStream fo = null;
                 try {
                     fo = new FileOutputStream(f);
                 } catch (FileNotFoundException e) {
                     e.printStackTrace();
                 }
                 try {
                     fo.write(bytes.toByteArray());
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
                 try {
                     fo.close();
                 } catch (IOException e) {
                     e.printStackTrace();
                 }

                 imagecaptured.setImageDrawable(Drawable.createFromPath(path));
             }

         }




        btntagwithimage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {



                // Create a storage reference from our app
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();
                File file = getFile(cabinetidstrnameimage);
//                    Uri photoURI = FileProvider.getUriForFile(MyActivity.this,
                //       "my.com.tm.moapps.remoteandroid.fileprovider",
                //        file);

                Uri apkURI = FileProvider.getUriForFile(
                        MyActivity.this,
                        MyActivity.this.getApplicationContext()
                                .getPackageName() + ".provider", file);
                StorageReference riversRef = storageRef.child("remote_camera"+ File.separator+apkURI.getLastPathSegment());
                UploadTask uploadTask = riversRef.putFile(apkURI);



                //create database info for marker

                FirebaseDatabase databasefirebase = FirebaseDatabase.getInstance();
                final DatabaseReference myRef = databasefirebase.getReference();
                myRef.child("photomarker/"+cabinetidstrnameimage).child("lat").setValue(markercabinet.getPosition().latitude);
                myRef.child("photomarker/"+cabinetidstrnameimage).child("lng").setValue(markercabinet.getPosition().longitude);


                //update current photo...only one entry
                myRef.child("currentphotomarker").removeValue();


                myRef.child("currentphotomarker/"+cabinetidstrnameimage).child("lat").setValue(markercabinet.getPosition().latitude);
                myRef.child("currentphotomarker/"+cabinetidstrnameimage).child("lng").setValue(markercabinet.getPosition().longitude);



                // Register observers to listen for when the download is done or if it fails
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                        marker.remove();

                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();


                        mMap.setInfoWindowAdapter(new infowindowsadaptor());

                        //marker.showInfoWindow();


                        alert.dismiss();
                    }
                });




            }
        });






    }



    public void onShowPopup3(View v, final ArrayList<phonelistmodel> comment) {


        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MyActivity.this);

        final AlertDialog alert = alertDialog.create();


        alert.setTitle("Subcribe");


        LayoutInflater inflater = getLayoutInflater();

        // inflate the custom popup layout
        final View convertView = inflater.inflate(R.layout.custom, null);
        // find the ListView in the popup layout
        final ListView listView = (ListView) convertView.findViewById(R.id.listView1);

        final phoneadaptor subcribe = new phoneadaptor(getApplicationContext(), R.layout.phonerow, comment);

        subcribe2 = subcribe;

        listView.setAdapter(subcribe);

        // setSimpleList(listView, comment);

        alert.setView(convertView);

        alert.show();


//        listView.setOnItemClickListener(
//
//
//
//                new AdapterView.OnItemClickListener() {
//
//
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(parent.getContext());
//                        builder.setMessage("Sent Reminder Notification?").setPositiveButton("Yes", dialogClickListener)
//                                .setNegativeButton("No", dialogClickListener).show();
//
//                        position2=position;
//
//
//
//
//
//
//
//
//
//                    }
//
//                }
//
//
//        );
//
//        TextView delete_link = (TextView)convertView.findViewById(R.id.deletebuilding);
//
//        delete_link.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int position = (Integer)v.getTag();
//                subcribe.remove(comment.get(position));
//            }
//
//        });


        }

//    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
//        @Override
//        public void onClick(DialogInterface dialog, int which) {
//            switch (which){
//                case DialogInterface.BUTTON_POSITIVE:
//                    subcribe2.remove(comment2.get(position2));
//
//                    break;
//
//                case DialogInterface.BUTTON_NEGATIVE:
//                    //Nothing
//                    break;
//            }
//        }
//    };
  //permission request to get location google api


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // Sign in logic here.

                    Intent i = new Intent(getApplicationContext(), login.class);
                    startActivity(i);
                }
            }
        });


    }

}
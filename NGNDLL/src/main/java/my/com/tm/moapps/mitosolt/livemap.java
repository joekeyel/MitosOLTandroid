package my.com.tm.moapps.mitosolt;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.common.server.converter.StringToIntConverter;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.socket.client.IO;
import io.socket.client.Socket;



public class livemap extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    List<Marker> markers;
    Map<String,Marker> eventMarkerMap = new HashMap<>();
    Map<String,String> eventname = new HashMap<>();
    String UUID;
    AlertDialog alert;


    private static final String TAG = "" ;
    private PowerManager.WakeLock wl;

    //for file storage and tagging location
    String cabinetidstrnameimage = "";
    static final int CAM_REQUEST = 1;
    Button btntagwithimage = null;
    ImageView imagecaptured = null;
    String elementstr = "Cabinet";
    Spinner elementtype;
    String cabinetidstr;
    MarkerOptions markercabinet;
    Marker marker;

    boolean not_first_time_showing_info_window;

    public static livemap livemapactivity;

    private Socket mSocket;

    {
        try {
            mSocket = IO.socket("http://58.27.84.188/");
        } catch (URISyntaxException e) {}
    }


    // realtime firebase databse

    FirebaseUser userfb = FirebaseAuth.getInstance().getCurrentUser();

    FirebaseDatabase databasefirebase = FirebaseDatabase.getInstance();
    DatabaseReference myRefdatabase = databasefirebase.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        UUID = telephonyManager.getDeviceId();

      //  moveTaskToBack(true);
        setContentView(R.layout.livemap);


        PowerManager pm = (PowerManager)getApplicationContext().getSystemService(Context.POWER_SERVICE);
        this.wl = pm.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE,TAG);
        wl.acquire();



        livemapactivity = this;

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.maplive);
        mapFragment.getMapAsync(this);





    }



    public static livemap getInstance() {

        return livemapactivity;
    }


    public void ShowToastInIntentService(final String sText)
    {  final Context MyContext = this;
        new Handler(Looper.getMainLooper()).post(new Runnable()
        {  @Override public void run()
        {  Toast toast1 = Toast.makeText(MyContext, sText, Toast.LENGTH_LONG);
            toast1.show();
        }
        });
    };

    public void putmarkermap(final ArrayList<phonelistmodel> phoneloc) {




        for (int j = 0; j < phoneloc.size(); j++) {

            double latitude = Double.parseDouble(phoneloc.get(j).getLatitude());
            double longitude = Double.parseDouble(phoneloc.get(j).getLongitude());
            LatLng latLng = new LatLng(latitude, longitude);

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(phoneloc.get(j).getNameuser());
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));


            String UUID = phoneloc.get(j).getUuid();
            String nameuser = phoneloc.get(j).getNameuser();


            mCurrLocationMarker = mMap.addMarker(markerOptions);
            //mCurrLocationMarker.showInfoWindow();



            //bind marker with uuid phone using hash map


            eventMarkerMap.put(UUID,mCurrLocationMarker);
            eventname.put(UUID,nameuser);




            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(8));

        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {


            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

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

        //move map camera
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));


        //save to realtime database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            myRef.child("currentuser").setValue(user.getUid());
            myRef.child(user.getUid()+"UUI").setValue(UUID);
            myRef.child(user.getUid()+"Lat").setValue(location.getLatitude());
            myRef.child(user.getUid()+"Lng").setValue(location.getLongitude());
            



        } else {
            // No user is signed in
        }






    }

    String USER;

    @Override
    protected void onStart() {
        super.onStart();

        // realtime firebase databse


        FirebaseDatabase databasefirebase = FirebaseDatabase.getInstance();
        DatabaseReference myRefdatabase = databasefirebase.getReference();

        myRefdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                    String username = String.valueOf(dataSnapshot.child("currentuser").getValue());

                    Log.i("USER", username);
                        String uuid = String.valueOf(dataSnapshot.child(username+"UUI").getValue());
                        String lat = String.valueOf(dataSnapshot.child(username+"Lat").getValue());
                        String lng = String.valueOf(dataSnapshot.child(username+"Lng").getValue());

                        //create marker for any active user
                        double latitude = Double.parseDouble(lat);
                        double longitude = Double.parseDouble(lng);

                        LatLng latLng = new LatLng(latitude, longitude);

                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);
                        markerOptions.title(username);
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.contractor_icon));

                        //check existing marker wether already on map where marker store on map
                        Marker marker = eventMarkerMap.get(uuid);
                        String name = eventname.get(uuid);

                        // if marker already exist
                        if (marker != null) {
                            animateMarker(latLng, marker, username);
                        }
                        //i f marker not exist
                        if (marker == null) {
                            mCurrLocationMarker = mMap.addMarker(markerOptions);

                        }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        if(getIntent().getStringExtra("datalist") == null) {
            //Do first time stuff here
        } else {
            String data = getIntent().getStringExtra("datalist");

            receivercoords(data);


        }




        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

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


        //when map is ready listen to socket io load:coords

//        mSocket.off();
//
//        mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
//
//            @Override
//            public void call(Object... args) {
//                Log.d("MainActivity: ", "socket connected");
//                //              mSocket.emit("chat message", "Connected:"+MyActivity.USERNAME1);
////                Toast.makeText(getApplicationContext(), "Service Started", Toast.LENGTH_LONG).show();
//                ShowToastInIntentService("Connected:"+MyActivity.USERNAME1);
//
//
//            }
//
//        }).on("load:coords", new Emitter.Listener() {
//            @Override
//            public void call(final Object... args) {
//
//                   if(args!=null){
//                    // I want this to run in the background
//                    String data = (String) args[0];
//                    String[] datalist = data.split(",");
//                    String UUID = datalist[0];
//
//                    Integer msg = datalist.length;
//
//
//                     if(!datalist[1].equals("locationoff") || !datalist[1].equals(null)) {
//
//                         double latitude = Double.parseDouble(datalist[1]);
//                         double longitude = Double.parseDouble(datalist[2]);
//                         LatLng latlng = new LatLng(latitude, longitude);
//
//                         Marker marker = eventMarkerMap.get(UUID);
//                         String name = eventname.get(UUID);
//
//
//                         Intent done = new Intent();
//                         done.setAction(MyAlarmReceiver2.ACTION);
//                         done.addCategory(Intent.CATEGORY_LAUNCHER);
//                         done.putExtra(PARAM_OUT_MSG, data);
//                         sendBroadcast(done);
//                         // movemarker(data);
//                         ShowToastInIntentService("Remote: Listening " + name);
//                         //onLocationChanged(latlng);
//
//                         if (UUID != "customer" && msg == 3) {
//
//                             animateMarker(latlng, marker, name);
//                         }
//                         if (UUID.equals("customer")) {
//
//                             tagcustomer(latlng, "TT");
//                         }
//
//                         if (msg > 3) {
//
//                             String chatmsg = "Speak:" + datalist[3];
//                             animateMarker2(latlng, marker, chatmsg);
//
//                         }
//                     }
//
//
//
//
//
//
//
//
//                }
//
//                }
//
//
//
//
//            })
//                .on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
//
//                    @Override
//                    public void call(Object... args) {
//                        ShowToastInIntentService("Remote: Connection Disconnect");
//                    }
//
//                }).on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
//
//            @Override
//            public void call(Object... args) {
//                ShowToastInIntentService("Remote: Connection Error");
//            }
//
//        }).on(Socket.EVENT_CONNECT_TIMEOUT, new Emitter.Listener() {
//
//            @Override
//            public void call(Object... args) {
//                ShowToastInIntentService("Remote: Time Out");
//            }
//
//        });
//
//
//        mSocket.connect();
//
//        if(MyActivity.USERNAME1!="") {
//
//           // mSocket.emit("send:coords",ShareExternalServer.DATA);
//        }

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {



            public void onMapLongClick(LatLng latLng) {

                opentagcabinet(latLng);
            }

        });






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



        markercabinet = new MarkerOptions();
        markercabinet.position(latlng);

        markercabinet.title(cabinetidstr);

        marker = mMap.addMarker(markercabinet);


        btncancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                marker.remove();
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

                 cabinetidstrnameimage = elementstr+"_"+cabinetidtotag.getText().toString();





                if(cabinetidstrnameimage.equals("")) {
                    marker.remove();
                    alert.hide();

                    Toast.makeText(livemap.this, "Pls Insert ID", Toast.LENGTH_SHORT)
                            .show();

                }else
                {

                    marker.setTitle(cabinetidstrnameimage);

                    // Perform action on click
                    Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    File file = getFile(cabinetidstrnameimage);
                    camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));

                    startActivityForResult(camera_intent, CAM_REQUEST);


                    //put marker


                }




            }
        });



    }

   //create a file and folder for the image capture method

    private File getFile(String filename){


        File Folder = new File("sdcard/camera_remote");

        if(!Folder.exists()){

            Folder.mkdir();
        }

        File image_file = new File(Folder,filename+".jpg");

        return image_file;


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        final String path = "sdcard/camera_remote/"+cabinetidstrnameimage+".jpg";

        Bitmap bmp = BitmapFactory.decodeFile( path );
        Bitmap photo = Bitmap.createScaledBitmap(bmp,300,300,true);

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 40, bytes);

        File f = new File(Environment.getExternalStorageDirectory()
                + File.separator + "camera_remote/" + cabinetidstrnameimage+".jpg");
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




        btntagwithimage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {



                // Create a storage reference from our app
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();
                Uri file = Uri.fromFile(new File(path));
                StorageReference riversRef = storageRef.child("remote_camera"+ File.separator+file.getLastPathSegment());
                UploadTask uploadTask = riversRef.putFile(file);

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


                        alert.hide();
                    }
                });




            }
        });






    }



    public void animateMarker(final LatLng toPosition, final Marker marker, final String name) {

        if (marker != null) {

            //move map camera

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {




                    Date c = Calendar.getInstance().getTime();


                    String datetime = String.valueOf(c);
                    //marker = mMap.addMarker(markerOptions);
                     Context context = getApplicationContext();
                    TextView text = new TextView(context);
                    text.setText("Active" +"/n"+ datetime);
                   // IconGenerator generator = new IconGenerator(context);
                   // generator.setBackground(context.getDrawable(R.drawable.roundeshape2));
                   // generator.setContentView(text);
                    //Bitmap icon = generator.makeIcon();

                    //  marker.remove();
                    marker.setPosition(toPosition);

                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.contractor_icon));


                    //mCurrLocationMarker.setSnippet("Active:" + datetime);
                    //marker.setSnippet("Active:" + datetime);
                 //   marker.showInfoWindow();
                  //  ShowToastInIntentService("Remote: Action " + name);


//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(toPosition));
                   // mMap.animateCamera(CameraUpdateFactory.zoomTo(5));


                }
            });

        }
    }

    public void animateMarker2(final LatLng toPosition,final Marker marker,final String name) {

        if (marker != null) {

            //move map camera

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {



                    marker.setPosition(toPosition);

                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.contractor_icon));

                    Date c = Calendar.getInstance().getTime();


                    String datetime = String.valueOf(c);

                    marker.setSnippet(name+"\n"+datetime);
                    marker.showInfoWindow();

                  //  ShowToastInIntentService("Remote: Action " + name);


                    mMap.moveCamera(CameraUpdateFactory.newLatLng(toPosition));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15));




                }
            });

        }
    }

    public void tagcustomer(final LatLng toPosition,final String nametag) {



            //move map camera

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {


////                 if (mCurrLocationMarker != null) {
////                       mCurrLocationMarker.remove();
////                   }
//
                MarkerOptions markerOptions = new MarkerOptions();
//
//
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));
//
                    markerOptions.position(toPosition);
                    markerOptions.title(nametag);
                    Marker marker = mMap.addMarker(markerOptions);




                    //marker = mMap.addMarker(markerOptions);

                    //  marker.remove();

//                       mCurrLocationMarker = mMap.addMarker(markerOptions);
//                       mCurrLocationMarker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.contractor_icon));


                    Date c = Calendar.getInstance().getTime();


                    String datetime = String.valueOf(c);
                    //mCurrLocationMarker.setSnippet("Active:" + datetime);
                    marker.setSnippet("Active:" + datetime);
                   // ShowToastInIntentService("Remote: Action " + nametag);


                    mMap.moveCamera(CameraUpdateFactory.newLatLng(toPosition));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15));


                }
            });

        }


    public void receivercoords(String data){
        if(data != null) {

            String[] datalist = data.split(",");
            String UUID = datalist[0];

            Integer msg = datalist.length;

           // ShowToastInIntentService(data);


            if (datalist.length >2) {

                double latitude = Double.parseDouble(datalist[1]);
                double longitude = Double.parseDouble(datalist[2]);


                LatLng latlng = new LatLng(latitude, longitude);

                Marker marker = eventMarkerMap.get(UUID);
                String name = eventname.get(UUID);


                Intent done = new Intent();

                done.addCategory(Intent.CATEGORY_LAUNCHER);

                sendBroadcast(done);
                // movemarker(data);
               // ShowToastInIntentService("Remote: Listening " + name);
                //onLocationChanged(latlng);

                if (UUID != "customer" && msg == 3) {

                    animateMarker(latlng, marker, name);
                }
                if (UUID.equals("customer")) {

                    String ttno = datalist[3];

                    tagcustomer(latlng, ttno);
                }

                if (msg > 3) {

                    String chatmsg = "Speak:" + datalist[3];
                    animateMarker2(latlng, marker, chatmsg);

                }


            }
        }

    }








    public void animateMarkerbounce(final Marker marker, final LatLng toPosition,
                              final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;

                marker.setPosition(toPosition);

                marker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.contractor_icon));

                Date c = Calendar.getInstance().getTime();
                String datetime = String.valueOf(c);
                //mCurrLocationMarker.setSnippet("Active:" + datetime);
                marker.setSnippet("Active:" + datetime);


                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                        marker.showInfoWindow();
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


        wl.release();
    }

    @Override
    public void onBackPressed() {



        moveTaskToBack(true);
    }








}

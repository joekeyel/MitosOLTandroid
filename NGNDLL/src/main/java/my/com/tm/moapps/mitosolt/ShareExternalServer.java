package my.com.tm.moapps.mitosolt;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;

import android.location.Address;
import android.location.Geocoder;
import android.telephony.TelephonyManager;
import android.util.Log;


import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.socket.client.IO;
import io.socket.client.Socket;


public class  ShareExternalServer  {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    List<Address> addresses ;
    String city = "";

    double latitude,longitude;
    public static String USERNAME = "";
    getuser userobject;
    public static String DATA = "";




    public String shareRegIdWithAppServer(final Context context,
                                          final String regId,
                                           final String phone,
                                           final String name,
                                           final  String group,
                                          final  String building
                                         ) {


        TelephonyManager telephonyManager = (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
       // String imei ="phonekhairiahndsb";



       BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
   //    String device_name= mBluetoothAdapter.getName();
        String device_name= "emulator";

     //   String imei = UUID.randomUUID().toString();

//        BluetoothAdapter m_BluetoothAdapter	= null; // Local Bluetooth adapter
//        m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        String imei = m_BluetoothAdapter.getAddress();


        String basket = "ipmsanvendor";


        String result = "";
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("regId", regId);
        paramsMap.put("emei", imei);
        paramsMap.put("device", device_name);
        paramsMap.put("phone", phone);
        paramsMap.put("name", name);
        paramsMap.put("group", group);
        paramsMap.put("buildingid", building);
        paramsMap.put("basket", basket);


        try {
            URL serverUrl = null;
            try {
                serverUrl = new URL(Config.APP_SERVER_URL);
            } catch (MalformedURLException e) {
                Log.e("AppUtil", "URL Connection Error: "
                        + Config.APP_SERVER_URL, e);
                result = "Invalid URL: " + Config.APP_SERVER_URL;
            }

            StringBuilder postBody = new StringBuilder();
            Iterator<Map.Entry<String, String>> iterator = paramsMap.entrySet()
                    .iterator();

            while (iterator.hasNext()) {
                Map.Entry<String, String> param = iterator.next();
                postBody.append(param.getKey()).append('=')
                        .append(param.getValue());
                if (iterator.hasNext()) {
                    postBody.append('&');
                }
            }
            String body = postBody.toString();
            byte[] bytes = body.getBytes();
            HttpURLConnection httpCon = null;
            try {
                httpCon = (HttpURLConnection) serverUrl.openConnection();
                httpCon.setDoOutput(true);
                httpCon.setUseCaches(false);
                httpCon.setFixedLengthStreamingMode(bytes.length);
                httpCon.setRequestMethod("POST");
                httpCon.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded;charset=UTF-8");
                OutputStream out = httpCon.getOutputStream();
                out.write(bytes);
                out.close();



                int status = httpCon.getResponseCode();
                if (status == 200) {
                    result = "RegId shared with Application Server. Name: "
                            + name +"\nPhone:"+phone+"\nPhone Type:"+device_name+"\nRegid:"+regId+"\nGroup:"+group;
                    Log.e("Send to server", "---------- Send to server");
                } else {
                    result = "Post Failure." + " Status: " + status;
                }
                Log.e("Send to server", "----------- "+result);
            } finally {
                if (httpCon != null) {
                    httpCon.disconnect();
                }
            }

        } catch (IOException e) {
            result = "Post Failure. Error in sharing with App Server.";
            Log.e("AppUtil", "Error in sharing with App Server: " + e);
        }
        return result;
    }


    public String updateltglng(final Context context, final String lat, final String lng) {

        TelephonyManager telephonyManager = (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();

        userobject = new getuser();
        userobject.execute(Config.APP_SERVER_URL6,imei);

         Socket mSocket = null;

        {
            try {
                mSocket = IO.socket("http://58.27.84.188/");
            } catch (URISyntaxException e) {}
        }


        latitude = Double.parseDouble(lat);
        longitude = Double.parseDouble(lng);



        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            city = addresses.get(0).getLocality();
            DATA = imei+","+lat+","+lng;
        } catch (IOException e) {
            e.printStackTrace();

            DATA = MyActivity.UUIDLATLNG;

        }

        //firebase realtime datase



        final String latfb = String.valueOf(latitude);
        final String lngfb = String.valueOf(longitude);

//        latitude = lat;
//        longitude = lng;


//socket io server
        mSocket.connect();
        mSocket.emit("chat message",USERNAME +':'+city);

//        datasocketmap data = new datasocketmap();
//        data.setUuid(imei);
//        data.setLat(lat);
//        data.setLng(lng);

        String data = imei+","+lat+","+lng;

      //  mSocket.connect();

        if(DATA.equals("") || MyActivity.UUIDLATLNG!=null || MyActivity.UUIDLATLNG!="" || !MyActivity.UUIDLATLNG.equals(null)) {
            DATA = MyActivity.UUIDLATLNG;
        }

        Log.e("sendcoords emitter",DATA);
        if(DATA!="" || !DATA.equals(null) || DATA != null) {
            mSocket.emit("send:coords", data);
        }


       // mSocket.emit("chat message",data);


        // String imei ="phonekhairiahndsb";

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //    String device_name= mBluetoothAdapter.getName();
        String device_name= "emulator";

        //   String imei = UUID.randomUUID().toString();

//        BluetoothAdapter m_BluetoothAdapter	= null; // Local Bluetooth adapter
//        m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        String imei = m_BluetoothAdapter.getAddress();


        String basket = "ipmsanvendor";


        String result = "";
        Map<String, String> paramsMap = new HashMap<String, String>();

        paramsMap.put("emei", imei);
        paramsMap.put("device", device_name);
        paramsMap.put("basket", basket);
        paramsMap.put("lat", lat);
        paramsMap.put("lng", lng);



        try {
            URL serverUrl = null;
            try {
                serverUrl = new URL(Config.APP_SERVER_URL16);
            } catch (MalformedURLException e) {
                Log.e("AppUtil", "URL Connection Error: "
                        + Config.APP_SERVER_URL16, e);
                result = "Invalid URL: " + Config.APP_SERVER_URL16;
            }

            StringBuilder postBody = new StringBuilder();
            Iterator<Map.Entry<String, String>> iterator = paramsMap.entrySet()
                    .iterator();

            while (iterator.hasNext()) {
                Map.Entry<String, String> param = iterator.next();
                postBody.append(param.getKey()).append('=')
                        .append(param.getValue());
                if (iterator.hasNext()) {
                    postBody.append('&');
                }
            }
            String body = postBody.toString();
            byte[] bytes = body.getBytes();
            HttpURLConnection httpCon = null;
            try {
                httpCon = (HttpURLConnection) serverUrl.openConnection();
                httpCon.setDoOutput(true);
                httpCon.setUseCaches(false);
                httpCon.setFixedLengthStreamingMode(bytes.length);
                httpCon.setRequestMethod("POST");
                httpCon.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded;charset=UTF-8");
                OutputStream out = httpCon.getOutputStream();
                out.write(bytes);
                out.close();



                int status = httpCon.getResponseCode();
                if (status == 200) {
                    result = lat+","+lng;
                    DATA = imei+","+lat+","+lng;
                    Log.e("Send to server", "---------- Send to server "+lat+","+lng);
                } else {
                    result = "Post Failure." + " Status: " + status;
                    DATA = imei+",locationoff,locationoff";
                }
                Log.e("Send to server", "----------- "+result);
            } finally {
                if (httpCon != null) {
                    httpCon.disconnect();
                }
            }

        } catch (IOException e) {
            result = "Post Failure. Error in sharing with App Server.";
            Log.e("AppUtil", "Error in sharing with App Server: " + e);
        }
        return result;




    }







}

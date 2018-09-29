package my.com.tm.moapps.mitosolt;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by joe on 12/10/2016.
 */

public class callradiustest extends AsyncTask<String,Void,String> {


    @Override
    protected String doInBackground(String... params) {


        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String stringurl = "http://58.27.84.166/mcconline/MCC%20Online%20V3/proxy_radius.php";



        URL url = null;
        try {


//            String newUrl = stringurl + "?login="+params[0];
//            Log.e("TEST_CAAS", "req line: " + newUrl);
            url = new URL(stringurl);
          conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);



            OutputStream os = conn.getOutputStream();
            BufferedWriter bufferwriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));




            String data = URLEncoder.encode("login", "UTF-8") + "=" + URLEncoder.encode(params[0], "UTF-8");


            bufferwriter.write(data);
            bufferwriter.flush();
            bufferwriter.close();


            os.close();
            int code = conn.getResponseCode();
            Log.e("TEST_RADIUS", "resp code: " + code);

            InputStream stream;
            if (200 == code)
            {
                stream = conn.getInputStream();
            }
            else
            {
                stream = conn.getErrorStream();
            }


            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();


            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "");
            }

            String finalJson = buffer.toString();
            Log.e("TEST_RAdius", "resp body: " + finalJson);

            //array no 1 ...testing with streamyx domain

            JSONArray parentarray = new JSONArray(finalJson);
            JSONObject parentobjectstreamyx= parentarray.getJSONObject(0);
            JSONObject attributes = parentobjectstreamyx.getJSONObject("sessionInfo");
            String resultstmx = attributes.getString("callingStationID");
            String resultstmxactivetime = "";

            Log.e("TEST_radius", "resp streamx: " + resultstmx);

            if(resultstmx.equals("{}")){

                resultstmx = "OFFLINE";
            }else{

                resultstmxactivetime = attributes.getString("sessionStartTime");
                resultstmx = resultstmx + " Session Start Time :"+resultstmxactivetime;

            }

            //array no 2 ....testing with tmnet domain

            JSONObject parentobjecttmnet= parentarray.getJSONObject(1);
            JSONObject attributestmnet = parentobjecttmnet.getJSONObject("sessionInfo");
            String resulttmnet = attributestmnet.getString("callingStationID");

            Log.e("TEST_radius", "resp tmnet: " + resulttmnet);

            if(resulttmnet.equals("{}")){

                resulttmnet = "OFFLINE";
            }

            String resultradius = "\nStreamyx Session Test:"+resultstmx+",\ntmnet Session Test:"+resulttmnet;





            return resultradius;


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (JSONException e) {
            Log.e("TEST_radius_jSON_ERROR", String.valueOf(e));

        }


        return null;
    }


    @Override
    protected void onPostExecute(String s) {

        Main.getInstance().updateTheTextView(s);
    }







}

package my.com.tm.moapps.mitosolt;

import android.os.AsyncTask;
import android.util.Log;

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

/**
 * Created by joe on 12/10/2016.
 */

public class callomsasynctask2 extends AsyncTask<String,Void,String> {


    @Override
    protected String doInBackground(String... params) {


        String callerno = params[3];
        String token = params[1];
        String callee = params[2];



        HttpURLConnection conn = null;
        BufferedReader reader = null;




        URL url = null;
        try {

            String urlStr = params[0];
            String newUrl = urlStr + "?app_key=YHYfx8Tm3rf4m3SFlgaCTrLiWjQa&username=%2B601546010410&format=json&access_token="+token;
            Log.e("TEST_CAAS_TOKEN", "req line: " + newUrl);
            url = new URL(newUrl);
          conn = (HttpURLConnection) url.openConnection();

            //conn.setRequestProperty("Authorization", "Caas12345");

            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestProperty("Host","open.huawei.com:18083");
            conn.setRequestMethod("POST");

            conn.setDoOutput(true);
            conn.setDoInput(true);

            JSONObject body   = new JSONObject();

            body.put("displayNbr",callerno);
            body.put("callerNbr",callerno);
            body.put("calleeNbr",callee);
            body.put("languageType", 0);

            OutputStream os = conn.getOutputStream();



            BufferedWriter bufferwriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));


            bufferwriter.write(body.toString());


            bufferwriter.flush();



            bufferwriter.close();


            os.close();
            int code = conn.getResponseCode();
            Log.e("TEST_CAAS_TOKEN", "resp code: " + code);




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

            Log.e("TEST_CAAS_token", "resp body: " + finalJson);
//            JSONObject parentObject = new JSONObject(finalJson);
//
//            String resultcode = parentObject.getString("resultcode");
//
//            omsresponsemodel responseobject = new omsresponsemodel();
//
//            responseobject.setResultcode(parentObject.getString("resultcode"));
//            responseobject.setAccess_token(parentObject.getString("access_token"));
//            responseobject.setRefresh_token(parentObject.getString("refresh_token"));
//            responseobject.setExpires_in(parentObject.getInt("expires_in"));


 //           omsresponsemodel response = new omsresponsemodel();

//           response.add(responseobject);

            Log.e("TEST_CAAS_token", "resp body: " + finalJson);

            return finalJson;


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }


    @Override
    protected void onPostExecute(String s) {

        Main.getInstance().updateTheTextViewoms2(s);
    }







}

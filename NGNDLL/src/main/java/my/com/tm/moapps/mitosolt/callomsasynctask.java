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

public class callomsasynctask extends AsyncTask<String,Void,omsresponsemodel> {


    @Override
    protected omsresponsemodel doInBackground(String... params) {


        HttpURLConnection conn = null;
        BufferedReader reader = null;



        URL url = null;
        try {

            String urlStr = params[0];
            String newUrl = urlStr + "?app_key=YHYfx8Tm3rf4m3SFlgaCTrLiWjQa&username=%2B601546010410&format=json";
            Log.e("TEST_CAAS", "req line: " + newUrl);
            url = new URL(newUrl);
          conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "Caas12345");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);



            OutputStream os = conn.getOutputStream();
            BufferedWriter bufferwriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

//
//            String data = URLEncoder.encode("version", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8") +
//                    "&" + URLEncoder.encode("app_key", "UTF-8") + "=" + URLEncoder.encode("Z22O68OQgI1MUf3Vy_zjaJ4Dspsa", "UTF-8") +
//                    "&" + URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode("+601546010286", "UTF-8") +
//                    "&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
//
//
//            bufferwriter.write(data);
//            bufferwriter.flush();
            bufferwriter.close();

            os.close();
            int code = conn.getResponseCode();
            Log.e("TEST_CAAS", "resp code: " + code);

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
            Log.e("TEST_CAAS", "resp body: " + finalJson);
            JSONObject parentObject = new JSONObject(finalJson);

            String resultcode = parentObject.getString("resultcode");

            omsresponsemodel responseobject = new omsresponsemodel();

            responseobject.setResultcode(parentObject.getString("resultcode"));
            responseobject.setAccess_token(parentObject.getString("access_token"));
            responseobject.setRefresh_token(parentObject.getString("refresh_token"));
            responseobject.setExpires_in(parentObject.getInt("expires_in"));


 //           omsresponsemodel response = new omsresponsemodel();

//           response.add(responseobject);

            return responseobject;


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
    protected void onPostExecute(omsresponsemodel s) {

        Main.getInstance().updateTheTextViewoms(s);
    }







}

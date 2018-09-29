package my.com.tm.moapps.mitosolt;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by joe on 5/26/2016.
 */
public class getfirebasetoken extends AsyncTask<String,Integer,String>{


    private String token;
    String username;
    private String uuid;
    String results;


    @Override
    protected String doInBackground(String... params) {


        HttpURLConnection conn = null;
        BufferedReader reader = null;



        URL url = null;
        try {

            String query = "?uuid="+params[1]+"&username=" + params[2]+"&password="+params[3];
            url = new URL(params[0]+query);
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

            results = parentObject.getString("result");

            if(results.equals("ok")) {
                token = parentObject.getString("token");
                uuid = parentObject.getString("uuid");

                username = parentObject.getString("username");
            }
            if(results.equals("not ok")){

                token = "Not Authorized";
            }




            return token;


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

   if(s!=null) {
       login.token = s;
       login.getInstance().returnloginresult(s);

   }
    }
}

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
public class rememberuser extends AsyncTask<String,Integer,String>{


    private String name;
    String basket;
    private String mobilephone;
    private  String userlogin;
    private  String userpassword;


    @Override
    protected String doInBackground(String... params) {


        HttpURLConnection conn = null;
        BufferedReader reader = null;



        URL url = null;
        try {

            String query = "?uuid=" + params[1];
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



            name = parentObject.getString("name");
            mobilephone = parentObject.getString("phone");
            String group = parentObject.getString("group");
            basket = parentObject.getString("basket");
            userlogin = parentObject.getString("userlogin");
            userpassword = parentObject.getString("userpassword");


            String username = name + " " + group;


            return username;


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



        if(login.getInstance()!=null && s!=null) {
            login.getInstance().setuser(userlogin, userpassword);

        }




    }
}

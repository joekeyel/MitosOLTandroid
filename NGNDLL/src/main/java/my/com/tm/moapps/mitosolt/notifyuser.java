package my.com.tm.moapps.mitosolt;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by joe on 4/12/2016.
 */
public class notifyuser extends AsyncTask<String,Void,String> {

   Context context;

    public notifyuser() {


    }

    @Override
    protected String doInBackground(String... params) {

        String building = params[0];
        String basket = params[1];
        String total = params[2];

        String stringurl = "http://58.27.84.166/mcconline/MCC%20Online%20V3/notifyuser.php";

        try {
            URL url = new URL(stringurl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter bufferwriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

            String data = URLEncoder.encode("buildingid", "UTF-8") + "=" + URLEncoder.encode(building, "UTF-8") +
                    "&" + URLEncoder.encode("basket", "UTF-8") + "=" + URLEncoder.encode(basket, "UTF-8") +
                    "&" + URLEncoder.encode("total", "UTF-8") + "=" + URLEncoder.encode(total, "UTF-8");


            bufferwriter.write(data);
            bufferwriter.flush();
            bufferwriter.close();
            os.close();

            InputStream is = conn.getInputStream();
            is.close();

            return "Sent Notify...";



        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return "not ok";
    }




}

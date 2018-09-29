package my.com.tm.moapps.mitosolt;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by joe on 7/17/2016.
 */
public class audittrail extends AsyncTask<String, Integer, ArrayList<auditmodel>> {

    String ttno2;

    public audittrail(Context applicationContext) {

    }

    @Override
    protected ArrayList<auditmodel> doInBackground(String... params) {
        HttpURLConnection conn = null;
        BufferedReader reader = null;

        ttno2 = params[1];

        URL url = null;
        try {

            String query = "?ttno=" + params[1];
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
            JSONArray parentArray2= parentObject.getJSONArray("audit");

            ArrayList<auditmodel> auditlist = new ArrayList<>();

            for (int j = 0; j < parentArray2.length(); j++) {


                JSONObject finalObject2 = parentArray2.getJSONObject(j);

                if(finalObject2.getString("remark")!="null") {
                    auditmodel auditobject = new auditmodel();

                    auditobject.setRemark(finalObject2.getString("remark"));
                    auditobject.setUpdateby(finalObject2.getString("updatedby"));

                    auditlist.add(auditobject);

                }



            }

            return auditlist;


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
    protected void onPostExecute(ArrayList<auditmodel> strings) {


      if(Main_Activity.getInstance()!=null) {

          Main_Activity.getInstance().onShowPopup2(strings);

      }


    }
}

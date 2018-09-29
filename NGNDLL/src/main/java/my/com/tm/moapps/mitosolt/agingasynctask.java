package my.com.tm.moapps.mitosolt;

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
 * Created by joe on 8/29/2016.
 */
public class agingasynctask extends AsyncTask<String, Integer, ArrayList<agingmodel>> {


    @Override
    protected ArrayList<agingmodel> doInBackground(String... params) {

        HttpURLConnection conn = null;
        BufferedReader reader = null;


        URL url = null;
        try {


            url = new URL(params[0]);
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
            JSONArray parentArray2= parentObject.getJSONArray("summaryaging");
            JSONArray parentArray3= parentObject.getJSONArray("count");
            JSONArray parentArray4= parentObject.getJSONArray("counttt");

            ArrayList<agingmodel> sumarrylist = new ArrayList<>();

            for (int j = 0; j < parentArray2.length(); j++) {

                agingmodel sumarryobject = new agingmodel();
                JSONObject finalObject2 = parentArray2.getJSONObject(j);
                JSONObject finalObject3 = parentArray3.getJSONObject(j);
                JSONObject finalObject4 = parentArray4.getJSONObject(j);


                sumarryobject.setExchange(finalObject2.getString("buildingid"));
                sumarryobject.setMigratesub(finalObject3.getString("capacity"));
                sumarryobject.setTotaltt(finalObject4.getString("totaltt"));
                sumarryobject.setOnedays(finalObject2.getString("aginglessthan1days"));
                sumarryobject.setTwodays(finalObject2.getString("agingbetween1to3days"));
                sumarryobject.setThreedays(finalObject2.getString("agingmorethan3days"));
                sumarryobject.setTotalaging(finalObject2.getString("totaltt"));

                sumarrylist.add(sumarryobject);



            }

            return sumarrylist;


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
    protected void onPostExecute(ArrayList<agingmodel> summarymodels) {
        aging.getInstance().updatelv(summarymodels);
        aging.getInstance().progressbarhide();


    }


}


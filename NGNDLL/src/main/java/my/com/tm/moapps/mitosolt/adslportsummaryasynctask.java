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
public class adslportsummaryasynctask extends AsyncTask<String, Integer, ArrayList<adslportsatesummarymodel>> {

    String querytest;

    @Override
    protected ArrayList<adslportsatesummarymodel> doInBackground(String... params) {

        HttpURLConnection conn = null;
        BufferedReader reader = null;


        URL url = null;
        try {

            String query = "?uuid=" + params[1];

            url = new URL(params[0] + query);
            conn = (HttpURLConnection) url.openConnection();

            querytest = params[0] + query;
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
            JSONArray parentArray2= parentObject.getJSONArray("masterlist");


            ArrayList<adslportsatesummarymodel> sumarrylist = new ArrayList<>();

            for (int j = 0; j < parentArray2.length(); j++) {

                adslportsatesummarymodel sumarryobject = new adslportsatesummarymodel();
                JSONObject finalObject2 = parentArray2.getJSONObject(j);



                sumarryobject.setTargetcabinet(finalObject2.getString("TARGET CABINET"));
                sumarryobject.setAdslportstate(finalObject2.getString("adslportstate"));
                sumarryobject.setTotal(finalObject2.getString("total"));


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
    protected void onPostExecute(ArrayList<adslportsatesummarymodel> summarymodels) {

      //  viewadslport.getInstance().showtoast(querytest);

        if(summarymodels!=null) {
            //to list out the view
            viewadslportsummary.getInstance().updatelv(summarymodels);
            //to filter the list view
            viewadslportsummary.getInstance().clicksearch(summarymodels);
            //to hideprogress bar
            viewadslportsummary.getInstance().progressbarhide();

        }
        else{
           viewadslportsummary.getInstance().progressbarhide();
           viewadslportsummary.getInstance().showtoast("No Data");
        }


    }


}


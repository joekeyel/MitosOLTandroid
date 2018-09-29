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
public class adslportstateasynctask extends AsyncTask<String, Integer, ArrayList<adslportsatemodel>> {

    String querytest;

    @Override
    protected ArrayList<adslportsatemodel> doInBackground(String... params) {

        HttpURLConnection conn = null;
        BufferedReader reader = null;


        URL url = null;
        try {

            String query = "?uuid=" + params[1] + "&cabinetid=" + params[2];

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


            ArrayList<adslportsatemodel> sumarrylist = new ArrayList<>();

            for (int j = 0; j < parentArray2.length(); j++) {

                adslportsatemodel sumarryobject = new adslportsatemodel();
                JSONObject finalObject2 = parentArray2.getJSONObject(j);



                sumarryobject.setServicenumber(finalObject2.getString("SERVICE NUMBER"));
                sumarryobject.setTargetdslout(finalObject2.getString("TARGET DSL OUT"));
                sumarryobject.setTargetpotsout(finalObject2.getString("TARGET POTS OUT"));
                sumarryobject.setTargetlen(finalObject2.getString("TARGET LEN"));
                sumarryobject.setPortstatestatus(finalObject2.getString("adslportstate"));
                sumarryobject.setTargetdside(finalObject2.getString("TARGET CABINET DSIDE"));
                sumarryobject.setActualdside(finalObject2.getString("CORRECT DSIDE PAIR"));
                sumarryobject.setPortstatestatusand(finalObject2.getString("statusand"));
                sumarryobject.setLoginid(finalObject2.getString("loginid"));

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
    protected void onPostExecute(ArrayList<adslportsatemodel> summarymodels) {

      //  viewadslport.getInstance().showtoast(querytest);

        if(summarymodels!=null) {
            viewadslport.getInstance().updatelv(summarymodels);
            viewadslport.getInstance().progressbarhide();
            viewadslport.getInstance().clicksearch2(summarymodels);

        }
        else{
           viewadslport.getInstance().progressbarhide();
           viewadslport.getInstance().showtoast("No Data");
        }


    }


}


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
 * Created by joe on 3/6/2016.
 */
public class summaryasynctask extends AsyncTask <String, Integer, ArrayList<summarymodel>> {

String basketstr;
int badgecount = 0;
    @Override
    protected ArrayList<summarymodel> doInBackground(String... params) {


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
            JSONArray parentArray2= parentObject.getJSONArray("summary");

            ArrayList<summarymodel> sumarrylist = new ArrayList<>();

            for (int j = 0; j < parentArray2.length(); j++) {

                summarymodel sumarryobject = new summarymodel();
                JSONObject finalObject2 = parentArray2.getJSONObject(j);


                if(finalObject2.getString("basket").equals("ipmsanvendor"))
                {
                    basketstr = "and";
                }
                else
                {
                    basketstr=finalObject2.getString("basket");
                }


                  sumarryobject.setBuildingid(finalObject2.getString("buildingid"));
                    sumarryobject.setBasket(basketstr);
                    sumarryobject.setTotal(finalObject2.getString("totaltt"));

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
    protected void onPostExecute(ArrayList<summarymodel> summarymodels) {
        if(summarymodels!=null) {
            summary.getInstance().updatelv(summarymodels);
            summary.getInstance().progressbarhide();

        }




    }


}

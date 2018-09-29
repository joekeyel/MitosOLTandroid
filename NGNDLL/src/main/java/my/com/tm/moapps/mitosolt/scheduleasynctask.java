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
public class scheduleasynctask extends AsyncTask<String, Integer, ArrayList<schedulemodel>> {


    @Override
    protected ArrayList<schedulemodel> doInBackground(String... params) {


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
            JSONArray parentArray2= parentObject.getJSONArray("schedule");
//            JSONArray parentArray3= parentObject.getJSONArray("count");
//            JSONArray parentArray4= parentObject.getJSONArray("counttt");

            ArrayList<schedulemodel> sumarrylist = new ArrayList<>();

            for (int j = 0; j < parentArray2.length(); j++) {

                schedulemodel sumarryobject = new schedulemodel();
                JSONObject finalObject2 = parentArray2.getJSONObject(j);
//                JSONObject finalObject3 = parentArray3.getJSONObject(j);
//                JSONObject finalObject4 = parentArray4.getJSONObject(j);


                sumarryobject.setTargetcabinet(finalObject2.getString("New Cabinet"));
                sumarryobject.setMigrationdate(finalObject2.getString("Migration Date"));
                sumarryobject.setState(finalObject2.getString("State"));
                sumarryobject.setStopdate(finalObject2.getString("stopdate"));
                sumarryobject.setPmwno(finalObject2.getString("pmwno"));
                sumarryobject.setCkc1(finalObject2.getString("ckcold"));
                sumarryobject.setCkc2(finalObject2.getString("ckcnew"));
                sumarryobject.setOldcabinet(finalObject2.getString("Old Cabinet"));
                sumarryobject.setStatuscabinet(finalObject2.getString("Remark"));
                sumarryobject.setAbbr(finalObject2.getString("Abbr"));
                sumarryobject.setSitename(finalObject2.getString("Site Name"));
                sumarryobject.setProjecttype(finalObject2.getString("Projecttype"));
                sumarryobject.setMigrationstatus(finalObject2.getString("MonitoringStatus"));

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
    protected void onPostExecute(ArrayList<schedulemodel> summarymodels) {
        schedule.getInstance().updatelv(summarymodels);
        schedule.getInstance().progressbarhide();


    }


}


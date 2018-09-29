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
 * Created by joe on 9/7/2016.
 */
public class phonelocation extends  AsyncTask<String,Integer,ArrayList<phonelistmodel>> {

    public phonelocation(Context applicationcontext){

    }
    @Override
    protected ArrayList<phonelistmodel> doInBackground(String... params) {
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
            JSONArray parentArray2= parentObject.getJSONArray("phonelist");

            ArrayList<phonelistmodel> phonelist = new ArrayList<>();

            for (int j = 0; j < parentArray2.length(); j++) {


                JSONObject finalObject2 = parentArray2.getJSONObject(j);

                if(finalObject2.getString("building")!="null") {
                    phonelistmodel phoneobject = new phonelistmodel();

                    phoneobject.setBuilding(finalObject2.getString("building"));
                    phoneobject.setNameuser(finalObject2.getString("name"));
                    phoneobject.setPhonenumber(finalObject2.getString("phoneNumber"));
                    phoneobject.setRegid(finalObject2.getString("regid"));
                    phoneobject.setUuid(finalObject2.getString("uuid"));
                    phoneobject.setLatitude(finalObject2.getString("lat"));
                    phoneobject.setLongitude(finalObject2.getString("lng"));
                    phoneobject.setModifytime(finalObject2.getString("modifytime"));

                    phonelist.add(phoneobject);

                }



            }

            return phonelist;


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
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected void onPostExecute(ArrayList<phonelistmodel> phonelistmodels) {

        if(phonelistmodels!=null && MyActivity.getInstance() != null) {
            MyActivity.getInstance().putmarkermap(phonelistmodels);
        }

        if(phonelistmodels!=null && livemap.getInstance()!=null) {
            livemap.getInstance().putmarkermap(phonelistmodels);
        }

    }
}

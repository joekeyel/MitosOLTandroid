package my.com.tm.moapps.mitosolt;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import static java.lang.Integer.parseInt;

/**
 * Created by joe on 2/1/2016.
 */
public class updatemrstatusand extends AsyncTask<String,Void,String> {

    Context context;
    String servicenumber;
    Integer position;
    String statusand;


    public updatemrstatusand(Context applicationContext) {
        this.context = applicationContext;

    }


    @Override
    protected String doInBackground(String... params) {

         servicenumber = params[0];
         position = parseInt(params[1]);
         statusand = params[2];

        String stringurl = "http://58.27.84.166/mcconline/MCC%20Online%20V3/update_mradslport_mobile.php";

        try {
            URL url = new URL(stringurl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter bufferwriter = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));

            String data = URLEncoder.encode("servicenumber","UTF-8")+"="+URLEncoder.encode(servicenumber,"UTF-8")+
                    "&"+URLEncoder.encode("statusand","UTF-8")+"="+URLEncoder.encode(statusand,"UTF-8");

            bufferwriter.write(data);
            bufferwriter.flush();
            bufferwriter.close();
            os.close();

            InputStream is = conn.getInputStream();
            is.close();

            return "Update Success...";



        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }


    @Override
    protected void onPostExecute(String result) {

      //  Toast.makeText(context,result,Toast.LENGTH_LONG).show();
        viewadslport.getInstance().updateviewlist(position,statusand);

        Toast.makeText(context,result,Toast.LENGTH_LONG).show();



    }
}

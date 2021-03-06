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

/**
 * Created by joe on 2/1/2016.
 */
public class UpdateTT2 extends AsyncTask<String,Void,String> {

    Context context;

    public UpdateTT2(Context applicationContext) {
        this.context = applicationContext;

    }


    @Override
    protected String doInBackground(String... params) {

        String No = params[4];
        String ttno = params[0];
        String Username = params[1];
        String Status = params[2];
        String remark = params[3];
        String reasoncode = params[8];
        String servicenum = params[5];
        String created = params[6];
        String uuid = params[7];
        String stringurl = "http://58.27.84.166/mcconline/MCC%20Online%20V3/update_status_mobile.php";

        try {
            URL url = new URL(stringurl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter bufferwriter = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));

            String data = URLEncoder.encode("No","UTF-8")+"="+URLEncoder.encode(No,"UTF-8")+

                    "&"+URLEncoder.encode("textarea","UTF-8")+"="+URLEncoder.encode(remark,"UTF-8")+
                    "&"+URLEncoder.encode("MM_Username","UTF-8")+"="+URLEncoder.encode(Username,"UTF-8")+
                    "&"+URLEncoder.encode("reasoncode","UTF-8")+"="+URLEncoder.encode(reasoncode,"UTF-8")+
                    "&"+URLEncoder.encode("serviceno","UTF-8")+"="+URLEncoder.encode(servicenum,"UTF-8")+
                    "&"+URLEncoder.encode("created_date","UTF-8")+"="+URLEncoder.encode(created, "UTF-8")+
                    "&"+URLEncoder.encode("uuid","UTF-8")+"="+URLEncoder.encode(uuid, "UTF-8")+
                    "&"+URLEncoder.encode("ttno","UTF-8")+"="+URLEncoder.encode(ttno, "UTF-8")+
                    "&"+URLEncoder.encode("updateby","UTF-8")+"="+URLEncoder.encode(Username, "UTF-8")+
                    "&"+URLEncoder.encode("basket","UTF-8")+"="+URLEncoder.encode("mcc", "UTF-8")+
                    "&"+URLEncoder.encode("servicenumber","UTF-8")+"="+URLEncoder.encode(servicenum, "UTF-8");

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

        Toast.makeText(context,result,Toast.LENGTH_LONG).show();



    }
}

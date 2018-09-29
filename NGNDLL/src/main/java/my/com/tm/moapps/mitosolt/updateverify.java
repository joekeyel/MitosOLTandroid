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
public class updateverify extends AsyncTask<String,Void,String> {

   Context context;
    String position;
    String remarks;

    public updateverify(Context applicationContext) {
        this.context = applicationContext;

    }


    @Override
    protected String doInBackground(String... params) {



            String idverification = params[0];
//            String exchangeid = params[5];
//            String dsidepair = params[2];
//            String servicenumber = params[3];
//            String servicetype = params[4];
            String remark = params[1];
           position = params[2];
           remarks = params[1];



            String stringurl = "http://58.27.84.166/mcconline/MCC%20Online%20V3/update_verify.php";

            try {
                URL url = new URL(stringurl);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter bufferwriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                String data = URLEncoder.encode("idverification", "UTF-8") + "=" + URLEncoder.encode(idverification, "UTF-8") +

                        "&" + URLEncoder.encode("remark", "UTF-8") + "=" + URLEncoder.encode(remark, "UTF-8");

                bufferwriter.write(data);
                bufferwriter.flush();
                bufferwriter.close();
                os.close();

                InputStream is = conn.getInputStream();
                is.close();

                return "Upload to server";



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;

        }


    @Override
    protected void onPostExecute(String result) {

        int index = Integer.parseInt(position);

        Toast.makeText(context,result,Toast.LENGTH_LONG).show();

// will call function in verification activity to update the custom listview --verificationadaptor
        verification.getInstance().updateviewlist(index,remarks);



    }
}

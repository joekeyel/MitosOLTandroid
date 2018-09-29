package my.com.tm.moapps.mitosolt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;



import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class verify_sub extends AppCompatActivity {

    EditText cabinetid,dsidepair,esidepair,dslin,dslout,subnumber,remark;
    String cabinetidstr,dsidepairstr,esidepairstr,dslinstr,dsloutstr,subnumberstr;
    TextView print;
    dbasehandler dbasehandlers;
    private static verify_sub mainactivity;

    private Socket mSocket1;
    {
        try {
            mSocket1 = IO.socket("http://58.27.84.188/");
        } catch (URISyntaxException e) {}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_sub);

        mainactivity = this;

        cabinetid = (EditText)findViewById(R.id.cabinetidet);
        cabinetid.setText(getIntent().getStringExtra("exchangeid"));
        cabinetidstr = cabinetid.getText().toString();


        dsidepair = (EditText)findViewById(R.id.dsidepairet);
        dsidepair.setText(getIntent().getStringExtra("dsidepair"));
        dsidepairstr = dsidepair.getText().toString();

        TextView servicetype = (TextView)findViewById(R.id.tvservicetype);
        servicetype.setText(getIntent().getStringExtra("servicetype"));



        esidepair = (EditText)findViewById(R.id.esidepairet);
        esidepairstr = esidepair.getText().toString();

        dslin = (EditText)findViewById(R.id.dslinet);
        dslinstr = dslin.getText().toString();

        dslout = (EditText)findViewById(R.id.dsloutet);
        dsloutstr = dslout.getText().toString();

        subnumber = (EditText)findViewById(R.id.subnumberet);
        subnumber.setText(getIntent().getStringExtra("servicenumber"));
        subnumberstr = subnumber.getText().toString();


        print =(TextView)findViewById(R.id.tvprint);
        print.setMovementMethod(new ScrollingMovementMethod());


        EditText remark = (EditText) findViewById(R.id.etremark);
        remark.setText(getIntent().getStringExtra("remark"));


        dbasehandlers = new dbasehandler(this,null,null,1);

        mSocket1.connect();





    }

  public void uploadbutton(View view) {

      new AlertDialog.Builder(this)
              .setTitle("Update Service Number")
              .setMessage("Do you want to update server?")
              .setIcon(android.R.drawable.ic_dialog_alert)
              .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                  public void onClick(DialogInterface dialog, int whichButton) {

                      EditText remarkno = (EditText) findViewById(R.id.etremark);
                      String idverification = getIntent().getStringExtra("idverification");

                      updateverify update = new updateverify(getApplicationContext());
                      update.execute(idverification,remarkno.getText().toString(),getIntent().getStringExtra("position"));
                  }

              })
              .setNegativeButton(android.R.string.no, null).show();

  }


                    public void savebutton(View view) {
                        new AlertDialog.Builder(this)
                                .setTitle("Save Service Number")
                                .setMessage("Do you want to save to your phone?")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int whichButton) {

                                        EditText remark = (EditText) findViewById(R.id.etremark);

                                        CabinetInventory cabinetinvent = new CabinetInventory(cabinetid.getText().toString());
                                        cabinetinvent.setDsidepair(dsidepair.getText().toString());
                                        cabinetinvent.setEsidepair(esidepair.getText().toString());
                                        cabinetinvent.setDslin(dslin.getText().toString());
                                        cabinetinvent.setDslout(dslout.getText().toString());
                                        cabinetinvent.setSubnumber(subnumber.getText().toString());
                                        cabinetinvent.setBnumber(remark.getText().toString());


                                        dbasehandlers.addport(cabinetinvent);
                                        printdatabse();
                                    }

                                })
                                .setNegativeButton(android.R.string.no, null).show();

                  }

                  public static verify_sub getInstance() {

                      return mainactivity;
                  }


                  private void printdatabse() {

                      String dbstring = dbasehandlers.databasetostr(cabinetid.getText().toString());
                      print.setText(dbstring);

                  }


                  public void updateTheTextView1(final String t) {
                      this.runOnUiThread(new Runnable() {
                          public void run() {

                              String incoming = t;


                              EditText textV1 = (EditText) findViewById(R.id.etremark);
                              textV1.setText(incoming);
                              mSocket1.emit("chat message", "Verification on " + incoming + " Cabinet ID: " + cabinetid.getText().toString() + " Pair: " + dsidepair.getText().toString());

                          }
                      });
                  }

                  public void callnumber(View v) {

                      String phone_no2= subnumber.getText().toString();
                      StringBuilder phone2 = new StringBuilder(phone_no2);
                      if(phone2.length()==9)

                      phone2.insert(0,"0");

                      else if (phone2.length()==8){
                          phone2.deleteCharAt(1);
                          phone2.insert(0,"0");
                      }

                      String phone_no = subnumber.getText().toString().replaceAll("-", "");
                      Intent callIntent = new Intent(Intent.ACTION_CALL);
                      callIntent.setData(Uri.parse("tel:" + phone2));

                      startActivity(callIntent);


                  }


              }






package my.com.tm.moapps.mitosolt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

public class summary_cabinet extends AppCompatActivity {


    public static summary_cabinet summarycabinet;
    public String building,basket,total;
    ArrayList<summarymodel> summarylist;
    public int iClickedItem = 0;
    android.app.AlertDialog.Builder alertDialogprogress;
    android.app.AlertDialog alertprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_cabinet);

        building = getIntent().getStringExtra("building");

        if(getIntent().getStringExtra("basket").equals("and")){
            basket = "ipmsanvendor";
        }
        else {
            basket = getIntent().getStringExtra("basket");
        }

        progressdialogshow();


        new summaryasynctaskcabinet().execute(Config.APP_SERVER_URL18,building,basket);

        summarycabinet = this;

    }

    public static summary_cabinet getInstance() {
        return summarycabinet;
    }


    public void updatelv(final ArrayList<summarymodel> summary){

        this.runOnUiThread(new Runnable() {
            public void run() {

                summarylist = summary;

                final summaryadaptor sanoladaptor2 = new summaryadaptor(getApplicationContext(), R.layout.customrowcabinet, summarylist);
                final ListView lvsummary = (ListView) findViewById(R.id.lvcabinetsummary);
                lvsummary.setAdapter(sanoladaptor2);

                //this part to sent friendly reminder notification to mobile apps
                lvsummary.setOnItemClickListener(


                        new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                iClickedItem = position;


                                summarymodel summ = (summarymodel) lvsummary.getAdapter().getItem(position);
                                building = summ.getBuildingid();
                                basket = summ.getBasket();
                                total = summ.getTotal();

                                Toast.makeText(getApplicationContext(), building + " " + basket + " " + total, Toast.LENGTH_LONG).show();


                            }

                        });


            }


        });



    }

    public void progressdialogshow(){


        alertDialogprogress = new android.app.AlertDialog.Builder(summary_cabinet.this);

        alertprogress = alertDialogprogress.create();

        alertprogress.setTitle("Loading");

        LayoutInflater inflater = getLayoutInflater();

        // inflate the custom popup layout
        final View convertView = inflater.inflate(R.layout.progressdialog, null);
        // find the ListView in the popup layout
        final ProgressBar progressBar = (ProgressBar)convertView.findViewById(R.id.progressBar4);

        // setSimpleList(listView, comment);

        alertprogress.setView(convertView);
        alertprogress.setCanceledOnTouchOutside(false);
        alertprogress.show();

    }

    public void progressbarhide(){

        alertprogress.dismiss();

    }
}

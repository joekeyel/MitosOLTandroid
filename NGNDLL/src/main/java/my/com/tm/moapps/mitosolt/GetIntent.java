package my.com.tm.moapps.mitosolt;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by aneh on 8/16/2014.
 */
public class GetIntent extends Activity {

public static   ArrayList<String> msgarray2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getintent);


            Bundle get = getIntent().getExtras();
        if (get == null) {
            return;
        }
            String get1 = get.getString("Notif");

            msgarray2.add(get1);

            final ListAdapter sanoAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, msgarray2);
            final ListView sanoview2 = (ListView) findViewById(R.id.Sanolistview);
            sanoview2.setAdapter(sanoAdapter2);

        sanoview2.setOnItemClickListener(

                new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String ttno = String.valueOf(parent.getItemAtPosition(position));
                        Toast.makeText(getApplicationContext(), ttno, Toast.LENGTH_LONG).show();

                        Intent i = new Intent(getApplicationContext(),
                            Main_Activity.class);

                    i.putExtra("ttno", ttno);

                    startActivity(i);
                    finish();



                    }

                }

        );



    }


//    @Override
//    protected void onNewIntent(Intent intent){
//            super.onNewIntent(intent);
//
//
//
//        String message = intent.getExtras().getString("Notif");
//
//
//        msgarray2.add(message);
//
//
//        ListAdapter sanoAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,msgarray2);
//        ListView sanoview = (ListView)findViewById(R.id.Sanolistview);
//        sanoview.setAdapter(sanoAdapter);
//
//
//    }




}


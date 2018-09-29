package my.com.tm.moapps.mitosolt;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;

public class graph extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graphwebview);


        String targetcabinet = getIntent().getStringExtra("targetcabinet");
        String migrationdate = getIntent().getStringExtra("migrationdate");
        String stopdate = getIntent().getStringExtra("stopdate");
        String state= getIntent().getStringExtra("state");
        String oldcabinet = getIntent().getStringExtra("oldcabinet");
        String statussite = getIntent().getStringExtra("statussite");
        String ckc1 = getIntent().getStringExtra("ckc1");
        String ckc2 = getIntent().getStringExtra("ckc2");
        String pmwno = getIntent().getStringExtra("pmwno");
        String building = getIntent().getStringExtra("building");
        String sitename = getIntent().getStringExtra("sitename");

        WebView graphview = (WebView)findViewById(R.id.webviewgraph);

        graphview.getSettings().setJavaScriptEnabled(true);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            graphview.getSettings().setAllowUniversalAccessFromFileURLs(true);
            graphview.getSettings().setAllowFileAccessFromFileURLs(true);
        }

        graphview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        graphview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
            }
        });
        graphview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        graphview.setVerticalScrollBarEnabled(true);
        String query="building="+building+"&stopdate="+stopdate+"&migrationdate="+migrationdate+"&targetcabinet="+targetcabinet+"&oldcabinet="+oldcabinet+"&state="+state+"&sitename="+sitename;

        graphview.loadUrl("http://58.27.84.166/mcconline/MCC%20Online%20V3/ipmsan_android_graph3.php?"+query);
    }
}

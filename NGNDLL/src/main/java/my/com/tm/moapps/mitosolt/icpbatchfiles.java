package my.com.tm.moapps.mitosolt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class icpbatchfiles extends AppCompatActivity {

    WebView mWebView;
    private String uri = "http://58.27.84.166/mcconline/macro/dailyicpfiles.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icpbatchfiles);

        mWebView = (WebView)findViewById(R.id.web_view);

        final WebSettings settings = mWebView.getSettings();
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptEnabled(true);
        mWebView.setVerticalScrollBarEnabled(true);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
                super.shouldOverrideUrlLoading(view, url);
                return false;
            }

            @Override
            public void onPageFinished(final WebView view, final String url) {
                super.onPageFinished(view, url);
                mWebView.requestLayout();
            }

        });
        mWebView.loadUrl(uri);
    }
}

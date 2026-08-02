package com.appmint.template;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
    private WebView webView;
    private final String SITE_URL = "https://example.com"; // will be replaced by workflow

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebView(this);
        setContentView(webView);
        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setAllowFileAccess(true);
        webView.setWebViewClient(new WebViewClient());
        String url = getIntent().getStringExtra("url");
        if(url==null) url = SITE_URL;
        // Read from BuildConfig / injected file
        try {
            java.io.InputStream is = getAssets().open("config.json");
            java.util.Scanner sc = new java.util.Scanner(is).useDelimiter("\\A");
            String json = sc.hasNext() ? sc.next() : "";
            if(json.contains("http")) {
                int start = json.indexOf("http");
                int end = json.indexOf("\"", start);
                if(end>start) url = json.substring(start, end);
            }
        } catch(Exception e) {}
        webView.loadUrl(url);
    }
    @Override
    public void onBackPressed() {
        if(webView.canGoBack()) webView.goBack(); else super.onBackPressed();
    }
}

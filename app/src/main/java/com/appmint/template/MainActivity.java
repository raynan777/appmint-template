package com.appmint.template;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.Gravity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.animation.AlphaAnimation;
import org.json.JSONObject;

public class MainActivity extends Activity {
    private WebView webView;
    private LinearLayout splashView;
    private String SITE_URL = "https://example.com";
    private String APP_NAME = "My App";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadConfig();
        FrameLayout root = new FrameLayout(this);
        root.setBackgroundColor(Color.parseColor("#000000"));
        splashView = new LinearLayout(this);
        splashView.setOrientation(LinearLayout.VERTICAL);
        splashView.setGravity(Gravity.CENTER);
        splashView.setBackgroundColor(Color.parseColor("#000000"));
        FrameLayout.LayoutParams splashParams = new FrameLayout.LayoutParams(-1,-1);
        splashView.setLayoutParams(splashParams);
        TextView iconText = new TextView(this);
        String firstLetter = APP_NAME.length() > 0 ? String.valueOf(APP_NAME.charAt(0)).toUpperCase() : "A";
        iconText.setText(firstLetter);
        iconText.setTextSize(48);
        iconText.setTextColor(Color.WHITE);
        iconText.setTypeface(null, Typeface.BOLD);
        iconText.setGravity(Gravity.CENTER);
        int size = dp(100);
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(size, size);
        iconParams.bottomMargin = dp(20);
        iconText.setLayoutParams(iconParams);
        iconText.setPadding(0, dp(15), 0, 0);
        TextView appNameText = new TextView(this);
        appNameText.setText(APP_NAME);
        appNameText.setTextSize(28);
        appNameText.setTextColor(Color.WHITE);
        appNameText.setTypeface(null, Typeface.BOLD);
        appNameText.setGravity(Gravity.CENTER);
        TextView loadingText = new TextView(this);
        loadingText.setText("Loading...");
        loadingText.setTextSize(14);
        loadingText.setTextColor(Color.parseColor("#888888"));
        loadingText.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams loadParams = new LinearLayout.LayoutParams(-2,-2);
        loadParams.topMargin = dp(12);
        loadingText.setLayoutParams(loadParams);
        splashView.addView(iconText);
        splashView.addView(appNameText);
        splashView.addView(loadingText);
        webView = new WebView(this);
        webView.setVisibility(View.INVISIBLE);
        FrameLayout.LayoutParams webParams = new FrameLayout.LayoutParams(-1,-1);
        webView.setLayoutParams(webParams);
        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setAllowFileAccess(true);
        s.setAllowContentAccess(true);
        s.setMixedContentMode(0);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                new Handler().postDelayed(() -> hideSplash(), 800);
            }
        });
        String url = getIntent().getStringExtra("url");
        if(url==null) url = SITE_URL;
        webView.loadUrl(url);
        root.addView(webView);
        root.addView(splashView);
        setContentView(root);
        new Handler().postDelayed(() -> hideSplash(), 4000);
    }

    private void loadConfig() {
        try {
            java.io.InputStream is = getAssets().open("config.json");
            java.util.Scanner sc = new java.util.Scanner(is).useDelimiter("\\A");
            String json = sc.hasNext() ? sc.next() : "";
            sc.close();
            if(json.trim().startsWith("{")) {
                JSONObject obj = new JSONObject(json);
                if(obj.has("websiteUrl")) SITE_URL = obj.getString("websiteUrl");
                if(obj.has("url")) SITE_URL = obj.getString("url");
                if(obj.has("appName")) APP_NAME = obj.getString("appName");
                if(obj.has("name")) APP_NAME = obj.getString("name");
            }
        } catch(Exception e) {}
    }

    private void hideSplash() {
        if(splashView.getVisibility() == 8) return;
        AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
        fadeOut.setDuration(500);
        fadeOut.setAnimationListener(new android.view.animation.Animation.AnimationListener() {
            public void onAnimationStart(android.view.animation.Animation a) {}
            public void onAnimationRepeat(android.view.animation.Animation a) {}
            public void onAnimationEnd(android.view.animation.Animation a) {
                splashView.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
                fadeIn.setDuration(300);
                webView.startAnimation(fadeIn);
            }
        });
        splashView.startAnimation(fadeOut);
    }

    private int dp(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()) webView.goBack(); else super.onBackPressed();
    }
}

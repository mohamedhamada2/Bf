package com.mz.bf.addsalary;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.mz.bf.R;
import com.mz.bf.api.MySharedPreference;
import com.mz.bf.authentication.LoginModel;

import androidx.appcompat.app.AppCompatActivity;

public class AddSalaryActivity extends AppCompatActivity {
    WebView webView;
    ProgressBar progressBar;
    ImageView back_img;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_salary);
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(this);
        webView = findViewById(R.id.webview);
        progressBar = findViewById(R.id.progressBar);
        back_img = findViewById(R.id.back_img);
        user_id = loginModel.getId()+"";
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://b.f.e.one-click.solutions/representative/Sale/sdad_pill/1/"+user_id);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    public class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }
    }
}
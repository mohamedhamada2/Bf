package com.mz.bf.uis.activity_print_bill;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.mz.bf.R;
import com.mz.bf.clientaccounting.ClientAccountingActivity;

public class PrintActivity extends AppCompatActivity {
    WebView webView;
    ProgressBar progressBar;
    ImageView back_img;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);
        webView = findViewById(R.id.webview);
        progressBar = findViewById(R.id.progressBar);
        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getDataIntent();
        webView.setWebViewClient(new PrintActivity.WebViewClient());
        webView.loadUrl("https://b.f.e.one-click.solutions/api_clients/Api_clients/print_fatora/"+id);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);
        PrintManager printManager = (PrintManager) this
                .getSystemService(Context.PRINT_SERVICE);

        PrintDocumentAdapter printAdapter =
                webView.createPrintDocumentAdapter("MyDocument");

        String jobName = getString(R.string.app_name) + " Print Test";

        printManager.print(jobName, printAdapter,
                new PrintAttributes.Builder().build());
    }

    private void getDataIntent() {
        id = getIntent().getStringExtra("id");

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
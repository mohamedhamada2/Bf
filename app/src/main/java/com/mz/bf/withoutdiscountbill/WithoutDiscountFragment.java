package com.mz.bf.withoutdiscountbill;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;


import com.mz.bf.R;
import com.mz.bf.api.MySharedPreference;
import com.mz.bf.authentication.LoginModel;

import androidx.fragment.app.Fragment;


public class WithoutDiscountFragment extends Fragment {
    WebView webView;
    ProgressBar progressBar;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    String user_id;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_without_discount, container, false);
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(getActivity());
        webView = view.findViewById(R.id.webview);
        progressBar = view.findViewById(R.id.progressBar);
        user_id = loginModel.getId()+"";
        webView.setWebViewClient(new WithoutDiscountFragment.WebViewClient());
        webView.loadUrl("https://abbgroup.org.uk/project/roxcelegypt/representative/Sale/sdad_pill/2/"+user_id);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);
        return  view;
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
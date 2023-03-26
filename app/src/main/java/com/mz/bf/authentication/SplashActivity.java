package com.mz.bf.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.mz.bf.MainActivity;
import com.mz.bf.R;
import com.mz.bf.api.CodeSharedPreferance;
import com.mz.bf.api.MySharedPreference;
import com.mz.bf.code.Code;
import com.mz.bf.code.CodeActivity;

public class SplashActivity extends AppCompatActivity {
    ImageView logo_img;
    MySharedPreference mySharedPreference;
    CodeSharedPreferance codeSharedPreferance;
    LoginModel loginModel;
    Code code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        logo_img = findViewById(R.id.logo_img);
        mySharedPreference = MySharedPreference.getInstance();
        codeSharedPreferance = CodeSharedPreferance.getInstance();
        loginModel = mySharedPreference.Get_UserData(SplashActivity.this);
        code = codeSharedPreferance.Get_UserData(SplashActivity.this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                logo_img.animate().rotationYBy(360f);
            }
        }, 1000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (code != null){
                    if (loginModel != null){
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }else {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                    }
                }else {
                    startActivity(new Intent(SplashActivity.this, CodeActivity.class));
                    finish();
                }
            }
        }, 2500);
    }
}
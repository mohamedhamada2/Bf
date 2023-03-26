package com.mz.bf.authentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.mz.bf.R;
import com.mz.bf.api.RetrofitClientInstance;
import com.mz.bf.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding activityLoginBinding;
    LoginViewModel loginViewModel;
    String user_name,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        activityLoginBinding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        loginViewModel = new LoginViewModel(this);
        activityLoginBinding.setLoginviewmodel(loginViewModel);
    }

    @Override
    public void onBackPressed() {
        RetrofitClientInstance.clear(this);
        super.onBackPressed();
    }

    public void Login(View view) {
        //startActivity(new Intent(LoginActivity.this, MainActivity.class));
        Validation();
    }

    private void Validation() {
        user_name = activityLoginBinding.etUserName.getText().toString();
        password = activityLoginBinding.etPassword.getText().toString();
        if (!TextUtils.isEmpty(user_name)&&!TextUtils.isEmpty(password)){
            loginViewModel.login(user_name,password);
        }else {
            if (TextUtils.isEmpty(user_name)){
                activityLoginBinding.etUserName.setError("برجاء إدخال إسم المستخدم");
            }else {
                activityLoginBinding.etUserName.setError(null);
            }
            if (TextUtils.isEmpty(password)){
                activityLoginBinding.etPassword.setError("برجاء إدخال كلمة المرور");
            }else {
                activityLoginBinding.etPassword.setError(null);
            }
        }
    }
}
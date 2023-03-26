package com.mz.bf.code;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mz.bf.R;
import com.mz.bf.api.GetDataService;
import com.mz.bf.api.RetrofitClientInstance;
import com.mz.bf.databinding.ActivityCodeBinding;

public class CodeActivity extends AppCompatActivity {
    ActivityCodeBinding activityCodeBinding;
    CodeViewModel codeViewModel;
    String code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);
        activityCodeBinding = DataBindingUtil.setContentView(this,R.layout.activity_code);
        codeViewModel = new CodeViewModel(this);

    }

    public void Login(View view) {
        code = activityCodeBinding.etUserName.getText().toString();
        codeViewModel.get_code(code);
    }

}
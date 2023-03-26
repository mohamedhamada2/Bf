package com.mz.bf.code;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.mz.bf.api.CodeSharedPreferance;
import com.mz.bf.api.GetDataService;
import com.mz.bf.api.RetrofitClientInstance;
import com.mz.bf.authentication.LoginActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CodeViewModel {
    CodeActivity codeActivity;
    CodeSharedPreferance codeSharedPreferance;
    Context context;
    String base_url;

    public CodeViewModel(Context context) {
        this.context = context;
        codeActivity = (CodeActivity) context;
        codeSharedPreferance = CodeSharedPreferance.getInstance();
        if (codeSharedPreferance.Get_UserData(context) == null){
            base_url = "https://b.f.e.one-click.solutions/";
        }else {

            base_url = codeSharedPreferance.Get_UserData(context).getRecords().getUrl();
        }
    }

    public void get_code(String code) {
        codeSharedPreferance = CodeSharedPreferance.getInstance();
        GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance2().create(GetDataService.class);
        Call<Code> call = getDataService.get_code(code);
        call.enqueue(new Callback<Code>() {
            @Override
            public void onResponse(Call<Code> call, Response<Code> response) {
                if (response.isSuccessful()){
                    if (response.body().getSuccess()==1){
                        codeSharedPreferance.Create_Update_UserData(context, response.body());
                        codeActivity.startActivity(new Intent(codeActivity, LoginActivity.class));
                    }else {
                        Toast.makeText(codeActivity, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Code> call, Throwable t) {

            }
        });
    }
}

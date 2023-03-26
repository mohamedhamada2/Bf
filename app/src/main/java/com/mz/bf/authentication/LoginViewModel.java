package com.mz.bf.authentication;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.mz.bf.MainActivity;
import com.mz.bf.Utilities.Utilities;
import com.mz.bf.api.CodeSharedPreferance;
import com.mz.bf.api.GetDataService;
import com.mz.bf.api.MySharedPreference;
import com.mz.bf.api.RetrofitClientInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginViewModel {
    Context context;
    LoginActivity loginActivity;
    LoginModel loginModel;
    MySharedPreference mprefs;
    CodeSharedPreferance codeSharedPreferance;
    String base_url;

    public LoginViewModel(Context context) {
        this.context = context;
        loginActivity = (LoginActivity) context;

    }

    public void login(String username, String password) {
        codeSharedPreferance = CodeSharedPreferance.getInstance();
        if (codeSharedPreferance.Get_UserData(context) == null){
            base_url = "https://b.f.e.one-click.solutions/";
        }else {

            base_url = codeSharedPreferance.Get_UserData(context).getRecords().getUrl();
        }
        mprefs = MySharedPreference.getInstance();
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(context,base_url).create(GetDataService.class);
            Call<LoginModel> call = getDataService.login_user(username,password);
            call.enqueue(new Callback<LoginModel>() {
                @Override
                public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getSuccess() ==1){
                            loginModel = response.body();
                            mprefs.Create_Update_UserData(context,loginModel);
                            Toast.makeText(context, "تم تسجيلك بنجاح", Toast.LENGTH_SHORT).show();
                            context.startActivity(new Intent(context, MainActivity.class));
                            //Animatoo.animateFade(context);
                            loginActivity.finish();
                        }else {
                            Toast.makeText(context, "بياناتك غير صحيحية", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginModel> call, Throwable t) {

                }
            });
            /*call.enqueue(new Callback<LoginModel>() {
                @Override
                public void onResponse(Call<LoginModel> call, LoginModel response) {
                    if (response.isSuccessful()){
                        if (response.body().getSuccess() ==1){
                            loginModel = response.body();
                            mprefs.Create_Update_UserData(context,loginModel);
                            Toast.makeText(context, "تم تسجيلك بنجاح", Toast.LENGTH_SHORT).show();
                            context.startActivity(new Intent(context, MainActivity.class));
                            //Animatoo.animateFade(context);
                            loginActivity.finish();
                        }else {
                            Toast.makeText(context, "بياناتك غير صحيحية", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginModel> call, Throwable t) {

                }
            });*/
        }
    }
}

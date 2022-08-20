package com.mz.bf.profile;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.mz.bf.Utilities.Utilities;
import com.mz.bf.api.GetDataService;
import com.mz.bf.api.RetrofitClientInstance;
import com.mz.bf.authentication.LoginModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileViewModel {
    Context context;
    ProfileActivity profileActivity;

    public ProfileViewModel(Context context) {
        this.context = context;
        profileActivity = (ProfileActivity) context;
    }

    public void getUserData(String user_id) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<LoginModel> call = getDataService.get_user_data(user_id);
            call.enqueue(new Callback<LoginModel>() {
                @Override
                public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getSuccess() ==1){
                            profileActivity.setData(response.body());
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginModel> call, Throwable t) {

                }
            });
        }
    }

    public void update_user_with_img(String user_id, String name, String password, Uri filepath) {
        RequestBody rb_user_id= Utilities.getRequestBodyText(user_id);
        RequestBody rb_name= Utilities.getRequestBodyText(name);
        RequestBody rb_password= Utilities.getRequestBodyText(password+"");
        MultipartBody.Part img = Utilities.getMultiPart(context, filepath, "img");
        if (Utilities.isNetworkAvailable(context)) {
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<LoginModel> call = getDataService.update_data_with_img(rb_user_id,rb_name,rb_password,img);
            call.enqueue(new Callback<LoginModel>() {
                @Override
                public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                    if (response.isSuccessful()){
                        Toast.makeText(context, "تم نعديل البيانات بنجاح", Toast.LENGTH_SHORT).show();
                        getUserData(user_id);
                    }
                }

                @Override
                public void onFailure(Call<LoginModel> call, Throwable t) {

                }
            });
        }
    }


    public void update_user_without_img(String user_id, String name, String password) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<LoginModel> call = getDataService.update_data_without_img(user_id,name,password);
            call.enqueue(new Callback<LoginModel>() {
                @Override
                public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getSuccess()==1){
                            Toast.makeText(context, "تم نعديل البيانات بنجاح", Toast.LENGTH_SHORT).show();
                            getUserData(user_id);
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginModel> call, Throwable t) {

                }
            });

        }
    }
}

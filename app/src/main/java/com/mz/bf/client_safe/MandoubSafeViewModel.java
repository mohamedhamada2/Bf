package com.mz.bf.client_safe;

import android.content.Context;

import com.mz.bf.Utilities.Utilities;
import com.mz.bf.api.CodeSharedPreferance;
import com.mz.bf.api.GetDataService;
import com.mz.bf.api.RetrofitClientInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MandoubSafeViewModel {
    Context context;
    MandoubSafeActivity mandoubSafeActivity;
    CodeSharedPreferance codeSharedPreferance;
    String base_url;

    public MandoubSafeViewModel(Context context) {
        this.context = context;
        mandoubSafeActivity = (MandoubSafeActivity) context;
        codeSharedPreferance = CodeSharedPreferance.getInstance();
        if (codeSharedPreferance.Get_UserData(context) == null){
            base_url = "https://b.f.e.one-click.solutions/";
        }else {

            base_url = codeSharedPreferance.Get_UserData(context).getRecords().getUrl();
        }
    }

    public void get_mandoub_safe(String user_id, String bill_date) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance(context,base_url).create(GetDataService.class);
            Call<SafeModel> call = getDataService.get_mandoub_safe(user_id,bill_date);
            call.enqueue(new Callback<SafeModel>() {
                @Override
                public void onResponse(Call<SafeModel> call, Response<SafeModel> response) {
                    if (response.isSuccessful()){
                        mandoubSafeActivity.setData(response.body());
                    }
                }

                @Override
                public void onFailure(Call<SafeModel> call, Throwable t) {

                }
            });
        }
    }
}

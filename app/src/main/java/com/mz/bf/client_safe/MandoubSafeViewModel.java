package com.mz.bf.client_safe;

import android.content.Context;

import com.mz.bf.Utilities.Utilities;
import com.mz.bf.api.GetDataService;
import com.mz.bf.api.RetrofitClientInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MandoubSafeViewModel {
    Context context;
    MandoubSafeActivity mandoubSafeActivity;

    public MandoubSafeViewModel(Context context) {
        this.context = context;
        mandoubSafeActivity = (MandoubSafeActivity) context;
    }

    public void get_mandoub_safe(String user_id, String bill_date) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
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

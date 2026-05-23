package com.mz.bf.uis.activity_print_bill;

import android.content.Context;

import com.mz.bf.Utilities.Utilities;
import com.mz.bf.api.GetDataService;
import com.mz.bf.api.RetrofitClientInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrintSandViewModel {
    Context context;

    public PrintSandViewModel(Context context) {
        this.context = context;
        printSandActivity = (PrintSandActivity) context;
    }

   PrintSandActivity printSandActivity;

    public void get_easl_details(String sand_id) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<EsalModel> call = getDataService.get_esal_details(sand_id);
            call.enqueue(new Callback<EsalModel>() {
                @Override
                public void onResponse(Call<EsalModel> call, Response<EsalModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getSuccess() == 1){
                            printSandActivity.setData(response.body().getEsal());
                        }
                    }
                }

                @Override
                public void onFailure(Call<EsalModel> call, Throwable t) {

                }
            });
        }
    }
}
